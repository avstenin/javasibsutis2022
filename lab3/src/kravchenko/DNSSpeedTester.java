import org.apache.commons.cli.*;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DNSSpeedTester {
    final private static byte dnsPort = 53;
    final private static short timeout = 5000;
    Set<DNS> dnsList = new HashSet<>();

    private boolean fileInput = false;

    private double getAverageConnectionTime(DNS dns) {
        byte requests = 10;
        double allTime = 0;
        for (byte i = 0; i < requests; ++i) {
            try (Socket socket = new Socket()) {
                double time = System.currentTimeMillis();
                socket.connect(new InetSocketAddress(dns.getIp(), dnsPort), timeout);
                time = (System.currentTimeMillis() - time);
                allTime += time;
            } catch (IOException e) {
                return -1;
            }
        }
        try {
            dns.setName(InetAddress.getByName(dns.getIp()).getHostName());
        } catch (UnknownHostException ignored){}

        return allTime / requests;
    }

    private void fillDNSListFromInput() {
        Scanner input = new Scanner(System.in);
        long dnsCount;
        if (!fileInput) {
            System.out.println("Enter the number of DNS servers:");
            do {
                System.out.print(">");
                dnsCount = input.nextLong();
            } while (dnsCount <= 0);
        } else
            dnsCount = input.nextLong();
        for (long index = 1; index <= dnsCount; ++index) {
            if (!fileInput) {
                System.out.println("Enter ip address:");
                System.out.print(">");
            }
            DNS dns = new DNS(input.next());
            if (dnsList.contains(dns))
                continue;
            dns.setConnectionTime(getAverageConnectionTime(dns));
            if (dns.getConnectionTime() != -1) dnsList.add(dns);
        }
    }

    private void printDNSList() {
        System.out.println("Answer:");
        if (dnsList.isEmpty()) System.out.println("All is unreachable!");
        else {
            ArrayList<DNS> arrayList = new ArrayList<>(dnsList);
            Collections.sort(arrayList);
            for (DNS dns : arrayList)
                System.out.println(dns.toString());
        }
    }

    private void saveDNSList() {
        if (!dnsList.isEmpty()) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
            String filename = dtf.format(LocalDateTime.now()) + ".log";
            try (FileWriter file = new FileWriter(filename)) {
                for (DNS dns : dnsList)
                    file.write(dns.toString() + '\n');
            } catch (Exception e) {
                System.out.println("An error occurred while trying to save to file.");
            }
        }
    }

    public static void main(String[] args) {
        new DNSSpeedTester().run(args);
    }

    public void run(String[] args) {
        parseArgs(args);
        fillDNSListFromInput();
        printDNSList();
        saveDNSList();
    }

    private void parseArgs(String[] args) {
        Options options = new Options();
        options.addOption(new Option("i", "input", true, "Set input file"));
        options.addOption(new Option("h", "history", false, "Show history"));
        options.addOption(new Option("u", "usage", false, "Print usage"));
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp(" ", options);
            System.exit(1);
        }
        if (cmd.hasOption("usage")) {
            formatter.printHelp(" ", options);
            System.exit(0);
        } else if (cmd.hasOption("history")) {
            printHistory();
            System.exit(0);
        } else if (cmd.hasOption("input")) {
            File file = new File(cmd.getOptionValue("input"));
            try {
                InputStream inputStream = new FileInputStream(file);
                System.setIn(inputStream);
                fileInput = true;
            } catch (FileNotFoundException ignored) {
                System.err.println("Can't open file!");
                System.exit(1);
            }
        }
    }

    private void printHistory() {
        Map<String, File> files = new TreeMap<>();
        File currentDir = new File(".");
        recursiveSearchLogfiles(currentDir, files);
        for (Map.Entry<String, File> file : files.entrySet()) {
            System.out.println('[' + file.getKey() + ']');
            printFile(file.getValue());
        }
        if (files.isEmpty())
            System.out.println("History is empty!");
    }

    private void recursiveSearchLogfiles(File dir, Map<String, File> files) {
        if (dir.isDirectory()) {
            if (dir.canRead()) {
                for (File file : Objects.requireNonNull(dir.listFiles())) {
                    if (file.isDirectory()) {
                        recursiveSearchLogfiles(file, files);
                    } else {
                        String filename = file.getName();
                        if (filename.contains(".dlog") && ".dlog".equals(filename.substring(filename.length() - 4))) {
                            files.put(filename, file);
                        }
                    }
                }
            }
        }
    }

    private void printFile(File file) {
        try (FileReader fileReader = new FileReader(file)) {
            Scanner input = new Scanner(fileReader);
            while (input.hasNext())
                System.out.println(input.nextLine());
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
