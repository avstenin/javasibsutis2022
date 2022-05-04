package zaripov;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    private static final int numOfResultStr = 14;
    private static final int countDNSs = 3;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String DNS;
        String[] DNSs = new String[countDNSs];
        Double[] avgTime = new Double[countDNSs];

        for (int i = 0; i < countDNSs; i++) {
            System.out.print("Введите адрес сервера DNS" + (i+1) + ": ");
            DNS = in.nextLine();
            BufferedReader r = getBufferOfProcess(DNS);
            String averageTimeLine = getResultLine(r);
            if (averageTimeLine == null) {
                System.out.println("Не получилось обратиться к DNS: " + DNS);
                avgTime[i] = 0D;
            } else {
                avgTime[i] = parseLine(averageTimeLine);
            }
            DNSs[i] = DNS;
        }

        sort(DNSs, avgTime);

        System.out.println();
        for (int i = 0; i < countDNSs; i++)
            System.out.println("AvgTime of DNS:" + DNSs[i] +" is " + avgTime[i]);
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

    public static void sort(String[] DNSs, Double[] avgTime) {
        for (int i = 0; i <= countDNSs - 1; i++) {
            for (int j = 0; j <= i; j++) {
                if (avgTime[i] > avgTime[j]) {
                    Double tmpForTime = avgTime[i];
                    avgTime[i] = avgTime[j];
                    avgTime[j] = tmpForTime;
                    String tmpForDNS = DNSs[i];
                    DNSs[i] = DNSs[j];
                    DNSs[j] = tmpForDNS;
                }
            }
        }
    }

}
