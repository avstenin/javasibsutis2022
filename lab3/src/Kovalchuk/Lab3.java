package Kovalchuk;

import java.io.*;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Scanner;

public class Lab3 {
    public static void main(String[] args) throws IOException {
        ArrayList<Time> data = new ArrayList<Time>();
        ArrayList<String> adresses  = getAdresses();
        if(adresses == null){
            return;
        }
        for(String i: adresses){
            data.add(checkTime(i));
        }
        //data.add(checkTime("8.8.8.8"));
        //data.add(checkTime("8.8.4.4"));
        //data.add(checkTime("77.88.8.7"));
        //Collections.sort(data);
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
        BufferedWriter writer = new BufferedWriter(new FileWriter(timeStamp + ".txt"));
        for(Time i : data) {
            System.out.println("IP: " + i.address + ", average time: " + i.time + "ms\n");
            writer.write("IP: " + i.address + ", average time: " + i.time + "ms\n");
        }
        writer.close();
    }

    private static void getHistory() throws FileNotFoundException{
        System.out.println("введите дату в формате yyyy-mm-dd");
        Scanner scanner = new Scanner(System.in);
        String tmp = scanner.nextLine();
        File file = findFile(new File(System.getProperty("user.dir")), tmp);

        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        try {
            tmp = br.readLine();
            while (tmp != null) {
                System.out.println(tmp);
                tmp = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка чтения из файла");
        }
        finally {
            scanner.close();
        }
    }

    private static ArrayList<String> getAdresses(){
        System.out.println("Укажите файл со списком адерсов или введте R для ручного ввода");
        System.out.println("введите английскую H для поиска истории");
        Scanner scanner = new Scanner(System.in);
        String tmp;
        boolean manual = false;
        try {
            tmp = scanner.nextLine();
            if (tmp.length() == 1){
                if(tmp.charAt(0) == 'R')
                    return getAdressesManual();
                if(tmp.charAt(0) == 'H'){
                    getHistory();
                    return null;
                }
            }
            else{
                return getAdressesFile(tmp);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            scanner.close();
        }
        return null;
    }

    private static ArrayList<String> getAdressesManual(){
        ArrayList<String> adresses  = new ArrayList<String>();
        System.out.println("Введите число адресов:");
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Введите " + num + " адреса:");
        for (int i = 0; i < num; ++i) {
            System.out.print(i + 1 + " адрес: ");
            String tmp = scanner.nextLine();
            System.out.println();
            if (!adresses.contains(tmp) && isValid(tmp))
                adresses.add(tmp);
            else {
                System.out.println("Некорректный адрес");
                i--;
            }
        }
        System.out.println("все адреса введены, пингую...");
        scanner.close();
        return adresses;
    }

    private static ArrayList<String> getAdressesFile (String name) throws FileNotFoundException{
        //пример тестового файла
        //77.88.8.7
        //8.8.8.8
        //8.8.4.4
        //ssssssssssssss

        ArrayList<String> adresses  = new ArrayList<String>();
        File file = findFile(new File(System.getProperty("user.dir")), name);

        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        try {
            String tmp = br.readLine();
            while (tmp != null) {
                if (!adresses.contains(tmp) && isValid(tmp))
                    adresses.add(tmp);
                tmp = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка чтения из файла");
            return null;
        }

        return adresses;
    }

    private static File findFile(File dir, String name) {
        File result = null;
        File[] dirlist  = dir.listFiles();

        for(int i = 0; i < dirlist.length; i++) {
            if(dirlist[i].isDirectory()) {
                result = findFile(dirlist[i], name);
                if (result != null) break;
            } else if(dirlist[i].getName().startsWith(name)) {
                return dirlist[i];
            }
        }
        return result;
    }

    private static boolean isValid(String address){
        int num = 0;
        for(int i = 0; i < address.length(); ++i){
            if(address.charAt(i) == '.'){
                if(num == 0) return false;
                num = 0;
            }
            else if(Character.isDigit(address.charAt(i))){
                num++;
            }
            else
                return false;
            if(num > 3)
                return false;
        }
        return true;
    }

    private static Time checkTime(String address) throws IOException {
//        ArrayList<String> strings = new ArrayList<String>();
//        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "ping " + address);
//        builder.redirectErrorStream(true);
//        Process p = builder.start();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//        String line;
//        while (true) {
//            line = reader.readLine();
//            if (line == null) {
//                break;
//            }
//            strings.add(line);
//        }
//        p.destroy();
//        Time time = new Time();
//        time.address = address;
//        int t = 0;
//        int pos = 0;
//        for (String i : strings) {
//            pos = i.indexOf("Average");
//            if(pos < 0) continue;
//            pos += 10;
//            while(Character.isDigit(i.charAt(pos))){
//                t = t*10 + Character.getNumericValue(i.charAt(pos));
//                pos++;
//            }
//            break;
//        }
//        time.time = t;
        Time time = new Time();
        time.address = address;
        int t = (int) System.currentTimeMillis();
        InetAddress a = InetAddress.getByName(address);
        a.isReachable(10000);
        t = (int)System.currentTimeMillis() - t;
        time.time = t;
        return time;
    }
}
