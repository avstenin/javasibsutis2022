package menchshikov;

import java.util.*;
import java.io.*;
import java.net.*;
import java.time.*;

public class DNS {

    public static Duration SpeedTest(String host) {
        Instant startTime = Instant.now();
        try {
            InetAddress address = InetAddress.getByName(host);
            if (address.isReachable(1000)) {
                return Duration.between(startTime, Instant.now());
            }
        } catch (IOException e) {
            //
        }
        return Duration.ofDays(1);
    }

    public static HashMap<String, Duration> sortByValue(HashMap<String, Duration> hm) {
        List<Map.Entry<String, Duration>> list = new LinkedList<Map.Entry<String, Duration>>(hm.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Duration>>() {
            public int compare(Map.Entry<String, Duration> o1,
                    Map.Entry<String, Duration> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        HashMap<String, Duration> temp = new LinkedHashMap<String, Duration>();
        for (Map.Entry<String, Duration> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s;

        HashMap<String, Duration> dnsAddress = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            System.out.println("Input #" + (i + 1) + " DNS address:");
            s = sc.nextLine();
            Duration temp = Duration.ZERO;
            for (int j = 0; j < 4; j++) {
                temp = temp.plus(SpeedTest(s));
            }
            dnsAddress.put(s, temp.dividedBy(4));
        }

        Map<String, Duration> sortDnsAdress = sortByValue(dnsAddress);

        System.out.println("Output:");
        for (Map.Entry<String, Duration> m : sortDnsAdress.entrySet()) {
            System.out.println("\t" + m.getKey() + ": " + m.getValue().toMillis() + "ms");
        }

        sc.close();
    }
}