package phonebase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
    public boolean insert(PhoneRecord... phoneRecords) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public PhoneRecord[] selectAll() {
        // TODO Auto-generated method stub
        return null;
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
