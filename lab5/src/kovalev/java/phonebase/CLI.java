package phonebase;

import java.sql.SQLException;

public class CLI
{
    public static void main(String[] args) {
        DBUI dbui = new PostgresUI();

        try {
            dbui.connect();
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver не найден.");
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            System.err.println("Соединение с PostgreSQL не удалось.");
            e.printStackTrace();
            return;
        }

        System.out.println("Соединение удалось!");

        try {
            dbui.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}