package phonebase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class CLI
{
    private static final String dbUrl  = "jdbc:postgresql://127.0.0.1:5432/phonedb";

    private static Properties dbCredentials = new Properties();
    static {
        dbCredentials.put("user", "pbuser");
        dbCredentials.put("password", "pbpwd");
    }

    public static Connection connectToPostgres() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver не найден.");
            e.printStackTrace();
            return null;
        }

        try {
            return DriverManager.getConnection(dbUrl, dbCredentials);
        } catch (SQLException e) {
            System.err.println("Соединение с PostgreSQL не удалось.");
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) {
        Connection connection = connectToPostgres();

        if (connection != null) {
            System.err.println("Соединение удалось!");
        }
    }
}