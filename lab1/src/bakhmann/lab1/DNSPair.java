package lab1;


public class DNSPair implements java.lang.Comparable<DNSPair> {
    private int Res;
    private String Name;
    public DNSPair(String name, int res){
        Res = res;
        Name = name;
    }
    public int getRes(){
        return this.Res;
    }

    public String getName(){
        return this.Name;
    }

    @Override
    public int compareTo(DNSPair o) {
        return Integer.compare(this.getRes(), o.getRes());
    }
    
}