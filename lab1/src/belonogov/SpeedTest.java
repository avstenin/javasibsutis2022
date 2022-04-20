import java.util.Scanner;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SpeedTest{

    public static class DNS
    {
        String address;
        double time;

        public DNS(String address, double time)
        {
            this.address = address;
            this.time = time;
        }
    }

    public static void main(String[] args){
        DNS[] dnsArray = new DNS[3];
        Scanner input = new Scanner(System.in);

        for (int i = 0; i<dnsArray.length; ++i)
        {
            int j = i+1;
            String temp;
            System.out.println("Please, enter the " + j + " DNS address");
            temp = input.next();
            Duration mls = Metric(temp);
            double transDurationToMls = (double)mls.toMillis();
            dnsArray[i] = new DNS(temp, transDurationToMls/5);
        }

        for (int i = 0; i<3; ++i)
        {
            for (int j = 0; j<3-i-1; ++j)
            {
                System.out.println(dnsArray[i].time + "<>" + dnsArray[j].time);
                if (dnsArray[i].time > dnsArray[j].time)
                {
                    //have a nice day!
                    DNS temp = dnsArray[i];
                    dnsArray[i] = dnsArray[j];
                    dnsArray[j] = temp;
                }
            }
        }

        for (int i = 0; i<dnsArray.length; ++i)
        {
            int j = i+1;
            System.out.println("DNS №" + j + " with address " + dnsArray[i].address + "; Average speed: " + dnsArray[i].time);
        }
    }

    public static Duration Metric(String address)
    {
        Duration amount = Duration.ZERO;
        try{    
            InetAddress addr = InetAddress.getByName(address);
            for (int i = 0; i<5; ++i){
                Instant begin = Instant.now();
                if (addr.isReachable(2000))
                {
                    int j = i + 1;
                    Duration temporary = Duration.between(begin, Instant.now());
                    System.out.println("№" + j + " ping: " + temporary.toMillis() + "ms");
                    amount = amount.plus(temporary);
                }
            }
            return amount;
        }catch (IOException e)
        {
            System.out.println("Cannot set connection with: " + address);
        }
        return Duration.ofDays(1);
    }
}