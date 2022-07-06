package com.company;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//8.8.8.8
//8.8.4.4
//1.1.1.1

//класс днс-адресов, содержит стоку с адресом и пинг.
class dns_info implements Comparable<dns_info>{
    private String address;
    private int ping;

    //конструктор
    public dns_info(String address, int ping){
        this.address = address;
        this.ping = ping;
    }

    //передача адреса для чего-либо
    public String show_address(){
        return address;
    }

    //передача пинга для чго-либо
    public int show_ping(){
        return ping;
    }

    //сборка и передача строки с инф-ией об адресе (для записи в файл)
    public String collect_info(){
        return "\tIP-address: " + address + " server's answer time: " + ping + " ms.";
    }

    //для сортировки по рез. пинга
    @Override
    public int compareTo(dns_info comp_dns_info) {
        int result = ping - comp_dns_info.ping;
        return result;
    }
}

public class lab3 {

    public static int dns_count = 0; //кол-во адресов
    public static int file_count = 0; //счетчик файлов в директории
    private final static int packetNumber = 4; //кол-во запросов для пингования одного адреса


    public static void main(String[] args) throws Exception {
        ArrayList<dns_info> dns = new ArrayList<>(); //массив эл-тов класса днс
        ArrayList<String> addresses = new ArrayList<>(); //массив строк для ввода адресов
        Scanner read = new Scanner(System.in);
        int choice;
        boolean flag = true;;
        menu();
        while (flag){
            System.out.print("\nNumber of operation: ");
            choice = read.nextInt();
            switch (choice) {
                case 1: //ввод с клавиатуры
                    keyboard_input(dns, addresses, read);
                    System.out.println("-----------------------------------\n");
                    menu();
                    break;
                case 2: //ввод с файла
                    file_input(dns, addresses, read);
                    System.out.println("-----------------------------------\n");
                    menu();
                    break;
                case 3: //история
                    view_history(read);
                    System.out.println("-----------------------------------\n");
                    menu();
                    break;
                case 4: //выход
                    System.out.println("Exit program");
                    flag = false;
                    break;

                default: //другие случаи
                    System.out.println("Invalid input, try again");
                }
        }
    }

    public static void menu() {
        System.out.print(
                "What do you want to do?\n\n" +
                "1) Input DNS from keyboard\n" +
                "2) Read DNS from file\n" +
                "3) Watch history\n" +
                "4) Exit\n");
    }

    public static void keyboard_input(ArrayList<dns_info> dns, ArrayList<String> addresses, Scanner read) throws Exception {
        System.out.print("How many DNS addresses you need to ping: ");
        dns_count = read.nextInt(); //ввод количества днс адресов
        String addr;

        for(int i = 0; i < dns_count; i++){
            System.out.println("\nPlease input "+(i+1)+" IP-address:");
            addr = read.next();
            while(!ip_check(addr)) {
                System.out.println("Invalid ip-address. Try again.\n");
                addr = read.next();
            }
            System.out.println("Please wait");
            get_ping(addr, dns, addresses); //получение адреса и вызов ф-ции пингования
        }

        Collections.sort(dns, Collections.reverseOrder()); //сортировака результатов
        System.out.println("\nProgram result:");

        for (dns_info out_dns: dns){ //вывод результатов после сортировки
            System.out.printf("IP-address: %-15s server's answer time: %3d ms.\n", out_dns.show_address(), out_dns.show_ping());
        }
        System.out.println("\nIf any of IP address is gone, that's mean that all packets were lost");
        create_record(dns,addresses, "|From keyboard|"); //создание лога
        dns_count = 0;
    }

