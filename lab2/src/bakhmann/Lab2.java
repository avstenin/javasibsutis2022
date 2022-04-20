import java.util.Scanner;
import java.util.regex.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

class DNSPair implements java.lang.Comparable<DNSPair> {
    private int Res;
    private String Name;
    private String Ip;

    public DNSPair(String name, int res, String ip) {
        Res = res;
        Ip = ip;
        Name = name;
    }

    public String getIp() {
        return this.Ip;
    }

    public int getRes() {
        return this.Res;
    }

    public String getName() {
        return this.Name;
    }

    @Override
    public int compareTo(DNSPair o) {
        return Integer.compare(this.getRes(), o.getRes());
    }

}

public class Lab2 {
    public static void main(String[] args) throws IOException {

        System.out.println("Введите количество адресов:");
        ArrayList<String> dns = new ArrayList<String>();
        Scanner n = new Scanner(System.in);
        int num = n.nextInt();
        System.out.println("Введите " + num + " адресов:");
        for (int i = -1; i < num; ++i) {
            String temp = n.nextLine();
            if (!dns.contains(temp))
                dns.add(temp);
        }
        n.close();
        ArrayList<DNSPair> dnspair = new ArrayList<DNSPair>();

        ExecutePing(dnspair, dns);
        
        PrintSorted(dnspair);
        AddToFile(dnspair);
    }

    public static void ExecutePing(ArrayList<DNSPair> dnspair, ArrayList<String> dns) throws IOException {
        for (int i = 0; i < dns.size(); ++i) {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "ping " + dns.get(i));
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
                AddTime(dnspair, strings, dns, i);
            }
        }
    }

    public static void AddTime(ArrayList<DNSPair> dnspair, ArrayList<String> strings, ArrayList<String> dns, int i)
            throws UnknownHostException {
        Pattern pa = Pattern.compile(".* = (\\d*)");
        Matcher m = pa.matcher(strings.get(strings.size() - 1));
        if (m.find()) {
            String s = m.group(1);
            InetAddress giriAddress = java.net.InetAddress.getByName(dns.get(i));
            String address = giriAddress.getHostAddress();
            dnspair.add(new DNSPair(dns.get(i), Integer.parseInt(s), address));
        }
    }

    public static void PrintSorted(ArrayList<DNSPair> dnspair) {
        Collections.sort(dnspair, Collections.reverseOrder());
        for (DNSPair a : dnspair) {
            System.out.println(
                    "IP: " + a.getIp() + " DNS: " + a.getName() + ", average time: " + a.getRes() + "ms");
        }
    }

    public static void AddToFile(ArrayList<DNSPair> dnspair) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        BufferedWriter writer = new BufferedWriter(new FileWriter(timeStamp));
        for (DNSPair a : dnspair) {
            writer.write("IP: " + a.getIp() + " DNS: " + a.getName() + ", average time: " + a.getRes() + "ms\n");
        }
        writer.close();
    }
}
