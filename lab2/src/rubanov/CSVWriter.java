package lab2.src.rubanov;

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
        var fileWriter = new FileWriter(fileNamePath);
        var stringBuilder = new StringBuilder();

        for (var row : map.entrySet()) {
            stringBuilder.append(row.getKey()).append(',');
            stringBuilder.append(row.getValue()).append('\n');
        }
        fileWriter.write(String.valueOf(stringBuilder));
        fileWriter.close();

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
        } else {
            System.out.println("Folder " + folderCreator.getName() + " exist");
        }
    }

    private String generateNewFileName() {
        return getTime() + ".csv";
    }

    private String getTime() {
        return ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
