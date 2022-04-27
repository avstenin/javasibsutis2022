package bel.company;

import java.util.Scanner;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;

public class SpeedTest{

    static class DNS implements Comparable<DNS>
    {
        String address;
        Duration time;

        public String getIP() {
            return this.address;
        }

        public Duration getTime() {
            return this.time;
        }

        public DNS(String address, Duration time)
        {
            this.address = address;
            this.time = time;
        }
        
        @Override
        public int compareTo(DNS o) {
            return Long.compare(this.time.toMillis(), o.time.toMillis());
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("Value of addresses: ");
        int addressesValue = input.nextInt();
        ArrayList<String> DNSaddresses = new ArrayList<>();
        System.out.println("Input DNS address: ");

        for (int i = 0; i<addressesValue; i++) {
            DNSaddresses.add(input.next());
        }

        ArrayList<DNS> DNSnewList = new ArrayList<>();
        for (String address : DNSaddresses) {
            Duration time = Duration.ZERO;
            if (!address.equals("")) {
                DNSnewList.add(new DNS(address, AverageTime(address)));
            }
        }

        DNSnewList.sort(Collections.reverseOrder());

        writeInFile(DNSnewList);
        }

     public static Duration AverageTime(String address) {
        Duration avg = Duration.ZERO;
        for (int i = 0; i < 5; i++) {
            avg = avg.plus(Ping(address));
        }
        avg = avg.dividedBy(5);
        return avg;
    }

    public static Duration Ping(String baronBelsanku) {
        Instant startTime = Instant.now();
        try {
            InetAddress address = InetAddress.getByName(baronBelsanku);
            if (address.isReachable(1000)) {
                return Duration.between(startTime, Instant.now());
            }
        } catch (IOException ignored) {
        }
        return Duration.ofDays(1);
    }

    public static void writeInFile(ArrayList<DNS> DNS) throws IOException {
        if (!DNS.isEmpty()) {
            File fileOut = new File("out.txt");
            FileWriter fileWriter = new FileWriter(fileOut);
            for (DNS out : DNS) {
                fileWriter.write(out.getIP() + ": " + out.getTime().toMillis() + "ms" + "\n");
            }
            fileWriter.close();
        }
    }
}