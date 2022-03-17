import java.io.IOException;
import java.net.InetAddress;
import java.time.*;
import java.util.*;

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

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Input");
        int inCount = in.nextInt();

        ArrayList<String> addressArray = new ArrayList<>();
        System.out.println("Input");
        for (int i = 0; i <= inCount; i++) {
            addressArray.add(in.nextLine());
        }

        in.close();
    }
}