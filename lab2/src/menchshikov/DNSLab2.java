import java.io.*;
import java.net.InetAddress;
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

public class DNSLab2 {

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

    public static void printInFile(ArrayList<DNS> DNS) throws IOException {
        if (!DNS.isEmpty()) {
            File fileOut = new File("out.txt");
            FileWriter fw = new FileWriter(fileOut);
            for (DNS out : DNS) {
                fw.write(out.getIP() + ": " + out.getTime().toMillis() + "ms");
            }
            fw.close();
        }
    }

    public static void arrayInputOutpun(ArrayList<String> addressArray) throws IOException {
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
        System.out.println("Input quantity of DNS address");
        int inCount = sc.nextInt();

        ArrayList<String> addressArray = new ArrayList<>();
        System.out.println("Input DNS address");
        for (int i = 0; i <= inCount; i++) {
            addressArray.add(sc.nextLine());
        }

        arrayInputOutpun(addressArray);
        sc.close();
    }
}