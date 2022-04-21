package kravchenko;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class DNSTime implements Comparable<DNSTime> {
    private final String ip;
    private final String name;
    private final Double connectionTime;

    public DNSTime(String ip, String name, Double connectionTime) {
        this.ip = ip;
        this.name = name;
        this.connectionTime = connectionTime;
    }

    @Override
    public int compareTo(DNSTime o) {
        return this.connectionTime.compareTo(o.connectionTime);
    }

    @Override
    public String toString() {
        return name + " (" + ip + ") " + connectionTime + "ms";
    }
}

public class DNSSpeedTester {
    final private static byte dnsPort = 53;
    final private static short timeout = 5000;
    ArrayList<DNSTime> dnsList = new ArrayList<>();
    Set<String> dnsSet = new HashSet<>();

    private double getAverageConnectionTime(String ip) {
        byte requests = 10;
        double allTime = 0;
        for (byte i = 0; i < requests; ++i) {
            try (Socket socket = new Socket()) {
                double time = System.currentTimeMillis();
                socket.connect(new InetSocketAddress(ip, dnsPort), timeout);
                time = (System.currentTimeMillis() - time);
                allTime += time;
            } catch (IOException e) {
                return -1;
            }
        }
        return allTime / requests;
    }

    private void fillDNSList() {
        Scanner input = new Scanner(System.in);
        long dnsCount;
        System.out.println("Enter the number of DNS servers:");
        do {
            System.out.print(">");
            dnsCount = input.nextLong();
        } while (dnsCount <= 0);
        for (long index = 1; index <= dnsCount; ++index) {
            String dnsIndex = "DNS" + index;
            System.out.println("Enter " + dnsIndex + " address:");
            System.out.print(">");
            String ip = input.next();
            if (dnsSet.contains(ip))
                continue;
            else
                dnsSet.add(ip);
            double connectionTime = getAverageConnectionTime(ip);
            if (connectionTime != -1) dnsList.add(new DNSTime(ip, dnsIndex, connectionTime));
        }
    }

    private void printDNSList() {
        System.out.println("Answer:");
        if (dnsList.isEmpty()) System.out.println("All is unreachable!");
        else {
            Collections.sort(dnsList);
            for (DNSTime dns : dnsList)
                System.out.println(dns.toString());
        }
    }

    private void saveDNSList() {
        if (!dnsList.isEmpty()) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
            String filename = dtf.format(LocalDateTime.now());
            try (FileWriter file = new FileWriter(filename)){
                for (DNSTime dns : dnsList)
                    file.write(dns.toString() + '\n');
            } catch (Exception e){
                System.out.println("An error occurred while trying to save to file.");
            }
        }
    }

    public static void main(String[] args) {
        new DNSSpeedTester().run();
    }

    public void run() {
        fillDNSList();
        printDNSList();
        saveDNSList();
    }
}
