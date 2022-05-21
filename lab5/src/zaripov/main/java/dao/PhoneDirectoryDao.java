package dao;

import dto.PhoneDirectoryFilter;
import entity.PhoneDirectory;
import util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class PhoneDirectoryDao {
    private static final PhoneDirectoryDao INSTANCE = new PhoneDirectoryDao();

    private static final String DELETE_SQL = """
            DELETE FROM phone_directory
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO phone_directory(first_name, last_name, phone_number, email)
            VALUES\040
                (?, ?, ?, ?)""";
    private static final String UPDATE_SQL = """
            UPDATE phone_directory
            SET first_name = ?,
                last_name = ?,
                phone_number = ?,
                email = ?
            WHERE id = ?
            """;
    private static final String FIND_ALL = """
            SELECT id,
                first_name,
                last_name,
                phone_number,
                email
            FROM phone_directory
            """;
    private static final String FIND_BY_ID = FIND_ALL + """
            WHERE id = ?
            """;

    private PhoneDirectoryDao() {
    }

    public static PhoneDirectoryDao getInstance() {
        return INSTANCE;
    }

    private PhoneDirectory buildPhoneDirectory(ResultSet resultSet) throws SQLException {
        return new PhoneDirectory(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("phone_number"),
                resultSet.getString("email")
        );
    }

    public List<PhoneDirectory> findAll(PhoneDirectoryFilter filter) {
        List<Object> parameters = new LinkedList<>();
        List<String> whereSql = new LinkedList<>();
        if (filter.firstName() != null) {
            whereSql.add("first_name = ?");
            parameters.add(filter.firstName());
        }
        if (filter.secondName() != null) {
            whereSql.add("last_name = ?");
            parameters.add(filter.secondName());
        }
        if (filter.phoneNumber() != null) {
            whereSql.add("phone_number = ?");
            parameters.add(filter.phoneNumber());
        }
        if (filter.email() != null) {
            whereSql.add("email LIKE ?");
            parameters.add("%" + filter.email() + "%");
        }
        String where;
        String sql;
        if (!whereSql.isEmpty()) {
            where = whereSql.stream().collect(joining(" AND ", "WHERE ", ""));
            sql = FIND_ALL + where;
        } else {
            sql = FIND_ALL;
        }

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            List<PhoneDirectory> phoneDirectories = new LinkedList<>();
            while (resultSet.next()) {
                phoneDirectories.add(buildPhoneDirectory(resultSet));
            }
            return phoneDirectories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PhoneDirectory> findAll() {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<PhoneDirectory> phoneDirectories = new LinkedList<>();
            while (resultSet.next()) {
                phoneDirectories.add(buildPhoneDirectory(resultSet));
            }
            return phoneDirectories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<PhoneDirectory> findById(Integer id) {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            PhoneDirectory phoneDirectory = null;
            if (resultSet.next()) {
                phoneDirectory = buildPhoneDirectory(resultSet);
            }
            return Optional.ofNullable(phoneDirectory);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int update(PhoneDirectory phoneDirectory) {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, phoneDirectory.getFirstName());
            preparedStatement.setString(2, phoneDirectory.getLastName());
            preparedStatement.setString(3, phoneDirectory.getPhoneNumber());
            preparedStatement.setString(4, phoneDirectory.getEmail());
            preparedStatement.setInt(5, phoneDirectory.getId());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PhoneDirectory> save(List<PhoneDirectory> phoneDirectories) {
        List<String> valuesSql = new LinkedList<>();
        List<List<Object>> allValues = new LinkedList<>();
        if (phoneDirectories.size() == 1) {
            List<Object> values = new LinkedList<>();
            values.add(phoneDirectories.get(0).getFirstName());
            values.add(phoneDirectories.get(0).getLastName());
            values.add(phoneDirectories.get(0).getPhoneNumber());
            values.add(phoneDirectories.get(0).getEmail());
            allValues.add(values);
        } else {
            boolean firstIteration = true;
            for (var phoneDirectory : phoneDirectories) {
                List<Object> values = new LinkedList<>();
                values.add(phoneDirectory.getFirstName());
                values.add(phoneDirectory.getLastName());
                values.add(phoneDirectory.getPhoneNumber());
                values.add(phoneDirectory.getEmail());
                allValues.add(values);
                if (firstIteration) {
                    firstIteration = false;
                    continue;
                }
                valuesSql.add("(?, ?, ?, ?)");
            }
        }

        String sql;
        if (!valuesSql.isEmpty()) {
            String values = valuesSql.stream().collect(joining(",\n\t", ",\n\t", ""));
            sql = SAVE_SQL + values;
        } else {
            sql = SAVE_SQL;
        }
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            for (var currentValues : allValues) {
                preparedStatement.setObject(index++, currentValues.get(0));
                preparedStatement.setObject(index++, currentValues.get(1));
                preparedStatement.setObject(index++, currentValues.get(2));
                preparedStatement.setObject(index++, currentValues.get(3));
            }

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            index = 0;
            while (generatedKeys.next()) {
                phoneDirectories.get(index++).setId(generatedKeys.getInt("id"));
            }
            return phoneDirectories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Integer id) {
        try (var connection = ConnectionManager.open();
             var prepareStatement = connection.prepareStatement(DELETE_SQL)) {
            prepareStatement.setInt(1, id);
            return prepareStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
