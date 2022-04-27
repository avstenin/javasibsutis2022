package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

    @Override
    public int compareTo(dns_info comp_dns_info) {
        int result = ping - comp_dns_info.ping;
        return result;
    }
}

public class lab1 {

    public final static int dns_count = 3;

    public static void main(String[] args) throws Exception {
        ArrayList<dns_info> dns = new ArrayList<>();
        Scanner read = new Scanner(System.in);


        for(int i = 0; i < dns_count; i++){
            System.out.println("Please input "+(i+1)+" IP-address:");
            get_ping(read.nextLine(), dns);
        }
        read.close();

        Collections.sort(dns, Collections.reverseOrder());
        System.out.println("Program result:");

        for (dns_info out_dns: dns){
            System.out.printf("IP-address: %-15s server's answer time: %3d ms.\n", out_dns.show_address(), out_dns.show_ping());
        }
    }

    public static void get_ping(String result, ArrayList<dns_info> dns) throws Exception {
        //System.out.println("Computing");
        String answer;
        ArrayList<String> answers = new ArrayList<>();
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd C:/windows/system32 && ping " + result);
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
}
