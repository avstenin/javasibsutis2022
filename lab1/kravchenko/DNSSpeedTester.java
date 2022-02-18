package kravchenko;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

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
    final private static byte dnsCount = 3;
    final private static byte dnsPort = 53;
    final private static short timeout = 5000;

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

    public static void main(String[] args) {
        new DNSSpeedTester().run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<DNSTime> dnsList = new ArrayList<>();
        for (byte index = 1; index <= dnsCount; ++index) {
            String dnsIndex = "DNS" + index;
            System.out.println("Enter " + dnsIndex + " address:");
            System.out.print(">");
            String ip = scanner.next();
            double connectionTime = getAverageConnectionTime(ip);
            if (connectionTime != -1)
                dnsList.add(new DNSTime(ip, dnsIndex, connectionTime));
        }
        System.out.println("Answer:");
        if (dnsList.isEmpty())
            System.out.println("All is unreachable!");
        else {
            Collections.sort(dnsList);
            for (DNSTime dns : dnsList)
                System.out.println(dns.toString());
        }
    }
}