    public static void file_input(ArrayList<dns_info> dns, ArrayList<String> addresses, Scanner read) throws Exception {
        System.out.print("Enter the full path of file: ");
        String filename = read.next(); //чтение имени файла
        File fileReader = new File(filename); //открытие файла

        while (!fileReader.exists()){ //если файл с указанным именем не существует
            System.out.println("No such file in directory. Try again.\n");
            filename = read.next(); //читаем имя файла снова
            fileReader = new File(filename);
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(fileReader))){ //получаем содержимое файла
            ArrayList<String> file_addresses = new ArrayList<>(); //массив адресов в файле
            String line;

            while((line = reader.readLine()) != null){ //пока файл не пустой
                if (ip_check(line)) { //если строка подходит под адрес
                    dns_count++;
                    file_addresses.add(line); //добавляем в массив адресов
                }
            }

            if(dns_count > 0) {
                System.out.println("\nAddresses, found in file to be pinged:");
                for (int i = 0; i < dns_count; i++) { //вывод найденных адресов
                    System.out.println(file_addresses.get(i));
                }
                System.out.print("\n");

                System.out.println("Start pinging");
                for (int i = 0; i < dns_count; i++) { //пингование найденных адресов
                    System.out.println("\tPinging address #" + (i + 1) + " (" + file_addresses.get(i) + ")");
                    get_ping(file_addresses.get(i), dns, addresses);
                }

                Collections.sort(dns, Collections.reverseOrder()); //сортировака результатов
                System.out.println("\nProgram result:");

                for (dns_info out_dns: dns){ //вывод результатов после сортировки
                    System.out.printf("\tIP-address: %-15s server's answer time: %3d ms.\n", out_dns.show_address(), out_dns.show_ping());
                }
                System.out.println("\nIf any of IP address is gone, that's mean that all packets were lost");
                create_record(dns,addresses, "|From file " + filename + '|'); //создание лога
                dns_count = 0;
            }
            else{
                System.out.println("No matching addresses were found in file, or file is empty");
            }
        }
    }

    public static void view_history(Scanner read) throws IOException{
        int choice;
        boolean flag = true;
        System.out.println("\nChose one:\n" +
                "1) Show history of all files\n" +
                "2) Show history in files filtered by date\n");

        while(flag) {
            System.out.print("Number of operation: ");
            choice = read.nextInt();
            switch (choice) {
                case 1:
                    show_all();
                    flag = false;
                    break;
                case 2:
                    show_by_filter(read);
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid command. Try again\n");
            }
        }
    }

    public static void show_by_filter(Scanner read) throws IOException{
        System.out.print("\nInput the date of log-file by next example 'dd_MM_YYYY': ");
        String filter = read.next();

        while(!date_check(filter)){
            System.out.println("Invalid date. Try again.\n");
            System.out.print("Input the date of log-file by next example 'dd_MM_YYYY': ");
            filter = read.next();
        }
        dir_check(Paths.get("src"), filter, true);
    }

    public static void show_all() throws IOException{
        dir_check(Paths.get("src"), "", false);
    }

    public static void dir_check(Path path, String filter, boolean flag) throws IOException{
        ArrayList<Path> files = new ArrayList<>(); //список подходящих файлов
        File directory = new File(path.toString()); //путь директории
        File[] all_files = directory.listFiles(); //массив, хранящий все файлы в директории

        for(int i = 0; i < all_files.length; i++) { //по всем файлам текущей директории
            if (all_files[i].isFile()) { //если текущий элемент файл, НЕ ДИРЕКТОРИЯ
                if(filename_check(all_files[i].getName())){ //и его имя соответствует регулярке программы (лог)
                    files.add(all_files[i].toPath()); //добавление файла в список всех подходящих файлов
                }
            } else { //если директория - перейти в директорию и повторить
                dir_check(Paths.get(all_files[i].toString()), filter, flag);
            }
        }

        for(Path current_file: files){ //когда закончилась рекурсия, среди подходящий файлов
            if(!flag){ //если показываем всю историю
                fileReader(current_file); //вывод содержимого файла
            } else if(current_file.toString().contains(filter)) { //если по фильтру и фильр пропускает
                file_count++;
                fileReader(current_file); // вывод содержимого файла
            }
        }

        if(file_count == 0 && directory.toString().equals("src")){ //если в директории не было подходящих файлов
            System.out.println("No matches found");
        }
        file_count = 0;
    }

    public static void fileReader(Path filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename.toString()))) {  //получаем содержимое файла
            String line;
            boolean all = true;
            System.out.println("\n-------------------------------------------\n"+
                    (++file_count) + ". Path of current file: " + filename.toString()); //вывод пути текущего файла
            String firstLine = "Date of ping: " + filename.toString().substring(filename.toString().length() - 23, filename.toString().length() - 4); //создание первой строки
            String[] pack_success = {"IP-address:", "server's answer time:", "ms"}; //создание капов для кооректных
            String[] pack_failed = {"IP-address:", "ping unsuccessful: all packets were lost"}; // и не корректных ответов

            ArrayList<String> allFile = new ArrayList<>(); //массив для вывода информации файла
            line = reader.readLine();
            if(line.contains(firstLine)) { //если первая строка совпадает
                allFile.add(line);
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
                        allFile.add(line); //добавляем строку в итоговый вывод
                    } else { //если строка не подходит
                        System.out.println("\tFile structure corrupted");
                        all = false;
                        break;
                    }
                }
            }
            if(all){ //если все строки были прочтены и соотнесены успешно
                for(String str : allFile){ //построчный вывод файла
                    System.out.println(str);
                }
            }
        }
    }

    public static void get_ping(String result, ArrayList<dns_info> dns, ArrayList<String> addresses) throws Exception {
        addresses.add(result);
        String answer;
        ArrayList<String> answers = new ArrayList<>(); //список с результатами пингования
        //ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd C:/windows/system32 && ping " + result);
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
            dns.add(new dns_info(result, Integer.parseInt(matcher.group(1)))); //создание эл-та, если найден паттерн. если не найден - пакеты потеряны, эл-т не создается
        }
    }

    public static void create_record(ArrayList<dns_info> dns, ArrayList<String> addresses, String type_input) throws  Exception {
        boolean write_flag = false;
        int count = 0;
        System.out.println("\nCreating new file with record");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss"); //создание формата даты
        String current_time = dateTimeFormatter.format(LocalDateTime.now()); //получение текущей даты
         try (FileWriter fileWriter = new FileWriter("src/log_"+current_time+".txt");) { //создание файла с нужным названием
            fileWriter.write("Date of ping: " + current_time + ". Type of input - " + type_input +  "\n"); //занесение даты в файл
            for (String checking_dns : addresses) { //в адресах содержатся все адреса для пингования
                write_flag = false; //обнуление флага записи
                for(dns_info success_dns: dns) { //в успешных адресах содержатся только те адреса, от которых пришел ответ
                    if (checking_dns.equals(success_dns.show_address())) {
                        fileWriter.write(success_dns.collect_info() + "\n"); //запись в файл адреса с ответом
                        write_flag = true;
                        count++;
                    }
                }
                if(!write_flag){
                    fileWriter.write( "\tIP-address: " + checking_dns + " ping unsuccessful: all packets were lost"+"\n"); //запись в файл адреса без ответа
                    count++;
                }
            }
            if(count == 0){
                fileWriter.write( "\tNothing was ping\n"); //запись в файл адреса без ответа
            }
        }
        System.out.println("Record create success");
    }

    public static boolean ip_check(String line){
        String regex = "((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)(\\.)){3}" +
                "(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)";
        return Pattern.matches(regex, line);
        //3 * (250 - 255 / 200 - 249 / 000 - 199 + .) + ((250 - 255 / 200 - 249 / 000 - 199)
    }

    public static boolean filename_check(String name){
        String regex = "log_(0[1-9]|[1-2]\\d|3[01])_(0[1-9]|1[0-2])_(\\d{4})_([0-1]\\d|2[0-3])(_([0-5]\\d)){2}.txt";
        return Pattern.matches(regex, name);
    }

    public static boolean date_check(String name){
        String regex = "(0[1-9]|[1-2]\\d|3[01])_(0[1-9]|1[0-2])_(\\d{4})";
        return Pattern.matches(regex, name);
    }
}
