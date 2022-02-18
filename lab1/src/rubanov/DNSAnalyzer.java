import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class DNSAnalyzer
{
    private HashMap<String, String> timeStatistic;

    public DNSAnalyzer startTest() throws Exception{
        greet();
        timeStatistic = measurePing();
        return this;
    }

    private void greet() {
        System.out.println("Введите 3 DNS адреса для вычисления задержки до них:");
    }

    private  ArrayList<String> scanAddresses() {
        ArrayList<String> addresses = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < 3; i++) {
            System.out.printf("Введите адрес сервера DNS%d:\n", i + 1);
            addresses.add(scanner.next());
        }
        return addresses;
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    private ArrayList<String> generateCommand(String address) throws Exception {
        if (isWindows()) {
            throw new Exception();
        } else {
            var command = new ArrayList<>(Arrays.asList("sh", "-c"));
            command.add(String.format("ping -c 2 %s | tail -1| awk '{print $4}' | cut -d '/' -f 2", address));
            return command;
        }
    }

    private HashMap<String, String> measurePing() throws Exception {

        var addresses = scanAddresses();

        var builder = new ProcessBuilder().redirectErrorStream(true);

        var outputStrings = new HashMap<String, String>();

        for (var address : addresses) {
            var command = generateCommand(address);
            builder.command(command);
            var process = builder.start();
            var reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while (true) {
                line = reader.readLine();
                if (line == null)
                    break;

                if (!line.startsWith("ping") && !line.startsWith("0"))
                    outputStrings.put(address, line);
            }
        }
        return outputStrings;
    }

    public void printTopTimes() {
        if (timeStatistic.isEmpty()) {
            System.out.println("Не удалось вычислить пинг.");
            return;
        }

        for (var row : timeStatistic.entrySet()) {
            System.out.printf("%15s  -  %6s\n", row.getKey(), row.getValue());
        }
    }
}
