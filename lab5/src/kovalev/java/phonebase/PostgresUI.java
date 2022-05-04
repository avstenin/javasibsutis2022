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

    @Override
    public void connect()
    throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(dbUrl, dbCredentials);
    }

    @Override
    public void closeConnection()
    throws SQLException {
        connection.close();
    }

    @Override
    public void insert(PhoneRecord... phoneRecords)
    throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            for (PhoneRecord record : phoneRecords) {
                stmt.executeUpdate("INSERT INTO phonedb " + record.sqlColumns() + " VALUES " + record.sqlValues() + ";");
            }
        }
    }

    @Override
    public PhoneRecord[] selectAll()
    throws SQLException {
        List<PhoneRecord> records = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM phonedb")) {
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
    public PhoneRecord findByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PhoneRecord findByPhone(String phone) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PhoneRecord findByEmail(String email) {
        // TODO Auto-generated method stub
        return null;
    }
}
