import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Lab1 {
    public static void main(String[] args) throws IOException {
        ArrayList<Time> data = new ArrayList<Time>();
        data.add(checkTime("8.8.8.8"));
        data.add(checkTime("8.8.4.4"));
        data.add(checkTime("77.88.8.7"));
        Collections.sort(data);
        System.out.println("address" + "  " + "time");
        for (Time i: data) {
            System.out.println(i.address + "  " + i.time);
        }
    }

    private static Time checkTime(String address) throws IOException {
        ArrayList<String> strings = new ArrayList<String>();
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "ping " + address);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = reader.readLine();
            if (line == null) {
                break;
            }
            strings.add(line);
        }
        p.destroy();
        Time time = new Time();
        time.address = address;
        int t = 0;
        int pos = 0;
        for (String i : strings) {
            pos = i.indexOf("Average");
            if(pos < 0) continue;
            pos += 10;
            while(Character.isDigit(i.charAt(pos))){
                t = t*10 + Character.getNumericValue(i.charAt(pos));
                pos++;
            }
            break;
        }
        time.time = t;
        return time;
    }
}
