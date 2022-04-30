package lab2.src.rubanov;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CSVReader implements Reader {
    private static final String logDirectoryPath = new File("").getAbsolutePath()
            + File.separator + "lab2" + File.separator + "src" + File.separator + "rubanov" + File.separator + "log";

    @Override
    public void printAllTestResults() throws IOException {
        var paths = getAllFilePaths();

        paths.forEach(path -> {
            System.out.println(parseDataInPath(path.toString()));

            try (CSVParser csvParser = CSVParser.parse(Files.newBufferedReader(path), CSVFormat.DEFAULT)) {
                for (CSVRecord csvRecord : csvParser) {
                    var ip = csvRecord.get(0);
                    var latency = csvRecord.get(1);
                    System.out.printf("%s %" + 15 + "s  -  %" + 6 + "s\n", csvRecord.getRecordNumber(), ip, latency);
                }
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private List<Path> getAllFilePaths() throws IOException {
        return Files.walk(Paths.get(new File("").getAbsolutePath()
                        + File.separator + "lab2" + File.separator + "src" + File.separator + "rubanov" + File.separator + "log"))
                .filter(Files::isRegularFile)
                .filter(file -> file.toString().endsWith(".csv"))
                .sorted()
                .toList();
    }

    private String parseDataInPath(String path) {
        var fileName = path.toString().split(File.separator);
        return fileName[fileName.length - 1].split("\\.")[0];
    }
}
