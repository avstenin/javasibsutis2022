import org.junit.Test;
import static org.junit.Assert.*;
import zaripov.Main;

public class MainTest {

    @Test
    public void parseLine() {
        String parsingLine = "    Minimum = 69ms, Maximum = 82ms, Average = 71ms";
        assertEquals(71d, Main.parseLine(parsingLine), 2);
    }

    @Test
    public void sort() {
        String[][] DNSInfo = {{"8.8.8.8", "77.88.8.7", "8.8.4.4"}, {null, null, null}, {"70.0", "65.0", "68.0"}};
        Main.countDNSs = 3;
        Main.sort(DNSInfo);
        assertArrayEquals(new String[] {"8.8.8.8", "8.8.4.4", "77.88.8.7"}, DNSInfo[0]);
        assertArrayEquals(new String[] {"70.0", "68.0", "65.0"}, DNSInfo[2]);
    }
}