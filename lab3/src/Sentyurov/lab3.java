package com.company;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class dns_info implements Comparable<dns_info>{
    private String address;
    private int ping;

    public dns_info(String address, int ping){
        this.address = address;
        this.ping = ping;
    }

    public String show_address(){
        return address;
    }

    public int show_ping(){
        return ping;
    }

    public String collect_info(){
        return "\tIP-address: " + address + " server's answer time: " + ping + " ms.";
    }

    @Override
    public int compareTo(dns_info comp_dns_info) {
        int result = ping - comp_dns_info.ping;
        return result;
    }
}

public class lab2 {

    public static int dns_count = 0;
    private final static int packetNumber = 4;

    public static void main(String[] args) throws Exception {
        ArrayList<dns_info> dns = new ArrayList<>();
        ArrayList<String> addresses = new ArrayList<>();
        Scanner read = new Scanner(System.in);
        System.out.print("How many DNS addresses you need to ping:");
        dns_count = read.nextInt();

        for(int i = 0; i < dns_count; i++){
            System.out.println("Please input "+(i+1)+" IP-address:");
            get_ping(read.next(), dns, addresses);
        }
        read.close();

        Collections.sort(dns, Collections.reverseOrder());
        System.out.println("\nProgram result:");

        for (dns_info out_dns: dns){
            System.out.printf("IP-address: %-15s server's answer time: %3d ms.\n", out_dns.show_address(), out_dns.show_ping());
        }
        System.out.println("If any of IP address is gone, that's mean that all packets were lost");
        create_record(dns,addresses);
    }

    public static void get_ping(String result, ArrayList<dns_info> dns, ArrayList<String> addresses) throws Exception {
        System.out.println("Please wait");
        addresses.add(result);
        String answer;
        ArrayList<String> answers = new ArrayList<>();
        //ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd C:/windows/system32 && ping " + result);
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        ProcessBuilder builder = new ProcessBuilder("ping", isWindows ? "-n" : "-c", String.valueOf(packetNumber), result);
        builder.redirectErrorStream(true);
        Process process = builder.start();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        while (true) {
            answer = bufferedReader.readLine();
            if (answer != null)
                answers.add(answer);
            else
                break;
        }

        Pattern pattern = Pattern.compile(".+ \\= (\\d+)");
        Matcher matcher = pattern.matcher(answers.get(answers.size() - 1));
        if (matcher.find()) {
            dns.add(new dns_info(result, Integer.parseInt(matcher.group(1))));
        }
    }

    public static void create_record(ArrayList<dns_info> dns, ArrayList<String> addresses) throws  Exception {
        boolean write_flag = false;
        System.out.println("\nCreating new file with record");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MMMM_yyyy_HH_mm_ss");
        String current_time = dateTimeFormatter.format(LocalDateTime.now());
        try (FileWriter fileWriter = new FileWriter("log_"+current_time+".txt");) {
            fileWriter.write("Date of ping: " + current_time + '\n');
            for (String checking_dns : addresses) {
                write_flag = false;
                for(dns_info success_dns: dns) {
                    if (checking_dns.equals(success_dns.show_address())) {
                        fileWriter.write(success_dns.collect_info() + "\n");
                        write_flag = true;
                    }
                }
                if(!write_flag){
                    fileWriter.write( "\tIP-address: " + checking_dns + " ping unsuccessful: all packets were lost"+"\n");
                }
            }
            fileWriter.write('\n');
        }
        System.out.println("Record create success");
    }
}
