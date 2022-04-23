package dns_test;

import dns.DNSLatency;
import dns.ListParsingError;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ListParsing
{
    @Test
    public void correctListExtraction()
    {
        String[] tokens = DNSLatency.extractListElements("[208,1.1.1.1,ya.ru,localhost]");
        assertArrayEquals(new String[]{"208", "1.1.1.1", "ya.ru", "localhost"}, tokens);
    }

    @Test
    public void incorrectListExtraction()
    {
        try {
            DNSLatency.extractListElements("[]");
            fail();
        } catch (ListParsingError e) {}
        try {
            DNSLatency.extractListElements("gibberish");
            fail();
        } catch (ListParsingError e) {}
        try {
            DNSLatency.extractListElements(";;;");
            fail();
        } catch (ListParsingError e) {}
    }
}
