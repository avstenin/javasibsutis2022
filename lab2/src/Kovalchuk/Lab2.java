package Kovalchuk;

import java.io.*;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Scanner;

public class Lab2 {
    public static void main(String[] args) throws IOException {
        ArrayList<Time> data = new ArrayList<Time>();
        ArrayList<String> adresses  = new ArrayList<String>();
        System.out.println("Введите число адресов:");

        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Введите " + num + " адреса:");
        for (int i = 0; i < num; ++i) {
            System.out.print(i+1 + " адрес: ");
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
        for(String i: adresses){
            data.add(checkTime(i));
        }
        //data.add(checkTime("8.8.8.8"));
        //data.add(checkTime("8.8.4.4"));
        //data.add(checkTime("77.88.8.7"));
        //Collections.sort(data);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        BufferedWriter writer = new BufferedWriter(new FileWriter(timeStamp + ".txt"));
        for(Time i : data) {
            System.out.println("IP: " + i.address + ", average time: " + i.time + "ms\n");
            writer.write("IP: " + i.address + ", average time: " + i.time + "ms\n");
        }
        writer.close();
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

