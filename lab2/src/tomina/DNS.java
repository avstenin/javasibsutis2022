package tomina;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class DNS {
    private int time;
    private String name;
    private String ip_addr;

    public int getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getIp_addr() {
        return ip_addr;
    }

    public DNS(String ip_addr) {
        this.ip_addr = ip_addr;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public static final Comparator<DNS> COMPARE_BY_TIME = new Comparator<DNS>() {
        @Override
        public int compare(DNS lhs, DNS rhs) {
            return lhs.getTime() - rhs.getTime();
        }
    };

    public static void write(ArrayList<DNS> arrDNS) throws IOException {
        File path = new File("output");
        path.mkdir();
        for (DNS dns : arrDNS) {
            FileWriter writer = new FileWriter(path + "\\DNS" + arrDNS.indexOf(dns) + ".txt");
            if (dns.time != -1) {
                String text = "IP адрес DNS-сервера:" + dns.ip_addr + "\tDNS-сервер: " + dns.name + " \tСреднее время отклика: " + dns.time + " ms\n";
                writer.write(text);
            }
            writer.flush();
            writer.close();
        }
    }
}