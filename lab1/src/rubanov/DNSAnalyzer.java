import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class DNSAnalyzer {
    private LinkedHashMap<String, String> testResults;

    DNSAnalyzer(){
        testResults = new LinkedHashMap<>();
    }

    public DNSAnalyzer startTest() throws Exception {
        greet();
        measurePing();
        sortInDescendingOrder();
        return this;
    }

    private void greet() {
        System.out.println("Введите 3 DNS адреса для вычисления задержки до них:");
    }

    private ArrayList<String> scanAddresses() {
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

    private void measurePing() throws Exception {
        var addresses = scanAddresses();

        var builder = new ProcessBuilder().redirectErrorStream(true);

        for (var address : addresses) {
            var command = generateCommand(address);
            builder.command(command);
            var process = builder.start();
            var reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String timeToHost;
            while (true) {
                timeToHost = reader.readLine();
                if (timeToHost == null)
                    break;

                if (!timeToHost.startsWith("ping") && !timeToHost.startsWith("0"))
                    testResults.put(address, timeToHost);
            }
        }
    }

    public void sortInDescendingOrder() {
        testResults = testResults.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    public DNSAnalyzer printTopTimes() {
        if (testResults.isEmpty()) {
            System.out.println("Не удалось вычислить пинг.");
            return this;
        }

        for (var row : testResults.entrySet())
            System.out.printf("%15s  -  %6s\n", row.getKey(), row.getValue());
        return this;
    }
}
