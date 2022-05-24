package tomina;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(System.in);
        System.out.println("0 - ввод с консоли");
        System.out.println("1 - ввод с файла");
        System.out.println("2 - вывод всей истории запросов");
        int sw = input.nextInt();
        switch (sw){
            case(0):
                console_input();
                break;
            case(1):
                input_from_file();
                break;
            case(2):
                File dir = new File("output");
                //if(dir.) System.out.println("0 - ввод с консоли");
                searchFiles(dir);
                break;
            default:
                break;
        }
    }

    private static void input_from_file() throws IOException {
        ArrayList<DNS> arrDNS = new ArrayList<>();
        int index = 0;
        System.out.println("Введите имя файла в директории запуска");
        Scanner input = new Scanner(System.in);
        String filename = input.next();
        File doc = new File(filename);
        Scanner obj = new Scanner(doc);
        int i = 0;
        while (obj.hasNextLine()){
            arrDNS.add(new DNS(obj.next()));
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
            i++;
        }
        Collections.sort(arrDNS, DNS.COMPARE_BY_TIME);
        DNS.write(arrDNS);
        System.out.println("Результат работы программы записан в директории /output");
    }

    private static void console_input() throws IOException {
        Scanner input = new Scanner(System.in);
        ArrayList<DNS> arrDNS = new ArrayList<>();
        System.out.println("Введите количество DNS-серверов");
        int n = 0;
        int index = 0;
        n = input.nextInt();
        for (int i = 0; i < n; i++){
            System.out.println("Введите адрес сервера DNS" + i +":");
            arrDNS.add(new DNS(input.next()));
            try{
                InetAddress inetAddress = java.net.InetAddress.getByName(arrDNS.get(i).getIp_addr());
                arrDNS.get(i).setName(inetAddress.getHostName());
            }catch (IOException e){
                System.out.println(e.getMessage());
            }

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

    private static void searchFiles(File dir) throws IOException {
        Path path = Paths.get("output");
        if (Files.exists(path)) {
            File[] files = dir.listFiles();
            for (File item : files) {
                if (item.isFile()) {
                    try (BufferedReader in = new BufferedReader(new FileReader(item.getParent() + "\\" + item.getName()))) {
                        String line;
                        while ((line = in.readLine()) != null) {
                            System.out.println(line);
                        }
                    }
                } else if (item.isDirectory()) {
                    System.out.println(item.getName());
                    searchFiles(item);
                }
            }
        } else {
            System.out.println("No history");
        }
    }
}