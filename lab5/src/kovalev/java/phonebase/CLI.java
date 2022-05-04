package phonebase;

import java.sql.SQLException;
import java.util.Scanner;

public class CLI
{
    public static void main(String[] args) {
        try (DBUI dbui = new PostgresUI();
             Scanner sc = new Scanner(System.in))
        {
            ConsoleUI.interact(dbui, sc);
        }
        catch (ClassNotFoundException e) {
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