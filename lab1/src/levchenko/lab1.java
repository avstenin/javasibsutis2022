import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class lab1 {
    public final static int countIP = 3;

    public static void main(String[] args) throws Exception{
        ArrayList<String> ipAddress = new ArrayList<>();
        ArrayList<answerDNS> answerDNS = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите 3 ip-адресса:");
        for(int i = 0; i < countIP; i++){
            ipAddress.add(scanner.nextLine());
        }
        scanner.close();

        System.out.println("Подождите, идет подсчет...");
        Ping(ipAddress, answerDNS);
    }

    public static void Ping(ArrayList<String> ipAddress, ArrayList<answerDNS> answerDNS) throws Exception{
        for (int i = 0; i < countIP; i++){
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "ping " + ipAddress.get(i));
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
            }
        }

        Collections.sort(answerDNS, Collections.reverseOrder());
        System.out.println("Результат работы:");
        for (answerDNS out: answerDNS){
            System.out.printf("IP-адресс: %-15s время ответа сервера: %3d мс.\n", out.getIP(), out.getAnswer());
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
}
