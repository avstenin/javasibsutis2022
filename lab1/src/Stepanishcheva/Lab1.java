import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Lab1 {
    public static final int addressNum = 3;

    public static Long getTime(InetAddress address) throws IOException {
        long accessTime = 0;
        for (int i = 0; i < addressNum; i++) {
            long startTimer = System.nanoTime();
            if (address.isReachable(3000)) {
                accessTime += System.nanoTime() - startTimer;
            }
        }
        return accessTime / addressNum;
    }

    public static void getAddress() throws IOException {
        Long[] timeResult = new Long[addressNum];
        HashMap<String, Long> userOutput = new HashMap<>();
        Scanner userInput = new Scanner(System.in);
        InetAddress userAddress = null;
        for (int i = 0; i < addressNum; i++) {
            System.out.println("Введите адрес сервера DNS" + (i + 1) + ": ");
            try {
                 userAddress = InetAddress.getByName(userInput.nextLine());
            } catch (IOException e) {
                System.out.println("Некорректный ввод адреса");
            }
            timeResult[i] = getTime(userAddress);
            if (timeResult[i] != 0) {
                userOutput.put("Среднее время доступа до DNS сервера " + userAddress, timeResult[i]);
            }
        }
        userOutput.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(System.out::println);
    }

    public static void main(String[] args) throws IOException {
        getAddress();
    }
}
