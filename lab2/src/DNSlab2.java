package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
public class AverageDnsSpeed {

    static class DNS implements Comparable<DNS> {
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

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("Input how much addresses do you want to input:");
        int numberOfAddresses = input.nextInt();
        ArrayList<String> DNSnumbers = new ArrayList<>();
        System.out.println("Input DNS address");
        for (int i = 0; i < numberOfAddresses; i++) {
            DNSnumbers.add(input.next());
        }

        ArrayList<DNS> DNSList = new ArrayList<>();
        for (String address : DNSnumbers) {
            Duration time = Duration.ZERO;
            if (!address.equals("")) {
                DNSList.add(new DNS(address, getAvgTime(address)));
            }
        }

        DNSList.sort(Collections.reverseOrder());

        writeInFile(DNSList);

    }

    public static void writeInFile(ArrayList<DNS> DNS) throws IOException {
        if (!DNS.isEmpty()) {
            File fileOut = new File("out.txt");
            FileWriter fw = new FileWriter(fileOut);
            for (DNS out : DNS) {
                fw.write(out.getIP() + ": " + out.getTime().toMillis() + "ms" + "\n");
            }
            fw.close();
        }
    }

    public static Duration getAvgTime(String DNS) {
        Duration avg = Duration.ZERO;
        for (int i = 0; i < 5; i++) {
            avg = avg.plus(ping(DNS));
        }
        avg = avg.dividedBy(5);
        return avg;
    }

    public static Duration ping(String host) {
        Instant startTime = Instant.now();
        try {
            InetAddress address = InetAddress.getByName(host);
            if (address.isReachable(1000)) {
                return Duration.between(startTime, Instant.now());
            }
        } catch (IOException ignored) {
        }
        return Duration.ofDays(1);
    }
}