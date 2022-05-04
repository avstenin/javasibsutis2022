package phonebase;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

interface Callable {
    boolean call(String[] args);
}

public final class ConsoleUI {

    private ConsoleUI() {}

    private static String fFirstName, fLastName, fPhone, fEMail;
    private static DBUI dbi;

    private static HashMap<String, String> cmdHelpMap = new HashMap<>();
    static {
        cmdHelpMap.put("set", "set {Field: FirstName|LastName|Phone|EMail} [Val] : ставит/убирает (если Val не указан) фильтр на поле Field. По умолчанию все фильтры очищены.");
        cmdHelpMap.put("get", "get : по назначенным фильтрам возвращает значения из БД.");
        cmdHelpMap.put("filters", "filters : возвращает список из фильтров.");
        cmdHelpMap.put("insert", "insert {FirstName} {LastName} {Phone} [EMail] : вставляет новую запись в БД. Все поля, кроме EMail, обязательны.");
        cmdHelpMap.put("exit", "exit : выход из программы.");
    }

    private static HashMap<String, Callable> cmdMap = new HashMap<>();
    static {
        cmdMap.put("set", ((args)     -> { return set(args); }));
        cmdMap.put("get", ((args)     -> { return get(args); }));
        cmdMap.put("filters", ((args) -> { return filters(args); }));
        cmdMap.put("insert", ((args)  -> { return insert(args); }));
        cmdMap.put("exit", ((args)    -> { return exit(args); }));
        cmdMap.put("help", ((args)    -> { return help(args); }));
    }


    private static boolean set(String[] args) {
        if ((args.length < 2) || (args.length > 3)) {
            return help(args);
        }

        String changeTo = (args.length == 3) ? args[2] : null;

        String field = args[1];
        switch (field) {
            case "FirstName": fFirstName = changeTo; break;
            case "LastName": fLastName = changeTo; break;
            case "Phone": fPhone = changeTo; break;
            case "EMail": fEMail = changeTo; break;
            default: return help(args);
        }

        return true;
    }

    @SuppressWarnings("unused")
    private static boolean get(String[] args) {
        try {
            PhoneRecord[] records = dbi.find(fFirstName, fLastName, fPhone, fEMail);
            if (records.length > 0) {
                for (PhoneRecord record : records)
                    System.out.println(record.toString());
            } else {
                System.out.println("Записи по этим фильтрам отсутствуют.");
            }
        } catch (SQLException e) {
            System.err.println("!!! Произошла ошибка при вызове 'get'.");
            e.printStackTrace();
        }
        return true;
    }

    @SuppressWarnings("unused")
    private static boolean filters(String[] args) {
        System.out.printf("%12s | %s\n", "fFirstName", fFirstName != null ? fFirstName : "-");
        System.out.printf("%12s | %s\n", "fLastName", fLastName != null ? fLastName : "-");
        System.out.printf("%12s | %s\n", "fPhone", fPhone != null ? fPhone : "-");
        System.out.printf("%12s | %s\n", "fEMail", fEMail != null ? fEMail : "-");
        return true;
    }


    private static boolean insert(String[] args) {
        if ((args.length < 4) || (args.length > 5)) {
            return help(args);
        }
        try {
            dbi.insert(new PhoneRecord(args[1], args[2], args[3], args.length == 5 ? args[4] : null));
        } catch (SQLException e) {
            System.err.println("!!! Произошла ошибка при вызове 'insert'.");
            e.printStackTrace();
        }
        return true;
    }

    @SuppressWarnings("unused")
    private static boolean exit(String[] args) {
        return false;
    }


    private static boolean help(String[] args) {
        String cmdForHelp = ((args.length >= 2) && (args[0] == "help")) ? args[1] : args[0];

        if (cmdHelpMap.keySet().contains(cmdForHelp)) {
            System.out.println(cmdHelpMap.get(cmdForHelp));
        } else {
            for (String cmdHelp : cmdHelpMap.values()) {
                System.out.println(cmdHelp);
            }
        }
        return true;
    }


    public static void interact(DBUI dbui, Scanner sc) {
        dbi = dbui;
        String[] tokens = {"help"};
        while (cmdMap.get(tokens[0]).call(tokens)) {
            tokens = sc.nextLine().strip().split("\\s+");
            if (tokens.length == 0) {
                tokens = new String[]{"help"};
            }
        }
    }
}
