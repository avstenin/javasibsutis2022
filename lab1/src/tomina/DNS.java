package tomina;

import java.util.Comparator;

public class DNS {
    private int time;
    private String name;

    public int getTime() {
        return time;
    }
    public String getName() {
        return name;
    }
    public DNS(String name) {
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
        System.out.printf("DNS: %s \tСреднее время отклика: %d\n", name, time);
    }
}
