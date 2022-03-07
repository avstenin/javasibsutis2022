import java.io.IOException;
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
            
            for (int i = 1; i <= ipQueryNum; i++) {
                System.out.println("Введите адрес сервера DNS" + String.valueOf(i) + ":");
                String ip = scanner.next();
                System.out.println();
                try {
                    PingTimeMeasure.add(ip);
                } catch (InterruptedException e) {
                    System.out.println("Зафиксировали прерывание, выходим.");
                    break;
                }
            }
        }

        PingTimeMeasure.print();
    }
}