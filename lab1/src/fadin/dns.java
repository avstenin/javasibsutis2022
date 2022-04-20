package lab1.src.fadin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class dns {
    static String[] ans = new String[3];

    private static boolean checkString(String line) {
        if (line == null) {
            return false;
        }
        if (!line.contains("/")) {
            System.out.println("DNS unreachable");
            return false;
        }
        return true;
    }

    
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        for (int t = 0; t < 3; ++t) {
            ans[t] = "";
            String sin = sc.nextLine();
            ProcessBuilder builder = new ProcessBuilder("/bin/bash",
                    "-c",
                    "ping -c 5 " + sin + " | grep avg");
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (!checkString(line)) {
                    break;
                }
                String[] str = line.split("/");
                ans[t] = str[4] + "\t" + sin;
            }
        }
        sc.close();
        for (String i : ans) {
            if (i != "" && i != null) {
                System.out.println("Time\tDNS");
                Arrays.sort(ans, Collections.reverseOrder());
                for (String j : ans) {
                    System.out.println(j);
                }
                return;
            }
        }
        System.out.println("All dns's unreachable");
    }
}
