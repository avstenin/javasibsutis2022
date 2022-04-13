
class DNS implements Comparable<DNS> {
    private String ip;
    private String name;
    private Double connectionTime;

    public DNS(String ip) {
        this.ip = ip;
    }

    @Override
    public int hashCode() {
        return ip.hashCode();
    }

    @Override
    public String toString() {
        if (name.isEmpty())
            return ip + " " + connectionTime + "ms";
        else
            return name + " (" + ip + ") " + connectionTime + "ms";
    }

    @Override
    public int compareTo(DNS o) {
        return this.connectionTime.compareTo(o.connectionTime);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(Double connectionTime) {
        this.connectionTime = connectionTime;
    }
}
