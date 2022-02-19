package lab1.src.Zakharov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class lab1 {
    public static void main(String[] args) throws Exception {

        System.out.println("Введите 3 IP адреса: ");
        ArrayList<String> ip = new ArrayList<>();
        ArrayList<String> averageTime = new ArrayList<>();
        HashMap<String,String> ipAndAverageTime = new HashMap<>();
        Scanner scr = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            ip.add(scr.nextLine());
            averageTime.add(getAverageTime(ip.get(i)));
            ipAndAverageTime.put(ip.get(i), averageTime.get(i));
        }
        Set set = ipAndAverageTime.entrySet();
        Iterator i = set.iterator();

        System.out.println("Среднее время отклика в порядке убывания: ");

        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue() + "ms" );
        }
    }

    public static String getAverageTime(String ip) throws IOException {
        String command = String.format("ping %s | ForEach-Object {if($_ -match 'Average = (\\d+)'){$Matches[1]}}", ip);
        ProcessBuilder builder = new ProcessBuilder(
                "powershell.exe", "/c", command);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new
                InputStreamReader(p.getInputStream()));
        String averageTime;
        averageTime = r.readLine();
        if(averageTime == null) {
            System.out.println("Не правильный IP адрес");
            System.exit(-1);
        }
        return averageTime;
    }
}

