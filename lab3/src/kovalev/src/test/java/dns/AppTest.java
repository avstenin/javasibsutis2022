package dns;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void correctListExtraction()
    {
        String[] tokens = DNSLatency.extractListElements("[208,1.1.1.1,ya.ru,localhost]");
        assertArrayEquals(new String[]{"208", "1.1.1.1", "ya.ru", "localhost"}, tokens);
    }
}
