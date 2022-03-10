import java.io.*;
import java.util.*;

public class Main {
    private static final int numOfResultStr = 14;
    private static int countDNSs;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите количество DNS адресов: ");
        countDNSs = in.nextInt();
        in.nextLine();
        String[][] DNSinfo = new String[3][countDNSs];

        for (int i = 0; i < countDNSs; i++) {
            System.out.print("Введите адрес сервера DNS" + (i + 1) + ": ");
            String DNS = in.nextLine();
            BufferedReader r = getBufferOfProcess(DNS);
            String averageTimeLine = getResultLine(r);
            if (averageTimeLine == null) {
                System.out.println("Не получилось обратиться к DNS: " + DNS);
                DNSinfo[2][i] = "0";
            } else {
                DNSinfo[2][i] = String.valueOf(parseLine(averageTimeLine));
            }
            DNSinfo[0][i] = DNS;
        }

        sort(DNSinfo);

        System.out.println();

        String fileName = writeToCSV(DNSinfo);
        readFromCSV(fileName);
    }

    private static void readFromCSV(String fileName) {
        try {
            Reader reader = new FileReader(fileName);
            BufferedReader br = new BufferedReader(reader);

            if (br.readLine() == null)
                return;

            String line;

            while((line = br.readLine()) != null) {
                String[] values;
                values = line.split(",");
                System.out.println("Avg time of DNS: " + values[0] + " is " + values[1] + ", sec.");
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String writeToCSV(String[][] DNSInfo) {
        try {
            String fileName = File.createTempFile("data_", ".csv" , new File(System.getProperty("user.dir"))).toString();
            FileWriter file = new FileWriter(fileName);

            StringBuilder sb = new StringBuilder();

            sb.append("DNS address");
            sb.append(',');
            sb.append("Avg time");
            sb.append('\n');

            for (int i = 0; i < countDNSs; i++) {
                sb.append(DNSInfo[0][i]);
                sb.append(',');
                sb.append(DNSInfo[2][i]);
                sb.append('\n');
            }

            file.write(sb.toString());
            file.close();

            return fileName;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static BufferedReader getBufferOfProcess(String DNS) {
        try {
            ProcessBuilder builder = new ProcessBuilder("ping", "-c", "10", DNS);
            builder.redirectErrorStream(true);
            Process p = builder.start();
            return new BufferedReader(new InputStreamReader(p.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double parseLine(String line) {
        String[] wordsArray = line.split(" ");
        String[] timesArray = wordsArray[3].split("/");
        return Double.parseDouble(timesArray[1]);
    }

    public static String getResultLine(BufferedReader r) {
        try {
            String line;
            for (int i = 0; i <= numOfResultStr; i++) {
                line = r.readLine();
                System.out.println(line);
                if (i == numOfResultStr) {
                    return line;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sort(String[][] DNSInfo) {
        for (int i = 0; i <= countDNSs - 1; i++) {
            for (int j = 0; j <= i; j++) {
                if (Double.parseDouble(DNSInfo[2][i]) > Double.parseDouble(DNSInfo[2][j])) {
                    String tmp = DNSInfo[2][i];
                    DNSInfo[2][i] = DNSInfo[2][j];
                    DNSInfo[2][j] = tmp;
                    tmp = DNSInfo[0][i];
                    DNSInfo[0][i] = DNSInfo[0][j];
                    DNSInfo[0][j] = tmp;
                }
            }
        }
    }

}
