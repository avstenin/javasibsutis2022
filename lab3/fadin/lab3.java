package lab3.fadin;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class lab3 {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int numOfDNS = 0, type = 0;
        ArrayList<DnsInfo> dnsInfo = new ArrayList<DnsInfo>();
        HashSet<String> dns = new HashSet<>();
        while (type != 1 && type != 2) {
            System.out.print("1. Manual input\n2. File input\n>");
            type = sc.nextInt();
            sc.nextLine();
            if (type != 1 && type != 2)
                System.out.println("Incorrect input");
        }
        if (type == 1) {
            System.out.print("Input number of DNS's\n>");
            numOfDNS = sc.nextInt();
            sc.nextLine();
            for (int i = 0; i < numOfDNS; i++) {
                System.out.print("Input DNS " + i + ":\n>");
                String sin = sc.nextLine();
                dns.add(sin);
            }
        }
        if (type == 2) {
            System.out.print("Input file name\n>");
            String sin = sc.nextLine();
            File file = new File(sin);
            dns = readDnsFromFile(file);
            if (!file.exists()){
                sc.close();
                throw new FileNotFoundException();
            }
        }
        int i = -1;
        for (var dnsName : dns) {
            ++i;
            DnsInfo temp = new DnsInfo();
            temp.setDns(dnsName);
            dnsInfo.add(temp);
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "ping " + dnsName);
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            ArrayList<String> strings = new ArrayList<String>();
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                strings.add(line);
            }
            if (strings.size() > 4) {
                Pattern pa = Pattern.compile(".* = (\\d*)");
                java.util.regex.Matcher m = pa.matcher(strings.get(strings.size() - 1));
                if (m.find()) {
                    String s = m.group(1);
                    InetAddress giriAddress = java.net.InetAddress.getByName(dnsName);
                    String address = giriAddress.getHostAddress();
                    dnsInfo.get(i).setIp(address);
                    dnsInfo.get(i).setAvgTime(Double.parseDouble(s));
                }
               
            }
        }
        Map<String, String> dnsHashMap = new HashMap<String, String>();
        for (DnsInfo j : dnsInfo) {
            System.out.println(j);
            if (j.getAvgTime() >= 0)
                dnsHashMap.put(j.getDns(), j.toString());
        }
        writeToFile(dnsHashMap);
        if (dns.size() == 0)
            System.out.println("All dns's unreachable");
        printStat();
        sc.close();
        return;
    }

    private static void writeToFile(Map<String, String> dnsHashMap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(timeStamp + ".txt"))) {
            for (Map.Entry<String, String> i : dnsHashMap.entrySet())
                writer.append(i.getValue() + '\n');
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void printStat() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Input date of measuring[All/yyyyMMdd_HHmmss]\n>");
        String sin = "";
        sin = sc.nextLine();
        File folder = new File(System.getProperty("user.dir"));
        if (sin.equals("All")) {
            File[] files = folder.listFiles();
            for (var file : files) {
                if (file.getName().contains(".txt")) {
                    System.out.println(file.getAbsolutePath());
                    readFile(file.getName());
                }
            }
            sc.close();
            return;
        }
        sc.close();
        try {
            readFile(sin + ".txt");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void readFile(String file) throws IOException {
        if (file.length() == 0) {
            System.out.println("File is empty");
            return;
        }
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        System.out.println(file);
        String line = "";
        while (line != null) {
            System.out.println(line);
            line = reader.readLine();
        }
        System.out.println("\n");
        reader.close();
    }

    static HashSet<String> readDnsFromFile(File file) throws IOException {
        HashSet<String> dns = new HashSet<>();
        if (file.length() == 0) {
            System.out.println("File is empty");
            return null;
        }
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String line = "";
        while (line != null) {
            line = reader.readLine();
            if (line != null)
                dns.add(line);
        }
        reader.close();
        return dns;
    }
}

class DnsInfo {
    private String ip;
    private String dns;
    private Double avgTime;

    public String getDns() {
        return dns;
    }

    public Double getAvgTime() {
        return avgTime;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setAvgTime(Double avgTime) {
        this.avgTime = avgTime;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    DnsInfo() {
        ip = "0.0.0.0";
        dns = "";
        avgTime = -1D;
    }

    public void init() {
        ip = "0.0.0.0";
        dns = "";
        avgTime = -1D;
    }

    @Override
    public String toString() {
        if (avgTime == -1) {
            return "DNS is unreachable";
        }
        return "" + ip + "\t" + dns + "\t" + avgTime;
    }
}
