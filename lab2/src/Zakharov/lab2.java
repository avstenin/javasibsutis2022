package lab2.src.Zakharov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Date;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

public class lab2 {
    public static void main(String[] args) throws Exception {
        int ipCount;
        ArrayList<String> ip = new ArrayList<>();
        ArrayList<String> averageTime = new ArrayList<>();
        HashMap<String,Integer> ipAndAverageTime = new HashMap<>();
        Scanner scr = new Scanner(System.in);
        System.out.println("Введите желаемое количество ip адрессов: ");
        ipCount = scr.nextInt();
        System.out.println("Введите " + ipCount + " ip адресов");
        for (int i = 0; i < ipCount; i++) {
            ip.add(scr.next());
            averageTime.add(getAverageTime(ip.get(i)));
            ipAndAverageTime.put(ip.get(i), Integer.parseInt(averageTime.get(i)));
        }
        //sortAverageTime(ipAndAverageTime);
        writeToFile(ipAndAverageTime);
        printResult();
    }

    public static String getAverageTime(String ip) throws IOException {
        String command = String.format("ping %s | ForEach-Object {if($_ -match '(?:Среднее|Average) = (\\d+)'){$Matches[1]}}", ip);
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

    public static void writeToFile(HashMap<String, Integer> averageTime) {
        try {
            File file = new File("newfile.txt");
            if (!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file, true);
            averageTime.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(x -> {
                        try {
                            fw.write(x + "ms\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            fw.write("---" + new Date() + "\n");
            fw.close();
        }
        catch (Exception e)
        {
            System.out.println("Exception occur emsg= "+ e.getMessage());
        }
    }

    public static void printResult() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("newfile.txt"));
            String line;
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

}

