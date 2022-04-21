package dns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public final class PingTimeMeasure {
    private static List<PingResult> queriesLog = new ArrayList<PingResult>();
    private final static int packetNum = 5;

    private PingTimeMeasure() {}

    private static String extractValue(String str, String key)
    {
        Pattern p = Pattern.compile(".*" + key + "=([0-9]+)");
        Matcher match = p.matcher(str);
        match.find();
        return match.group(1);
    }

    public static PingResult findQuery(String ip)
    {
        return queriesLog.stream()
                             .filter( query -> ip.equals( query.getIp() ) )
                             .findAny()
                             .orElse(null);
    }

    public static void readCsv(Path path)
    throws IOException {
        File csvData = path.toFile();
        if (!csvData.exists() || csvData.isDirectory())
            return;
        
        try (CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.Builder.create(CSVFormat.DEFAULT).setDelimiter(";").build()))
        {
            for (CSVRecord csvRecord : parser) {
                String ip = csvRecord.get(0);
                if (findQuery(ip) == null)
                    queriesLog.add(new PingResult(ip, Double.parseDouble(csvRecord.get(1))));
            }
        }
    }

    public static void remove(String ip)
    {
        PingResult query = findQuery(ip);
        if (query != null)
            queriesLog.remove(query);
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
        int packetsReceived = 0;
        while ((s = stdInput.readLine()) != null)
        {
            if (s.contains("time=") || s.contains("время=")) {
                String timeMsString;
                if (s.contains("time=")) {
                    timeMsString = extractValue(s, "time");
                }
                else {
                    timeMsString = extractValue(s, "время");
                }
                Double timeMs = Double.parseDouble(timeMsString);
                totalTimeMs += timeMs;
                packetsReceived++;
            }
            else if (s.contains("could not find host") || s.contains("Name or service not known")) {
                System.out.println("Не удалось найти сервер по адресу '" + ip + "'. Сервер пропущен.");
                return exitSuccess;
            }
        }

        if (packetsReceived == 0) {
            System.out.println("Запросы на сервер '" + ip + "' остались безответными. Сервер пропущен.");
            return exitSuccess;
        }
        else if (packetsReceived < packetNum) {
            System.out.println(Integer.toString(packetNum - packetsReceived) + " из " + Integer.toString(packetNum) + " пакетов были потеряны.");
        }

        totalTimeMs /= packetsReceived;

        PingResult res = new PingResult(ip, totalTimeMs);
        queriesLog.add(res);
        Collections.sort(queriesLog, Comparator.comparing(PingResult::getTime).reversed());

        return exitSuccess;
    }

    public static PingResult[] getQueriesLog() {
        return queriesLog.toArray(new PingResult[0]);
    }

    public static void writeToFile(String destination) {
        if (queriesLog.isEmpty() || destination.isEmpty())
            return;

        File parentFolder = new File(destination).getAbsoluteFile().getParentFile();
        parentFolder.mkdirs();

        try (PrintWriter out = new PrintWriter(destination)) {
            for (PingResult res : queriesLog) {
                out.println(res.getIp() + ";" + res.getTime());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка записи в файл '" + destination + "'!");
        }
    }
}
