package phonebase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PostgresUI implements DBUI {

    private static final String dbUrl  = "jdbc:postgresql://127.0.0.1:5432/phonedb";
    private static Properties dbCredentials = new Properties();
    static {
        dbCredentials.put("user", "pbuser");
        dbCredentials.put("password", "pbpwd");
    }
    private Connection connection;

    public PostgresUI()
    throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(dbUrl, dbCredentials);
    }

    @Override
    public void close()
    throws SQLException {
        connection.close();
    }

    @Override
    public void insert(PhoneRecord... phoneRecords)
    throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            for (PhoneRecord record : phoneRecords) {
                stmt.executeUpdate("INSERT INTO PhoneBook " + record.sqlColumns() + " VALUES " + record.sqlValues() + ";");
            }
        }
    }

    private PhoneRecord[] getRecords(String query)
    throws SQLException {
        List<PhoneRecord> records = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                records.add(new PhoneRecord(rs.getString("FirstName"),
                                            rs.getString("LastName"),
                                            rs.getString("Phone"),
                                            rs.getString("EMail")));
            }
        }
        return records.toArray(PhoneRecord[]::new);
    }

    @Override
    public PhoneRecord[] selectAll()
    throws SQLException {
        return getRecords(String.format("SELECT * FROM PhoneBook"));
    }

    @Override
    public PhoneRecord[] find(String firstName, String lastName, String phone, String email)
    throws SQLException {
        int n = 0;
        String query = "SELECT * FROM PhoneBook ";
        if (firstName != null) {
            query += (n++ == 0) ? "WHERE " : "AND ";
            query += String.format("FirstName == '%s' ", firstName);
        }
        if (lastName != null) {
            query += (n++ == 0) ? "WHERE " : "AND ";
            query += String.format("LastName == '%s' ", lastName);
        }
        if (phone != null) {
            query += (n++ == 0) ? "WHERE " : "AND ";
            query += String.format("Phone == '%s' ", phone);
        }
        if (phone != null) {
            query += (n++ == 0) ? "WHERE " : "AND ";
            query += String.format("EMail == '%s' ", email);
        }
        return getRecords(query);
    }
}
