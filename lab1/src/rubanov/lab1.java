import java.io.IOException;

public class lab1 {

    public static void main(String[] args) throws Exception {
        try {
            var test = new DNSAnalyzer().startTest().printTopTimes();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
