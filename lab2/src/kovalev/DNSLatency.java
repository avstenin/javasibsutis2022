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
    public static void main(String[] args)
    throws IOException {
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

        File inputFile = new File(cmd.hasOption("input") ? cmd.getOptionValue("input") : "csv/");
        String outputFilePath;
        if (cmd.hasOption("output"))
            outputFilePath = cmd.getOptionValue("output");
        else {
            DateFormat df = new SimpleDateFormat("yyyy-dd-MM_HH-mm-ss");
            Date now = Calendar.getInstance().getTime();
            String suffix = df.format(now);
            outputFilePath = "csv/DNSQueries__" + suffix + ".csv";
        }

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

        try (Scanner scanner = new Scanner(System.in)) {
            int ipQueryNum;
            System.out.println("Введите число серверов DNS:");
            while (true) {
                try {
                    ipQueryNum = Integer.parseInt(scanner.next());
                    break;
                }
                catch (NumberFormatException e) {
                    System.out.println("Введите корректное число!");
                }
            }

            try {
                for (int i = 1; i <= ipQueryNum; i++) {
                    System.out.println("Введите адрес сервера DNS" + String.valueOf(i) + ":");
                    String ip = scanner.next();
                    var query = PingTimeMeasure.findQuery(ip);
                    if (query != null) {
                        System.out.println("Сервер по адресу '" + ip + "' уже был протестирован. Его результат: " + query.time() + " мс.");
                        System.out.println("Хотите сбросить результат? [y/N]");
                        char answer = (char) System.in.read();
                        if ((answer == 'Y') || (answer == 'y'))
                            PingTimeMeasure.remove(ip);
                        else
                            continue;
                    }
                    System.out.println();
                    PingTimeMeasure.add(ip);
                }
            } catch (InterruptedException | NoSuchElementException e) {
                System.out.println("Зафиксировали прерывание, выходим.");
            }
            
        }

        PingTimeMeasure.print();
        PingTimeMeasure.writeToFile(outputFilePath);
    }
}