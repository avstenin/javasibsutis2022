import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PingResult {
    private String ip;
    private Double time;

    public PingResult(String ip, Double time) {
        this.ip = ip;
        this.time = time;
    }

    public void print() {
        System.out.println("IP: " + ip);
        System.out.println("Среднее время: " + String.valueOf(time) + " мс");
        System.out.println();
    }

    public Double time() {
        return time;
    }
}

public final class PingTimeMeasure {
    private static ArrayList<PingResult> connectionTime = new ArrayList<PingResult>();
    private final static int packetNum = 5;

    private PingTimeMeasure() {}

    private static String extractValue(String str, String key) {
        Pattern p = Pattern.compile(".*" + key + "=([0-9]+)");
        Matcher match = p.matcher(str);
        match.find();
        return match.group(1);
    }

    public static int add(String ip)
    throws IOException, InterruptedException
    {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
    
        ProcessBuilder pb = new ProcessBuilder("ping", isWindows ? "-n" : "-c", String.valueOf(packetNum), ip);
        Process proc = pb.start();

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

        int exitSuccess = proc.waitFor();

        String s = null;
        Double totalTimeMs = .0;
        while ((s = stdInput.readLine()) != null)
        {
            if (s.contains("time=")) {
                String timeMsString = extractValue(s, "time");
                Double timeMs = Double.parseDouble(timeMsString);
                totalTimeMs += timeMs;
            }
        }
        totalTimeMs /= packetNum;
        PingResult res = new PingResult(ip, totalTimeMs);
        connectionTime.add(res);
        Collections.sort(connectionTime, Comparator.comparing(PingResult::time).reversed());
        return exitSuccess;
    }

    public static void print() {
        for (PingResult res : connectionTime) {
            res.print();
        }
    }
}
