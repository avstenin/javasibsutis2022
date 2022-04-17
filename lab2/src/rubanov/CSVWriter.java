package lab2.src.rubanov;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class CSVWriter implements Writer {

    private static final String logDirectoryPath = new File("").getAbsolutePath()
            + File.separator + "lab2" + File.separator + "src" + File.separator + "rubanov" + File.separator + "log";

    @Override
    public void createLogFile(Map<String, Float> map) {
        try {
            createFolderIfNotExist();
            tryWriteMapToFile(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tryWriteMapToFile(Map<String, Float> map) throws Exception {
        var fileName = generateNewFileName();
        var fileNamePath = logDirectoryPath + File.separator + generateNewFileName();

        CSVPrinter printer = new CSVPrinter(new FileWriter(fileNamePath), CSVFormat.EXCEL);
        for (var row : map.entrySet()) {
            printer.printRecord(row.getKey(), row.getValue());
        }
        printer.close();

        System.out.println("File created: " + fileName);
    }

    private void createFolderIfNotExist() throws Exception {
        var folderCreator = new File(logDirectoryPath);
        if (!folderCreator.exists()) {
            if (folderCreator.mkdir()) {
                System.out.println("Folder created: " + folderCreator.getName());
            } else {
                throw new Exception("Cannot create folder");
            }
        }
    }

    private String generateNewFileName() {
        return getTime() + ".csv";
    }

    private String getTime() {
        return ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
