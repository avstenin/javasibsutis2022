package stepanov;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
public class AverageDnsSpeed {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ArrayList<String> DNSnumbers = new ArrayList<>();
        ArrayList<Double> DNSAvgTime = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String temp = input.nextLine();
            DNSnumbers.add(temp);
            DNSAvgTime.add(getAvgTime(temp));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3 - i - 1; j++) {
                if (DNSAvgTime.get(i) < DNSAvgTime.get(j)) {
                    String temp = DNSnumbers.get(i);
                    DNSnumbers.set(i, DNSnumbers.get(j));
                    DNSnumbers.set(j, temp);

                    Double tempDur = DNSAvgTime.get(i);
                    DNSAvgTime.set(i, DNSAvgTime.get(j));
                    DNSAvgTime.set(j, tempDur);
                }

            }
        }

        for (int i = 0;  i < 3; i++) {
            System.out.println(DNSnumbers.get(i) + " : " + DNSAvgTime.get(i));
        }
    }

    public static Double getAvgTime(String DNS) {
        Double avg = 0.0;
        for (int i = 0; i < 5; i++) {
            avg += ping(DNS).toMillis();
        }
        return avg/5;
    }

    public static Duration ping(String host) {
        Instant startTime = Instant.now();
        try {
            InetAddress address = InetAddress.getByName(host);
            if (address.isReachable(1000)) {
                return Duration.between(startTime, Instant.now());
            }
        } catch (IOException e) {
            // Host not available, nothing to do here
        }
        return Duration.ofDays(1);
    }
}
