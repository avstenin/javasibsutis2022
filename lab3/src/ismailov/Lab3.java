import java.net.*;
import java.text.*;
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

public class Lab3 {

    private static void printToFile(ArrayList<DNS> DNSPairs) {
        String filename = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
        filename += ".txt";
        if (!DNSPairs.isEmpty()) {
            try (FileWriter file = new FileWriter(filename)) {
                for (DNS val : DNSPairs)
                    if (val.getTime().toMillis() == Duration.ofDays(1).toMillis())
                        file.write("DNS address " + val.getIP() + " unreachable\n");
                    else
                        file.write("Ping to " + val.getIP() + " is " + val.getTime().toMillis() + " ms\n");
            } catch (Exception e) {
            }
        }
    }

    public static boolean isValid(String address){
        String[] nums = address.split("\\.");
        if(nums.length != 4) return false;
        for(int i = 0;i< nums.length;i++)
        {
            for(int j = 0;j<nums[i].length();j++)
            {
                if(!Character.isDigit(nums[i].charAt(j))) return false;
            }
        }
        for(int i = 0; i<nums.length;i++){
            int num = Integer.parseInt(nums[i]);
            if(num < 0 || num > 255) return false;
        }
        return true;
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

    public static ArrayList<String> FileInput(String name) throws FileNotFoundException{
        ArrayList<String> addresses = new ArrayList<String>();
        File file = recursiveFileSearch(new File(System.getProperty("user.dir")),name);
        FileReader fReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fReader);
        int i = 1;
        ArrayList<Integer> invalidIP = new ArrayList<Integer>();
        try{
            String line = bReader.readLine();
            while(line != null){
                if(isValid(line)){
                    if(!addresses.contains(line)){
                        addresses.add(line);
                    }
                }
                else
                {
                    invalidIP.add(i);
                }
                i++;
                line = bReader.readLine();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("File read error");
            return null;
        }
        if(!invalidIP.isEmpty())
        {
            System.out.println("There are invalid IP addresses in lines: ");
            for(Integer val : invalidIP){
                System.out.println(val + " ");
            }
        }
        return addresses;
    }

    public static File recursiveFileSearch(File directory, String filename){
        File result = null;
        File[] directoryList = directory.listFiles();

        for(int i = 0; i < directoryList.length;i++)
        {
            if(directoryList[i].isDirectory()){
                result = recursiveFileSearch(directoryList[i],filename);
                if(result != null) break;
            }
            else if(directoryList[i].getName().startsWith(filename)){
                return directoryList[i];
            }
        }
        return result;
    }

    public static void getFileHistory(String filename) throws FileNotFoundException{
        File file = recursiveFileSearch(new File(System.getProperty("user.dir")),filename);
        FileReader fReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fReader);
        String line;
        try{
            line = bReader.readLine();
            while(line != null){
                System.out.println(line);
                line = bReader.readLine();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("File read error");
        }
    }

    public static void main(String[] args) throws IOException{
        Scanner in = new Scanner(System.in);
        System.out.println("Choose mode:\n1.Manual input\n2.File input\n3.History search");
        int mode = in.nextInt();
        if(mode != 1 && mode != 2 && mode != 3){
            System.out.println("Invalid input. Try again");
            System.exit(1);
        }

        ArrayList<String> addresses = new ArrayList<>();

        if(mode == 1){
            System.out.println("Type in quantity of DNS addresses: ");
            int DNSQuantity = in.nextInt();

            System.out.println("Type in DNS addresses");
            for (int i = 0; i < DNSQuantity; i++) {
                String address = in.next();
                if(isValid(address)) {
                    if (!addresses.contains(address))
                        addresses.add(address);
                }
                else{
                    System.out.println("Invalid ip address. Try again");
                    i--;
                }
            }
        }
        else if(mode == 2){
            System.out.println("Type in filename");
            String filename = in.next();
            addresses = FileInput(filename);
        }
        else if(mode == 3)
        {
            System.out.println("Type in filename in format [yyyy-mm-dd_hh_mm_ss]");
            String filename = in.next();
            getFileHistory(filename);
            System.exit(0);
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