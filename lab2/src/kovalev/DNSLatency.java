import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DNSLatency {
    public static void main(String[] args)
    throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            int ipQueryNum;
            System.out.println("Введите число серверов DNS:");
            while (true) {
                try {
                    ipQueryNum = Integer.parseInt(scanner.next());
                    break;
                }
                catch (NumberFormatException e) {
                    System.out.println("Введите корректное число!");
                }
            }

            try {
                for (int i = 1; i <= ipQueryNum; i++) {
                    System.out.println("Введите адрес сервера DNS" + String.valueOf(i) + ":");
                    String ip = scanner.next();
                    var query = PingTimeMeasure.findQuery(ip);
                    if (query != null) {
                        System.out.println("Сервер по адресу '" + ip + "' уже был протестирован. Его результат: " + query.time() + " мс.");
                        System.out.println("Хотите сбросить результат? [y/N]");
                        char answer = (char) System.in.read();
                        if ((answer == 'Y') || (answer == 'y'))
                            PingTimeMeasure.remove(ip);
                        else
                            continue;
                    }
                    System.out.println();
                    PingTimeMeasure.add(ip);
                }
            } catch (InterruptedException | NoSuchElementException e) {
                System.out.println("Зафиксировали прерывание, выходим.");
            }
            
        }

        PingTimeMeasure.print();
        PingTimeMeasure.writeToFile("DNSQueries.csv");
    }
}