import java.util.Scanner;
import java.util.regex.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Comparable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class lab2 {

  public static void main(String[] args) throws IOException
  {
      System.out.println("Enter 3 DNS-servers:");

      ArrayList<String> DnsAddres = new ArrayList<String>();

      Scanner scan = new Scanner(System.in);

      for (int i = 0; i < 3; i++)
      {
          String temp = scan.nextLine();
          DnsAddres.add(temp);
      }
      scan.close();

      ArrayList<DNSRes> DNSres = new ArrayList<DNSRes>();

      for (int i = 0; i < 3; i++)
      {
          ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "ping "+ DnsAddres.get(i));
          builder.redirectErrorStream(true);
          Process p = builder.start();
          BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

          String line;
          ArrayList<String> consoleStrings = new ArrayList<String>();

          while (true)
            {
              int a =0;
              line = r.readLine();
              if (line == null)
              {
                break;
              }

              consoleStrings.add(line);
            }

          Pattern pattern = Pattern.compile(".* = (\\d*)");
          Matcher matcher = pattern.matcher(consoleStrings.get(consoleStrings.size() - 1));

          if (matcher.find())
          {
            DNSres.add(new DNSRes(DnsAddres.get(i), Integer.parseInt(matcher.group(1))));
          }
      }

      Collections.sort(DNSres, Collections.reverseOrder());

      System.out.println("");
      for (DNSRes a : DNSres)
      {
         System.out.println("Adr: " + a.getName() + ", time: " + a.getTime() + "ms");
      }

      writeToFile(DNSres);
  }


  public static void writeToFile(ArrayList<DNSRes> dnsRes) throws IOException
  {
    if(dnsRes.isEmpty() == false)
    {
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
      String currentTime = dateTimeFormatter.format(LocalDateTime.now());
      try(FileWriter fileWriter = new FileWriter("DNS-pings.txt", true);)
      {
        fileWriter.write("Pings by date: " + currentTime + '\n');
        for(DNSRes dns : dnsRes)
        {
          fileWriter.write("Adr: " + dns.getName() + ", time: " + dns.getTime() + "ms\n");
        }
        fileWriter.write('\n');
      }
      System.out.println("Information successfully saved");
    }
  }
}

class DNSRes implements Comparable<DNSRes>
{
  private String Name;
  private int Time;

  public DNSRes(String name, int time)
  {
    Name = name;
    Time = time;
  }

  public String getName()
  {
    return Name;
  }

  public int getTime()
  {
    return Time;
  }

  @Override
  public int compareTo(DNSRes obj)
  {
      return Time - obj.Time;
  }
}
