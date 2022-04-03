package lab1.src.Zakharov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class lab1 {
    public static void main(String[] args) throws Exception {

        System.out.println("Введите 3 IP адреса: ");
        ArrayList<String> ip = new ArrayList<>();
        ArrayList<String> averageTime = new ArrayList<>();
        HashMap<String,Integer> ipAndAverageTime = new HashMap<>();
        Scanner scr = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            ip.add(scr.nextLine());
            averageTime.add(getAverageTime(ip.get(i)));
            ipAndAverageTime.put(ip.get(i), Integer.parseInt(averageTime.get(i)));
        }
        sortAverageTime(ipAndAverageTime);
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

    public static void sortAverageTime(HashMap<String, Integer> noSortedMap) {
        noSortedMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(x -> System.out.println(x + "ms"));
    }
}

