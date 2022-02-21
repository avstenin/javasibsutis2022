import java.net.*;
import java.time.*;
import java.util.*;
import java.io.*;

public class DNSTester {

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

    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);

        HashMap<String, Duration> Addresses = new HashMap<>();
        int RequestQuantity = 5;
        for(int i=1; i<=3; i++) {
            System.out.println("DNS address #" + i);
            Duration time = Duration.ZERO;
            String address = in.nextLine();
            for (int j = 0; j < RequestQuantity; j++)
                time = time.plus(PingToAddress(address));
            Addresses.put(address, time.dividedBy(RequestQuantity));
        }
        LinkedHashMap<String,Duration> sortedAddresses = new LinkedHashMap<>();
        Addresses.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(val -> sortedAddresses.put(val.getKey(), val.getValue()));

        for(Map.Entry<String,Duration> val : sortedAddresses.entrySet())
           if(val.getValue().toMillis() == Duration.ofDays(1).toMillis())
               System.out.println("\n\nDNS address " + val.getKey() + " unreachable");
            else
               System.out.println("\nPing to " + val.getKey() + " is " + val.getValue().toMillis() + " ms");
        in.close();
    }
}
