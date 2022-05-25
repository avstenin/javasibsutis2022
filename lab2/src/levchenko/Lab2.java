import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lab2 {
  private static int countIP, countStartIP, countAnswerDNS = 0;
  private final static int packetNumber = 4;

  public static void main(String[] args) throws Exception{
      ArrayList<String> ipAddress = new ArrayList<>();
      ArrayList<answerDNS> answerDNS = new ArrayList<>();

      Scanner scanner = new Scanner(System.in);
      System.out.print("Введите количество вводимых IP-адресов: ");
      countStartIP = countIP = scanner.nextInt();

      if(countStartIP == 1){
          System.out.println("Введите " + countStartIP + " ip-адрес:");
      } else if(countStartIP > 1 && countStartIP < 5){
          System.out.println("Введите " + countStartIP + " ip-адреса:");
      } else if(countStartIP > 4) {
          System.out.println("Введите " + countStartIP + " ip-адресов:");
      }
      else{
          System.out.println("Некорректный ввод, попробуйте еще раз:");
          countStartIP = countIP = scanner.nextInt();
      }

      if(countStartIP > 0){
          for(int i = 0; i < countStartIP; i++){
              String temp = scanner.next();
              if(ipAddress.contains(temp) == false){
                  ipAddress.add(temp);
              }
          }
          scanner.close();

          countIP = ipAddress.size();
          if(countStartIP != countIP){
              System.out.println("\nПрисутствуют одинаковые IP-адреса, в целях уникальности повторяющиеся не были добавлены!");
          }

          System.out.println("Подождите, идет подсчет...");
          Ping(ipAddress, answerDNS);
          saveFile(answerDNS);
      }
    }

    public static void Ping(ArrayList<String> ipAddress, ArrayList<answerDNS> answerDNS) throws Exception{
        for (int i = 0; i < countIP; i++){
            boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
            ProcessBuilder builder = new ProcessBuilder("ping", isWindows ? "-n" : "-c", String.valueOf(packetNumber), ipAddress.get(i));
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            ArrayList<String> memStrings = new ArrayList<>();

            while (true){
                line = bufferedReader.readLine();
                if (line == null){
                    break;
                }
                memStrings.add(line);
            }

            Pattern pattern = Pattern.compile(".+ \\= (\\d+)");
            Matcher matcher = pattern.matcher(memStrings.get(memStrings.size() - 1));
            if(matcher.find()){
                answerDNS.add(new answerDNS(ipAddress.get(i), Integer.parseInt(matcher.group(1))));
                countAnswerDNS++;
            }
        }

        if(countAnswerDNS != countIP){
            if(countAnswerDNS == 0){
                System.out.println("Результат работы: ответа нет\nСохранение в файл не произошло");
                return;
            }
            System.out.println("Результат работы (от некоторых IP-адресов нет отклика): ");
        } else if (countAnswerDNS == countIP){
            System.out.println("Результат работы:");
        }

        Collections.sort(answerDNS, Collections.reverseOrder());
        for (answerDNS out: answerDNS){
            System.out.printf("IP-адрес: %-15s время ответа сервера: %3d мс.\n", out.getIP(), out.getAnswer());
        }
    }

    public static void saveFile(ArrayList<answerDNS> answerDNS) throws IOException {
            if(answerDNS.isEmpty() == false){
                System.out.println("\nСохранение результатов в файл...");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
                String timeNow = dateTimeFormatter.format(LocalDateTime.now());
                try(FileWriter fileWriter = new FileWriter("FileLab2.txt", true);){
                    fileWriter.write("Пинг был сделан: " + timeNow + '\n');
                    for(answerDNS element : answerDNS){
                        fileWriter.write(element.toString() + "\n");
                    }
                    fileWriter.write('\n');
                }
                System.out.println("Сохранение произошло успешно");
            }
        }
}

class answerDNS implements Comparable<answerDNS>{
    private String ip;
    private int answer;

    public answerDNS(String ip, int answer) {
        this.ip = ip;
        this.answer = answer;
    }

    public String getIP(){
        return ip;
    }

    public int getAnswer(){
        return answer;
    }

    @Override
    public int compareTo(answerDNS otherDNS) {
        return answer - otherDNS.answer;
    }

    @Override
    public String toString() {
        return "Ответ от " + ip + " составляет " + answer + " мс.";
    }
}
