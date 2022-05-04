package phonebase;

import java.sql.Connection;
import java.sql.SQLException;

public class CLI
{
    public static void main(String[] args) {
        DBUI dbui = new PostgresUI();

        Connection connection;
        try {
            connection = dbui.connect();
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver не найден.");
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            System.err.println("Соединение с PostgreSQL не удалось.");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.err.println("Соединение удалось!");
        }
    }
}