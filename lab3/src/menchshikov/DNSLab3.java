import java.io.*;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

class DNS implements Comparable<DNS> {
    private final String dnsIP;
    private final Duration time;

    public DNS(String ip, Duration t) {
        this.dnsIP = ip;
        this.time = t;
    }

    public String getIP() {
        return this.dnsIP;
    }

    public Duration getTime() {
        return this.time;
    }

    @Override
    public int compareTo(DNS o) {
        return Long.compare(this.time.toMillis(), o.time.toMillis());
    }
}

public class DNSLab3 {
    public static boolean fileHistory(String filename) throws FileNotFoundException {
        File file = searchFile(new File(System.getProperty("user.dir")), filename);
        if (file == null)
            return false;
        FileReader fReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fReader);
        String line;
        try {
            line = bReader.readLine();
            while (line != null) {
                System.out.println("\t" + line);
                line = bReader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("File read error");
        }
        return true;
    }

    public static Boolean checkIP(String line) {
        String[] numbers = line.split("\\.");
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[i].length(); j++) {
                if (!Character.isDigit(numbers[i].charAt(j))) {
                    return false;
                }
            }
        }
        for (int i = 0; i < numbers.length; i++) {
            int count = Integer.parseInt(numbers[i]);
            if (count < 0 || count > 255) {
                return false;
            }
        }
        if (numbers.length != 4) {
            return false;
        }
        return true;
    }

    public static ArrayList<String> inputFromFile(String filename) throws FileNotFoundException {
        ArrayList<String> addressArrayFromFile = new ArrayList<String>();
        File file = searchFile(new File(System.getProperty("user.dir")), filename);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferReader = new BufferedReader(fileReader);
        try {
            String line = bufferReader.readLine();
            while (line != null) {
                if (checkIP(line)) {
                    if (!addressArrayFromFile.contains(line)) {
                        addressArrayFromFile.add(line);
                    }
                }
                line = bufferReader.readLine();
            }
            bufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return addressArrayFromFile;
    }

    public static File searchFile(File directory, String filename) {
        File result = null;
        File[] directoryList = directory.listFiles();

        for (int i = 0; i < directoryList.length; i++) {
            if (directoryList[i].isDirectory()) {
                result = searchFile(directoryList[i], filename);
                if (result != null)
                    break;
            } else if (directoryList[i].getName().startsWith(filename)) {
                return directoryList[i];
            }
        }
        return result;
    }

    public static Duration SpeedTest(String host) {
        Instant startTime = Instant.now();
        try {
            InetAddress address = InetAddress.getByName(host);
            if (address.isReachable(1000)) {
                return Duration.between(startTime, Instant.now());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error speed test");
        }
        return Duration.ofDays(1);
    }

    public static void printInFile(ArrayList<DNS> DNS) throws IOException {
        String filename = new SimpleDateFormat("dd-MM-yy_HH-mm").format(Calendar.getInstance().getTime());
        filename += ".txt";
        if (!DNS.isEmpty()) {
            File fileOut = new File(filename);
            FileWriter fw = new FileWriter(fileOut);
            for (DNS out : DNS) {
                fw.write(out.getIP() + ": " + out.getTime().toMillis() + "ms" + "\n");
            }
            fw.close();
        }
    }

    public static void arrayInputOutput(ArrayList<String> addressArray) throws IOException {
        ArrayList<DNS> DNSList = new ArrayList<>();
        for (String address : addressArray) {
            Duration time = Duration.ZERO;
            if (address != "") {
                for (int i = 0; i < 4; i++) {
                    time = time.plus(SpeedTest(address));
                }
                time = time.dividedBy(4);
                DNSList.add(new DNS(address, time));
            }
        }

        Collections.sort(DNSList, Collections.reverseOrder());

        System.out.println("Output:");
        for (DNS out : DNSList) {
            System.out.println("\t" + out.getIP() + ": " + out.getTime().toMillis() + "ms");
        }

        printInFile(DNSList);
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            ArrayList<String> addressArray = new ArrayList<>();
            String multiline = "1.Console input\n" +
                    "2.File input\n" +
                    "3.Search file\n" +
                    "4.Exit";
            System.out.println(multiline);
            int firstInput = sc.nextInt();
            if (firstInput == 1) {
                System.out.println("Input count of addresses");
                int inCount = sc.nextInt();
                System.out.println("Input DNS address");
                for (int i = 0; i < inCount; i++) {
                    addressArray.add(sc.next());
                }
            } else if (firstInput == 2) {
                System.out.println("Name of file: ");
                String filename = sc.next();
                addressArray = inputFromFile(filename);
            } else if (firstInput == 3) {
                System.out.println("Input file with format: 'dd-MM-yy_HH-mm.txt'");
                String filename = sc.next();
                if (!fileHistory(filename)) {
                    System.out.println("Input error");
                    continue;
                }
            } else if (firstInput == 4) {
                break;
            }
            arrayInputOutput(addressArray);
        }
        sc.close();
    }
}