package zaripov;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final int numOfResultStr = 14;
    public static int countDNSs;

    public static void main(String[] args) {
        String currDNS = "";
        String[] DNSs = {""};
        String[][] DNSinfo;

        Scanner in = new Scanner(System.in);
        System.out.println("Выберите режим работы: ");
        System.out.println("1.Вывести историю измерений");
        System.out.println("2.Измерить время отклика DNS-сервера(ов)");
        System.out.print("Выберите - ");
        int workMode = in.nextInt();
        if (workMode != 1 && workMode != 2) {
            System.out.print("Выберите режим работы: ");
            workMode = in.nextInt();
        }

        if (workMode == 1) {
            try {
                printMeasures();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Выберите режим ввода DNS адресов: ");
            System.out.println("1.Из консоли");
            System.out.println("2.Из файла");
            System.out.print("Выберите - ");
            int inputMode = in.nextInt();
            if (inputMode != 1 && inputMode != 2) {
                System.out.print("Выберите режим ввода DNS адресов: ");
                inputMode = in.nextInt();
            }

            if (inputMode == 2) {
                String fileName;
                try {
                    in.nextLine();
                    System.out.print("Введите txt-файл (с расширением) с DNS адресами: ");
                    fileName = in.nextLine();
                    if (getFileExtension(fileName).isEmpty()) {
                        throw new IOException("Неправильный формат файла!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                try {
                    DNSs = fillDNSArray(fileName);
                } catch (NoSuchElementException | FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
            }

            if (inputMode == 1) {
                System.out.print("Введите количество DNS адресов: ");
                countDNSs = in.nextInt();
                in.nextLine();
            } else
                countDNSs = DNSs.length;

            DNSinfo = new String[3][countDNSs];

            for (int i = 0; i < (inputMode == 1 ? countDNSs : DNSs.length); i++) {
                try {
                    if (inputMode == 1) {
                        System.out.print("Введите адрес сервера DNS" + (i + 1) + ": ");
                        currDNS = in.nextLine();
                    } else currDNS = DNSs[i];

                    BufferedReader r = getBufferOfProcess(currDNS);
                    String averageTimeLine = getResultLine(r, currDNS);
                    if (averageTimeLine == null)
                        continue;
                    DNSinfo[2][i] = String.valueOf(parseLine(averageTimeLine));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    DNSinfo[2][i] = "0";
                } finally {
                    DNSinfo[0][i] = currDNS;
                }
            }

            sort(DNSinfo);

            System.out.println();

            String fileName = writeToCSV(DNSinfo);
            readFromCSV(fileName);
        }
    }

    private static void printMeasures() throws Exception{
        File directorySearch = new File(System.getProperty("user.dir"));
        File[] files = directorySearch.listFiles();
        List<File> filesOfMeasures = new ArrayList<>();

        if (files == null)
            throw new Exception("История измерений отсутствует!");

        for (File file : files)
            if (file.toString().toLowerCase().endsWith("csv"))
                filesOfMeasures.add(file);

        if (filesOfMeasures.isEmpty()) {
            throw new Exception("История измерений отсутвует!");
        }

        for (File file : filesOfMeasures) {
            System.out.println(file + ":");
            readFromCSV(file.toString());
            System.out.println();
        }
    }

    private static String[] fillDNSArray(String fileName) throws FileNotFoundException {
        List<String> DNSs = new ArrayList<>();

        try {
            Reader reader = new FileReader(fileName);
            BufferedReader br = new BufferedReader(reader);

            String line;

            while ((line = br.readLine()) != null) {
                Pattern pattern = Pattern.compile("([0-9]{1,3}\\.){3}[0-9]{1,3}");
                Matcher matcher = pattern.matcher(line);
                while (matcher.find())
                    DNSs.add(line.substring(matcher.start(), matcher.end()));
            }
            br.close();
        } catch (Exception e) {
            throw new FileNotFoundException("Файл не найден!");
        }
        if (DNSs.isEmpty())
            throw new NoSuchElementException("DNS адрес(а) не найден!");

        return DNSs.toArray(new String[0]);
    }

    private static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            if (fileName.substring(fileName.lastIndexOf(".") + 1).equals("txt"))
                return "txt";
            else return "";
        else return "";
    }

    private static void readFromCSV(String fileName) {
        try {
            Reader reader = new FileReader(fileName);
            BufferedReader br = new BufferedReader(reader);

            if (br.readLine() == null)
                return;

            String line;

            while ((line = br.readLine()) != null) {
                String[] values;
                values = line.split(",");
                System.out.println("Avg time of DNS: " + values[0] + " is " + values[1] + ", sec.");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String writeToCSV(String[][] DNSInfo) {
        try {
            String fileName = File.createTempFile("data_", ".csv", new File(System.getProperty("user.dir"))).toString();
            FileWriter file = new FileWriter(fileName);

            StringBuilder sb = new StringBuilder();

            sb.append("DNS address");
            sb.append(',');
            sb.append("Avg time");
            sb.append('\n');

            for (int i = 0; i < countDNSs; i++) {
                sb.append(DNSInfo[0][i]);
                sb.append(',');
                sb.append(DNSInfo[2][i]);
                sb.append('\n');
            }

            file.write(sb.toString());
            file.close();

            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedReader getBufferOfProcess(String DNS) throws IOException {
        try {
            ProcessBuilder builder = new ProcessBuilder("ping", "-c", "10", DNS);

            if (isWindows())
                builder = new ProcessBuilder("ping", "-n", "8", DNS);

            builder.redirectErrorStream(true);
            Process p = builder.start();
            return new BufferedReader(new InputStreamReader(p.getInputStream()));
        } catch (IOException e) {
            throw new IOException("Не удалось создать процесс");
        }
    }

    public static double parseLine(String line) {
        String[] wordsArray = line.split(",");
        String[] timeArray = wordsArray[2].split(" ");
        if (isWindows())
            return Double.parseDouble(timeArray[3].substring(0, 2));
        return Double.parseDouble(timeArray[3]);
    }

    public static String getResultLine(BufferedReader r, String DNS) throws IOException {
        String line;
        for (int i = 0; i <= numOfResultStr; i++) {
            line = r.readLine();
            if (line == null)
                throw new IOException("Не удалось обратиться к DNS: " + DNS);
            System.out.println(line);
            if (i == numOfResultStr) {
                return line;
            }
        }
        return null;
    }

    public static void sort(String[][] DNSInfo) {
        for (int i = 0; i <= countDNSs - 1; i++) {
            for (int j = 0; j <= i; j++) {
                if (Double.parseDouble(DNSInfo[2][i]) > Double.parseDouble(DNSInfo[2][j])) {
                    String tmp = DNSInfo[2][i];
                    DNSInfo[2][i] = DNSInfo[2][j];
                    DNSInfo[2][j] = tmp;
                    tmp = DNSInfo[0][i];
                    DNSInfo[0][i] = DNSInfo[0][j];
                    DNSInfo[0][j] = tmp;
                }
            }
        }
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.contains("win"));
    }

}


