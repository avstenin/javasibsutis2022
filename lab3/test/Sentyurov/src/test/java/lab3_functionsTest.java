import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

public class lab3_functionsTest {
    lab3_functions funcs = new lab3_functions();

    @Test
    public void dir_check(){
        Path path = Path.of("src");
        int input = funcs.dir_check(path, 0);
        int output = 2;
        assertEquals(output, input);
    }

    @Test
    public void filename_check_true(){
        boolean input = funcs.filename_check("log_06_07_2022_15_59_15.txt");
        boolean output = true;
        assertEquals(output, input);
    }

    @Test
    public void filename_check_fail(){
        boolean input = funcs.filename_check("log_06_.txt");
        boolean output = false;
        assertEquals(output, input);
    }

    @Test
    public void filereader_success() throws IOException {
        Path path = Path.of("src/main/java/log_06_07_2022_15_59_15.txt");
        int input = funcs.fileReader(path);
        int output = 1;
        assertEquals(output, input);
    }

    @Test
    public void filereader_fail() throws IOException {
        Path path = Path.of("src/main/java/log_06_07_2022_16_16_47.txt");
        int input = funcs.fileReader(path);
        int output = 0;
        assertEquals(output, input);
    }

    @Test
    public void get_ping_success() throws Exception {
        int input = funcs.get_ping("8.8.8.8");
        int output = 1;
        assertEquals(output, input);
    }

    @Test
    public void get_ping_fail() throws Exception {
        int input = funcs.get_ping("4.4.4.4");
        int output = 0;
        assertEquals(output, input);
    }

    @Test
    public void ip_check_success() {
        boolean input = funcs.ip_check("8.8.8.8");
        boolean output = true;
        assertEquals(output, input);
    }

    @Test
    public void ip_check_fail() {
        boolean input = funcs.ip_check("3000.5.8.9");
        boolean output = false;
        assertEquals(output, input);
    }

    @Test
    public void date_check_success() {
        boolean input = funcs.date_check("06_07_2022");
        boolean output = true;
        assertEquals(output, input);
    }

    @Test
    public void date_check_fail() {
        boolean input = funcs.date_check("99_99_99");
        boolean output = false;
        assertEquals(output, input);
    }
}