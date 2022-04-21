package dns;

public class PingResult implements Comparable<PingResult> {
    private String ip;
    private Double time;

    public PingResult(String ip, Double time) {
        this.ip = ip;
        this.time = time;
    }

    @Override
    public String toString() {
        return String.format("%-16s | %.2f мс", ip, time);
    }

    @Override
    public int compareTo(PingResult o) {
        return this.time.compareTo(o.time);
    }

    public Double getTime() {
        return time;
    }

    public String getIp() {
        return ip;
    }
}