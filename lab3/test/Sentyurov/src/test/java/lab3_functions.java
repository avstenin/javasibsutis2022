import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class lab3_functions {

    public int dir_check(Path path, int file_count) {
        ArrayList<Path> files = new ArrayList<>(); //список подходящих файлов
        File directory = new File(path.toString()); //путь директории
        File[] all_files = directory.listFiles(); //массив, хранящий все файлы в директории

        for(int i = 0; i < all_files.length; i++) { //по всем файлам текущей директории
            if (all_files[i].isFile()) { //если текущий элемент файл, НЕ ДИРЕКТОРИЯ
                if(filename_check(all_files[i].getName())){ //и его имя соответствует регулярке программы (лог)
                    files.add(all_files[i].toPath()); //добавление файла в список всех подходящих файлов
                }
            } else { //если директория - перейти в директорию и повторить
                file_count = dir_check(Paths.get(all_files[i].toString()), file_count);
            }
        }
        System.out.println("\n\n" + files.size());
        file_count += files.size();
        return file_count;
    }

    public int fileReader(Path filename) throws IOException {
        int control = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename.toString()))) {  //получаем содержимое файла
            String line;

            String firstLine = "Date of ping: " + filename.toString().substring(filename.toString().length() - 23, filename.toString().length() - 4); //создание первой строки
            String[] pack_success = {"IP-address:", "server's answer time:", "ms"}; //создание капов для кооректных
            String[] pack_failed = {"IP-address:", "ping unsuccessful: all packets were lost"}; // и не корректных ответов

            line = reader.readLine();
            if(line.contains(firstLine)) { //если первая строка совпадает
                while ((line = reader.readLine()) != null) { //получение строки
                    boolean full1 = true, full2 = false;

                    for (String str : pack_success) {
                        if (!line.contains(str)) { //если не содержится элемент капа при успешном пинге
                            full1 = false; //выход - несовпадение по строке (исход возможен)
                            break;
                        } else {
                            full1 = true;
                        }
                    }
                    for (String str : pack_failed) {
                        if (!line.contains(str)) { //если не содержится элемент капа при провальном пинге
                            full2 = false; //выход - несовпадение по строке (исход возможен)
                            break;
                        } else {
                            full2 = true;
                        }
                    }
                    if (full1 ^ full2) { //если как минимум один из двух капов в наличии
                        control = 1;
                    } else { //если строка не подходит
                        control=  0;
                    }
                }
            }
        }
        return control;
    }

    public int get_ping(String result) throws Exception {
        int packetNumber = 4, success = 0;
        String answer;
        ArrayList<String> answers = new ArrayList<>(); //список с результатами пингования
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win"); //если система windows - true
        ProcessBuilder builder = new ProcessBuilder("ping", isWindows ? "-n" : "-c", String.valueOf(packetNumber), result);
        builder.redirectErrorStream(true);
        Process process = builder.start(); //запуск команды
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream())); //получение результата после запуска

        while (true) {
            answer = bufferedReader.readLine();
            if (answer != null)
                answers.add(answer); //чтение и построчное добавление в массив результатов
            else
                break;
        }

        Pattern pattern = Pattern.compile(".+ \\= (\\d+)"); // [... = ddd...d]
        Matcher matcher = pattern.matcher(answers.get(answers.size() - 1));
        if (matcher.find()) {
            success = 1;
        }
        return success;
    }

    public boolean ip_check(String line){
        String regex = "((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)(\\.)){3}" +
                "(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)";
        return Pattern.matches(regex, line);
        //3 * (250 - 255 / 200 - 249 / 000 - 199 + .) + ((250 - 255 / 200 - 249 / 000 - 199)
    }

    public boolean filename_check(String name){
        String regex = "log_(0[1-9]|[1-2]\\d|3[01])_(0[1-9]|1[0-2])_(\\d{4})_([0-1]\\d|2[0-3])(_([0-5]\\d)){2}.txt";
        return Pattern.matches(regex, name);
    }

    public boolean date_check(String name){
        String regex = "(0[1-9]|[1-2]\\d|3[01])_(0[1-9]|1[0-2])_(\\d{4})";
        return Pattern.matches(regex, name);
    }
}
