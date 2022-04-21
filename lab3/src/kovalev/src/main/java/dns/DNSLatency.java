package dns;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class DNSLatency {
    private static File inputFile = new File("csv/");
    private static String outputFilePath;

    /**
     * Обработка поданных аргументов с использованием Commons CLI.
     * Даёт пользователю возможность задать свои пути ввода/вывода.
     * 
     * @param args Аргументы, поданные через CLI.
     */
    private static void parseArgs(String[] args) {
        Options options = new Options();
        options.addOption("i", "input", true, "Input File/Directory");
        options.addOption("o", "output", true, "Output CSV File");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("DNSLatency", options);
            System.exit(1);
        }

        if (cmd.hasOption("input"))
            inputFile = new File(cmd.getOptionValue("input"));

        if (cmd.hasOption("output"))
            outputFilePath = cmd.getOptionValue("output");
        else {
            DateFormat df = new SimpleDateFormat("yyyy-dd-MM_HH-mm-ss");
            Date now = Calendar.getInstance().getTime();
            String suffix = df.format(now);
            outputFilePath = "csv/DNSQueries__" + suffix + ".csv";
        }
    }

    /**
     * Чтение логов в формате CSV.
     * Если ранее подан файл, читается только он.
     * Если подана директория (по умолчанию), из неё читаются рекурсивно файлы CSV.
     */
    private static void readCsv()
    throws IOException {
        if (inputFile.isDirectory())
        {
            Files.walk(inputFile.toPath())
                 .filter(name -> name.toString().endsWith(".csv"))
                 .forEach(t -> {
                    try {
                        PingTimeMeasure.readCsv(t);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                 });
        }
        else
        {
            PingTimeMeasure.readCsv(inputFile.toPath());
        }
    }

    /**
     * Используя консольный ввод, запрашивается количество серверов, которые введёт пользователь.
     * 
     * @return Количество серверов на проверку производительности.
     */
    private static int requestIpQueryNum() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите число серверов DNS:");
            while (true) {
                try {
                    return Integer.parseInt(scanner.next());
                }
                catch (NumberFormatException e) {
                    System.out.println("Введите корректное число!");
                }
            }
        }
    }

    /**
     * Используя консольный ввод, запрашивается доменное имя сервера.
     * Если сервер уже проходил тестирование, запрашивается разрешение на сброс результата.
     * 
     * @param i Порядковый номер сервера
     * @return Строка доменного имени сервера, либо null
     */
    private static String requestDnsServer(int i) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите адрес сервера DNS" + String.valueOf(i) + ":");
            String ip = scanner.next();
            PingResult query = PingTimeMeasure.findQuery(ip);
            if (query != null) {
                System.out.println("Сервер по адресу '" + ip + "' уже был протестирован. Его результат: " + query.time() + " мс.");
                System.out.println("Хотите сбросить результат? [y/N]");
                String answer = scanner.next();
                if (answer.toLowerCase() == "y")
                    PingTimeMeasure.remove(ip);
                else
                    return null;
            }
            return ip;
        }
    }
    public static void main(String[] args)
    throws IOException {
        parseArgs(args);
        readCsv();

        int ipQueryNum = requestIpQueryNum();

        try {
            for (int i = 1; i <= ipQueryNum; i++) {
                String ip = requestDnsServer(i);
                System.out.println();
                if (ip != null)
                    PingTimeMeasure.add(ip);
            }
        } catch (InterruptedException | NoSuchElementException e) {
            System.out.println("Зафиксировали прерывание, выходим.");
        }
        finally {
            PingTimeMeasure.print();
            PingTimeMeasure.writeToFile(outputFilePath);
        }
    }
}