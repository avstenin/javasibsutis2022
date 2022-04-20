package lab2.fadin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class lab2 {

    private static boolean checkString(String line) {
        if (line == null) {
            return false;
        }
        if (!line.contains("/") && !line.contains("PING")) {
            System.out.println("DNS unreachable");
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        
        Scanner sc = new Scanner(System.in);
        int numOfDNS = 0;
        System.out.print("Input number of DNS's\n>");
        numOfDNS = sc.nextInt();
        sc.nextLine();
        // DnsInfo[] dnsInfo = new DnsInfo[numOfDNS];
        ArrayList<DnsInfo> dnsInfo = new ArrayList<DnsInfo>();
        HashSet<String> dns = new HashSet<>();
        for (int i = 0; i < numOfDNS; i++) {
            System.out.print("Input DNS " + i + ":\n>");
            String sin = sc.nextLine();
            dns.add(sin);
        }
        int i = -1;
        for(var dnsName: dns) {
            ++i;
            DnsInfo temp = new DnsInfo();
            temp.setDns(dnsName);
            dnsInfo.add(temp);
            ProcessBuilder builder = new ProcessBuilder("/bin/bash",
                    "-c",
                    "ping -c 1 " + dnsName + " | grep 'avg\\|PING'");
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (!checkString(line)) {
                    break;
                }
                if (line.contains("PING")) {
                    String str = line.substring(line.indexOf('(') + 1, line.indexOf(')'));
                    dnsInfo.get(i).setIp(str);
                }
                if (line.contains("avg")) {
                    String[] str = line.split("/");
                    dnsInfo.get(i).setAvgTime(Double.parseDouble(str[4]));
                }
            }
        }
        sc.close();
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
        return;
    }

    private static void writeToFile(Map<String, String> dnsHashMap) {
        try (FileWriter writer = new FileWriter("dnses.txt", true)) {
            for (Map.Entry<String, String> i : dnsHashMap.entrySet())
                writer.append(i.getValue() + '\n');
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void printStat() {
        Map<String, String> dnsHashMap = new HashMap<String, String>();
        try {
            File file = new File("dnses.txt");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                String str[] = line.split("\t");
                dnsHashMap.put(str[1], line);
                line = reader.readLine();
            }
            reader.close();
            System.out.println("Statistic:");
            for (Map.Entry<String, String> i : dnsHashMap.entrySet())
                System.out.println("> " + i.getValue());
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
