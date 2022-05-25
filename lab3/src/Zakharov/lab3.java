package lab3.src.Zakharov;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Date;
import java.util.Map;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;


public class lab3 {
    public static void main(String[] args) throws Exception {
        int ipCount;
        byte choiceUser;
        ArrayList<String> ip = new ArrayList<>();
        ArrayList<String> averageTime = new ArrayList<>();
        HashMap<String,Integer> ipAndAverageTime = new HashMap<>();
        Scanner scr = new Scanner(System.in);
        System.out.println("1. Ввести ip вручную\n2. Прочитать с файла");
        choiceUser = scr.nextByte();
        scr.nextLine();
        if(choiceUser == 1) {
            System.out.println("Введите желаемое количество ip адресов: ");
            ipCount = scr.nextInt();
            System.out.println("Введите " + ipCount + " ip адресов");
            for (int i = 0; i < ipCount; i++) {
                ip.add(scr.next());
                averageTime.add(getAverageTime(ip.get(i)));
                ipAndAverageTime.put(ip.get(i), (int) Double.parseDouble(averageTime.get(i)));
            }
            sortAverageTime(ipAndAverageTime);
            writeToFile(ipAndAverageTime);
            //printResult();
        }
        else if (choiceUser == 2) {
            String path;
            String filename;
            System.out.println("Введите путь к файлу");
            path = scr.nextLine();
            System.out.println("Введите имя файла");
            filename = scr.nextLine();
            ip = readFromFile(path, filename);
            try {
                for (int i = 0; i < ip.size(); i++) {
                    averageTime.add(getAverageTime(ip.get(i)));
                    ipAndAverageTime.put(ip.get(i), (int) Double.parseDouble(averageTime.get(i)));
                }
            }
            catch (NullPointerException e) {
                System.out.println(e.getMessage() + ". Ошибка ввода данных или файл пуст");
                }
            }
            sortAverageTime(ipAndAverageTime);
            writeToFile(ipAndAverageTime);
            //printResult();
    }

    public static String getAverageTime(String ip) throws IOException {
        String os = checkOperatingSystems();
        ProcessBuilder builder;
        if (os.equalsIgnoreCase("linux")) {
            String command = String.format("ping -c 2 %s | tail -1| awk '{print $4}' | cut -d '/' -f 2", ip);
            builder = new ProcessBuilder(
                    "sh", "-c", command);
        }
        else {
            String command = String.format("ping %s | ForEach-Object {if($_ -match 'Average|Среднее = (\\d+)'){$Matches[1]}}", ip);
            builder = new ProcessBuilder(
                    "powershell.exe", "/c", command);
        }
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new
                InputStreamReader(p.getInputStream()));
        String averageTime;
        averageTime = r.readLine();
        p.destroy();
        if (averageTime == null) {
            System.out.println("Не правильный IP адрес");
            System.exit(-1);
        }
        return averageTime;
    }

    public static String checkOperatingSystems() {
        String nameOS;
        if (System.getProperty("os.name").toLowerCase().contains("windows"))
            nameOS = "windows";
        else
            nameOS = "linux";
        return nameOS;
    }

    public static void sortAverageTime(HashMap<String, Integer> noSortedMap) {
        noSortedMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed());
    }

    public static void writeToFile(HashMap<String, Integer> averageTime) {
            try (FileWriter fw = new FileWriter(("newfile.txt"), true)) {
                if (!averageTime.isEmpty()) {
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
                }
                else {
                    fw.write("Ошибка записи в файл, не правильно указаны данные");
                }
            } catch (Exception e) {
                System.out.println("Exception occur emsg= " + e.getMessage());
            }
    }

    public static void findFile(String path, String filename) throws FileNotFoundException {
        File f = new File(path + "\\" + filename);
        if (f.exists()) {
            System.out.println("Файл найден");
        }
        else {
            throw new FileNotFoundException();
        }
    }

    public static ArrayList<String> readFromFile(String path, String filename) {
        try {
            findFile(path,filename);
            ArrayList<String> ip = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(path + "\\" + filename))) {
                String line;
                ip.add(br.readLine());
                while ((line = br.readLine()) != null) {
                    ip.add(line);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(-1);
            }
            return ip;
        }
        catch (FileNotFoundException ex) {
            System.out.println("Файл не найден, проверьте правильность пути и названия файла");
            return null;
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

