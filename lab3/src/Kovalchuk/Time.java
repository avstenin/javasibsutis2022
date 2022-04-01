package Kovalchuk;

public class Time implements Comparable<Time> {
    String address;
    int time;

    @Override
    public int compareTo(Time o) {
        return this.time > o.time ? 1 : (this.time == o.time ? 0 : -1);
    }
}
