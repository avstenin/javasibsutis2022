import java.net.*;
import java.time.*;
import java.util.*;
import java.io.*;

class DNS implements Comparable<DNS>{
    private final String dnsIP;
    private final Duration time;

    public DNS(String ip, Duration t)
    {
        dnsIP = ip;
        time = t;
    }

    public Duration getTime()
    {
        return this.time;
    }

    public String getIP()
    {
        return this.dnsIP;
    }

    @Override
    public int compareTo(DNS o)
    {
        return Long.compare(this.time.toMillis(),o.time.toMillis());
    }
}

public class Lab2 {

    private static void printToFile(ArrayList<DNS> DNSPairs) {
        if (!DNSPairs.isEmpty()) {
            try (FileWriter file = new FileWriter("Result.txt")) {
                for (DNS val : DNSPairs)
                    if (val.getTime().toMillis() == Duration.ofDays(1).toMillis())
                        file.write("DNS address " + val.getIP() + " unreachable\n");
                    else
                        file.write("Ping to " + val.getIP() + " is " + val.getTime().toMillis() + " ms\n");
            } catch (Exception e) {
            }
        }
    }


    public static Duration PingToAddress(String address) {
        Instant startTime = Instant.now();
        int timeout = 100;
        try {
            InetAddress ping = InetAddress.getByName(address);
            if (ping.isReachable(timeout))
                return Duration.between(startTime, Instant.now());
        } catch (IOException exception) {
        }
        return Duration.ofDays(1);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите количество DNS адресов: ");
        int DNSQuantity = in.nextInt();

        ArrayList<String> addresses = new ArrayList<>();
        System.out.println("Введите DNS адреса");
        for (int i = 0; i <= DNSQuantity; i++) {
            String address = in.nextLine();
            if (!addresses.contains(address))
                addresses.add(address);
        }
        in.close();

        ArrayList<DNS> DNSPairs = new ArrayList<DNS>();
        int RequestQuantity = 5;
        for (String val : addresses) {
            if (val != "") {
                Duration time = Duration.ZERO;
                for (int j = 0; j < RequestQuantity; j++)
                    time = time.plus(PingToAddress(val));
                DNSPairs.add(new DNS(val, time.dividedBy(RequestQuantity)));
            }
        }

        Collections.sort(DNSPairs, Collections.reverseOrder());
        printToFile(DNSPairs);
        for (DNS val : DNSPairs) {
            if (val.getTime().toMillis() == Duration.ofDays(1).toMillis()) {
                System.out.println("\nDNS address " + val.getIP() + " unreachable");
            }
            else {
                System.out.println("\nPing to " + val.getIP() + " is " + val.getTime().toMillis() + " ms");
            }
        }

    }
}