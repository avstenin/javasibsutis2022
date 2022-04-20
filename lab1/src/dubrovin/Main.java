import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class Address implements Comparable<Address> {
    public Double _connectionTime;
    public String _address;
    public int _index;

    public Address(String address, double connectionTime, int index) {
        _address = address;
        _connectionTime = connectionTime;
        _index = index;
    }

    @Override
    public String toString() {
        return "ip " + _address + ", connection time : " + _connectionTime + "ms (average)";
    }

    @Override
    public int compareTo(Address o) {
        return this._connectionTime.compareTo(o._connectionTime);
    }
}

//  Google          8.8.8.8         8.8.4.4
//  Quad9	        9.9.9.9	        149.112.112.112
//  OpenDNS Home	208.67.222.222	208.67.220.220
//  Cloudflare	    1.1.1.1         1.0.0.1
//  CleanBrowsing	185.228.168.9	185.228.169.9
//  Alternate DNS	76.76.19.19     76.223.122.150
//  AdGuard DNS	    94.140.14.14	94.140.15.15

public class Main implements Runnable {

    public static void main(String[] args) {
        new Thread(new Main()).start();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter count of DNS's");
        int count = scanner.nextInt();
        ArrayList<Address> addresses = new ArrayList<>();

        for (int index = 0; index < count; index++) {
            System.out.print("Enter address no" + (index + 1) + " : ");
            String dns = scanner.next();
            double averageTime = .0;
            boolean exceptionThrown = false;
            int attempts = 5;
            for (int attempt = 0; attempt < attempts; attempt++) {
                Socket socket = new Socket();
                try {
                    double currentTime = System.currentTimeMillis();
                    int port = 53;
                    socket.connect(new InetSocketAddress(dns, port), 5000);
                    averageTime += (System.currentTimeMillis() - currentTime);
                } catch (IOException e) {
                    exceptionThrown = true;
                }
            }
            if (exceptionThrown) {
                System.out.println("error: Can't reach address " + dns);
                continue;
            }
            averageTime /= attempts;
            addresses.add(new Address(dns, averageTime, index + 1));
        }

        if (addresses.isEmpty()) System.out.println("Can't reach any dns");
        else {
            Collections.sort(addresses);
            for (int i = 0; i < addresses.size(); i++) {
                System.out.println("DNS no" + addresses.get(i)._index);
                System.out.println(addresses.get(i).toString());
            }
        }
    }
}
