
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

//  Google          8.8.8.8         8.8.4.4
//  Quad9	        9.9.9.9	        149.112.112.112
//  OpenDNS Home	208.67.222.222	208.67.220.220
//  Cloudflare	    1.1.1.1         1.0.0.1
//  CleanBrowsing	185.228.168.9	185.228.169.9
//  Alternate DNS	76.76.19.19     76.223.122.150
//  AdGuard DNS	    94.140.14.14	94.140.15.15

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

public class Lab3 {

    ArrayList<Address> addresses = new ArrayList<>();
    String inputFilename = "";
    boolean flagH = false, flagI = false, flagF = false;

    public void getAddressesFromInput() {
        int count = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter count of DNS's");
        count = scanner.nextInt();

        for (int index = 0; index < count; index++) {
            System.out.print("Enter address no" + (index + 1) + " : ");
            String dns = scanner.next();
            double averageTime = .0;
            boolean exceptionThrown = false;
            int attempts = 5;
            for (int attempt = 0; attempt < attempts; attempt++) {
                Socket socket = new Socket();
                try {
                    int port = 53;
                    double currentTime = System.currentTimeMillis();
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
                System.out.print("DNS no" + addresses.get(i)._index + ": ");
                System.out.println(addresses.get(i).toString());
            }
        }
    }

    public void getAddressesFromFile()
    {
        ArrayList<String> list = new ArrayList<>();
        try (FileReader fileReader = new FileReader(inputFilename))
        {
            Scanner input = new Scanner(fileReader);
            while (input.hasNext())
                list.add(input.next());

        } catch (IOException ioException)
        {
            System.out.println(ioException.getMessage());
            ioException.printStackTrace();
        }

        int count = list.size();
        for (int index = 0; index < count; index++) {
            double averageTime = .0;
            boolean exceptionThrown = false;
            int attempts = 5;
            for (int attempt = 0; attempt < attempts; attempt++) {
                Socket socket = new Socket();
                try {
                    int port = 53;
                    double currentTime = System.currentTimeMillis();
                    socket.connect(new InetSocketAddress(list.get(index), port), 5000);
                    averageTime += (System.currentTimeMillis() - currentTime);
                } catch (IOException e) {
                    exceptionThrown = true;
                }
            }
            if (exceptionThrown) {
                System.out.println("error: Can't reach address " + list.get(index));
                continue;
            }
            averageTime /= attempts;
            addresses.add(new Address(list.get(index), averageTime, index + 1));
        }

        if (addresses.isEmpty()) System.out.println("Can't reach any dns");
        else {
            Collections.sort(addresses);
            for (int i = 0; i < addresses.size(); i++) {
                System.out.print("DNS no" + addresses.get(i)._index + ": ");
                System.out.println(addresses.get(i).toString());
            }
        }
    }

    Set<String> historyFilenames = new TreeSet<>();
    File currentDirectory = new File(".");
    public void getHistory()
    {
        boolean found = false;
        String[] filenames = currentDirectory.list();
        for (String filename : filenames)
            if (!historyFilenames.contains(filename) && filename.contains(".log"))
            {
                found = true;
                historyFilenames.add(filename);
                break;
            }
        if (!found) return;
        getHistory();
    }

    public void printHistory()
    {
        System.out.println("\n --- HISTORY ---");
        for (String filename : historyFilenames)
            printFile(filename);
        System.out.println("\n - END HISTORY -\n");
    }

    public void printFile(String filename)
    {
        File file = new File(filename);
        try (FileReader fileReader = new FileReader(file))
        {
            System.out.println("\n" + filename + ":");
            Scanner input = new Scanner(fileReader);
            while (input.hasNext())
                System.out.println(input.nextLine());
        } catch (IOException ioException)
        {
            System.out.println(ioException.getMessage());
            ioException.printStackTrace();
        }
    }

    public void serializeAddresses() {
        if (addresses.isEmpty()) return;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        String filename = dateTimeFormatter.format(LocalDateTime.now()) + ".log";
        try {
            FileWriter out = new FileWriter(filename);
            System.out.println("File " + filename + " created");
            for (int i = 0; i < addresses.size(); i++) {
                out.write(addresses.get(i).toString() + "\r\n");
                out.flush();
            }
        } catch (IOException e) {
            System.out.println("Can't open file " + filename);
        }
    }

    public void run(String[] args) {
        System.out.println("");
        try {
            for (String param : args)
                if (param.equals("-h"))
                    flagH = true;
                else if (param.equals("-i"))
                    flagI = true;
                else if (Files.exists(Path.of(param)))
                {
                    flagF = true;
                    inputFilename = param;
                }
                else throw new Exception("usage");
        } catch(Exception exception)
        {
            if (exception.getMessage().equals("usage"))
            {
                System.out.println("usage: -h -i filename");
                System.out.println("(all params are optional)");
            }
            else
                exception.printStackTrace();
            return;
        }

        if (flagI)
            getAddressesFromInput();
        if (flagF)
            getAddressesFromFile();
        if (flagH)
        {
            getHistory();
            printHistory();
        }
        serializeAddresses();
    }

    public static void Lab3(String[] args) {
        new Lab3().run(args);
    }
}