package tomina;

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
    public String getIp_addr() { return ip_addr; }

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

    public void print(){
        if(time!=-1)
        System.out.printf("IP адрес DNS-сервера: %s \tDNS-сервер: %s \tСреднее время отклика: %d ms\n", ip_addr, name, time);
    }


}
