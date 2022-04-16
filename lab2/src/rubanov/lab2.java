package lab2.src.rubanov;

import java.io.IOException;
import java.util.Scanner;

public class lab2 {

    public static void main(String[] args) throws Exception {
        try {
            var scanner = new Scanner(System.in);
            System.out.println("Enter the number of DNS servers:");
            var testsCount = scanner.nextInt();
            new DNSAnalyzer().startTest(testsCount).printTopTimes();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
