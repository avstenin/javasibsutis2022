package lab2.src.rubanov;

import java.io.IOException;

public class lab2 {

    public static void main(String[] args) throws Exception {
        try {
            new DNSAnalyzer().startTest(5).printTopTimes();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
