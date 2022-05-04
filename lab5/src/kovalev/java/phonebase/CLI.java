package phonebase;

import java.sql.SQLException;

public class CLI
{
    public static void main(String[] args) {
        try (DBUI dbui = new PostgresUI()) {
            System.out.println("Соединение удалось!");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver не найден.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Соединение с PostgreSQL не удалось.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Проблемы с закрытием соединения с PostgreSQL.");
            e.printStackTrace();
        }
    }
}