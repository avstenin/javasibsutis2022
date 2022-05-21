import dao.PhoneDirectoryDao;
import dto.PhoneDirectoryFilter;
import entity.PhoneDirectory;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int mode;
        while (true) {
            System.out.println();
            System.out.println("""
                    Select an action:
                    \t1.Show all records
                    \t2.Find records by parameter
                    \t3.Insert new records
                    \t4.Update records
                    \t5.Delete record by Id""");
            System.out.print("Enter the action or exit (enter 0): ");
            mode = sc.nextInt();
            if (mode == 0) {
                System.out.println("Exiting...");
                break;
            }
            if (mode < 0 || mode > 5) {
                System.out.print("Wrong action! Enter the action: ");
                continue;
            }
            switch (mode) {
                case 1:
                    PhoneDirectoryDao.getInstance().findAll().forEach(System.out::println);
                    break;
                case 2:
                    selectFind(sc);
                    break;
                case 3:
                    insert(sc);
                    break;
                case 4:
                    update(sc);
                    break;
                case 5:
                    delete(sc);
            }
        }
    }

    private static void delete(Scanner sc) {
        System.out.print("Enter ID of record, which you want to delete: ");
        int id = sc.nextInt();
        boolean delete = PhoneDirectoryDao.getInstance().delete(id);
        if (delete) {
            System.out.println("The record was deleted!");
        } else {
            System.out.println("Couldn't find a record by this ID!");
        }
    }

    private static void update(Scanner sc) {
        System.out.print("Enter ID of record, which you want to update: ");
        int id = sc.nextInt();
        System.out.print("Enter new values for this template - \"[first_name]," +
                "[second_name],[phone_number],[email]\".\n" +
                "If parameter is empty - enter \"null\": ");
        sc.nextLine();
        String newValues = sc.nextLine();
        int recordCount = getUpdatedRecordCount(id, newValues);
        if (recordCount == 0) {
            System.out.println("Couldn't find a record by this ID!");
        } else {
            System.out.println("The record was updated!");
        }
    }

    private static int getUpdatedRecordCount(int id, String newValues) {
        String[] parseParameters = parseAndNullingString(newValues);
        return PhoneDirectoryDao.getInstance().update(new PhoneDirectory(
                id,
                parseParameters[0],
                parseParameters[1],
                parseParameters[2],
                parseParameters[3]
        ));
    }

    private static void insert(Scanner sc) {
        System.out.println("How many records do you want to enter?");
        System.out.print("Enter count of records: ");
        int count = sc.nextInt();
        System.out.print("Enter values for this template - \"[first_name]," +
                "[second_name],[phone_number],[email]\".\n" +
                "If parameter is empty - enter \"null\": ");
        sc.nextLine();
        List<PhoneDirectory> records = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            String values = sc.nextLine();
            PhoneDirectory record = getRecordClassByValues(values);
            records.add(record);
        }
        System.out.println("These records have been inserted: ");
        PhoneDirectoryDao.getInstance().save(records).forEach(System.out::println);
    }

    private static PhoneDirectory getRecordClassByValues(String values) {
        String[] parseParameters = parseAndNullingString(values);
        return new PhoneDirectory(
                null,
                parseParameters[0],
                parseParameters[1],
                parseParameters[2],
                parseParameters[3]
        );
    }

    private static void selectFind(Scanner sc) {
        int mode;
        while (true) {
            System.out.println();
            System.out.println("""
                    Select an action:
                    \t1.Find record by Id
                    \t2.Find records by another parameter""");
            System.out.print("Enter the action or go to previous menu (enter 0): ");
            mode = sc.nextInt();
            if (mode == 0) {
                System.out.println("Go to previous menu");
                break;
            }
            if (mode < 0 || mode > 2) {
                System.out.print("Wrong action! Enter the action: ");
                continue;
            }
            switch (mode) {
                case 1:
                    System.out.print("Enter Id: ");
                    int id = sc.nextInt();
                    Optional<PhoneDirectory> record = PhoneDirectoryDao.getInstance().findById(id);
                    if (record.isPresent()) {
                        System.out.println(record.get());
                    } else {
                        System.out.println("Couldn't find a record by Id = " + id);
                    }
                    System.out.println();
                    break;
                case 2:
                    System.out.print("Enter parameters for this template - \"[first_name]," +
                            "[second_name],[phone_number],[email]\".\n" +
                            "If parameter is empty - enter \"null\": ");
                    sc.nextLine();
                    String parameters = sc.nextLine();
                    List<PhoneDirectory> records = getRecordsByParameters(parameters);
                    if (records.isEmpty()) {
                        System.out.println("Couldn't find a record by this parameters!");
                    } else {
                        records.forEach(System.out::println);
                    }
            }
        }
    }

    private static List<PhoneDirectory> getRecordsByParameters(String parameters) {
        String[] parseParameters = parseAndNullingString(parameters);
        return PhoneDirectoryDao.getInstance().findAll(
                new PhoneDirectoryFilter(parseParameters[0], parseParameters[1],
                        parseParameters[2], parseParameters[3])
        );
    }

    private static String[] parseAndNullingString(String newValues) {
        String[] parseParameters = newValues.replaceAll(" ", "").split(",");
        for (int i = 0; i < parseParameters.length; i++) {
            if (parseParameters[i].equalsIgnoreCase("null")) {
                parseParameters[i] = null;
            }
        }
        return parseParameters;
    }
}