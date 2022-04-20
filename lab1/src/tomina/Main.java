package tomina;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception{
        ArrayList<DNS> arrDNS = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        int index = 0;
        for (int x = 0; x < 3; x++){
        System.out.println("Введите адрес сервера DNS" + x +":");
            arrDNS.add(new DNS(input.next()));
            ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c", "ping", arrDNS.get(x).getName());
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
               arrDNS.get(x).setTime(-1);
               continue;
            }
            String time = fullline.substring(index+10, index+14);
            time = time.replaceAll("[a-zA-Zа-яА-Я]*", "");
            time = time.replaceAll(" ", "");
            arrDNS.get(x).setTime(Integer.parseInt(time));
        }
        Collections.sort(arrDNS, DNS.COMPARE_BY_TIME);
        for (int x = 0; x < 3; x++){
            arrDNS.get(x).print();
        }
    }
}