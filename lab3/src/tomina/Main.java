package tomina;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception{
        ArrayList<DNS> arrDNS = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        int index = 0;
        int n = 0;
        System.out.println("Введите количество DNS-серверов");
        n = input.nextInt();
        for (int i = 0; i < n; i++){
            System.out.println("Введите адрес сервера DNS" + i +":");
            arrDNS.add(new DNS(input.next()));
            InetAddress inetAddress = java.net.InetAddress.getByName(arrDNS.get(i).getIp_addr());
            arrDNS.get(i).setName(inetAddress.getHostName());
            ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c", "ping", arrDNS.get(i).getIp_addr());
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new
                    InputStreamReader(p.getInputStream(),"866"));
            String line;
            String fullline = "";
            while (true) {
                line = r.readLine();
                fullline=fullline+line;
                if (line == null) { break; }
            }
            index = fullline.indexOf("Среднее");
            if(index<0){
                System.out.println("DNS не доступен");
                arrDNS.get(i).setTime(-1);
                continue;
            }
            String time = fullline.substring(index+10, index+14);
            time = time.replaceAll("[a-zA-Zа-яА-Я]*", "");
            time = time.replaceAll(" ", "");
            arrDNS.get(i).setTime(Integer.parseInt(time));
        }
        Collections.sort(arrDNS, DNS.COMPARE_BY_TIME);
        DNS.write(arrDNS);
        System.out.println("Результат работы программы записан в директории /output");
    }
}
//77.88.8.7
//1.1.1.1
//8.8.8.8
//87.240.137.158