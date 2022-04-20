package lab1;

import java.util.Scanner;
import java.util.regex.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Lab1 {

    public static void main(String[] args) throws IOException {

        System.out.println("Введите 3 адреса:");
        ArrayList<String> dns = new ArrayList<String>();
        Scanner n = new Scanner(System.in);
        for (int i = 0; i < 3; ++i) {
            String temp = n.nextLine();
            dns.add(temp);
        }
        n.close();
        ArrayList<DNSPair> dnspair = new ArrayList<DNSPair>();

        ExecutePing(dnspair, dns);

        PrintSorted(dnspair);
    }



    
    public static void ExecutePing(ArrayList<DNSPair> dnspair, ArrayList<String> dns) throws IOException {
        for (int i = 0; i < 3; ++i) {
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

    public static void AddTime(ArrayList<DNSPair> dnspair, ArrayList<String> strings, ArrayList<String> dns, int i) {
        Pattern pa = Pattern.compile(".* = (\\d*)");
        Matcher m = pa.matcher(strings.get(strings.size() - 1));
        if (m.find()) {
            String s = m.group(1);
            dnspair.add(new DNSPair(dns.get(i), Integer.parseInt(s)));
        }
    }

    public static void PrintSorted(ArrayList<DNSPair> dnspair) {
        Collections.sort(dnspair, Collections.reverseOrder());
        for (DNSPair a : dnspair) {
            System.out.println("Адрес: " + a.getName() + ", cреднее время отклика: " + a.getRes() + "мс");
        }
    }
}
