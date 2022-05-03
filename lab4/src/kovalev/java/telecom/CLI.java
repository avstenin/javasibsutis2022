package telecom;

public class CLI
{
    public static void main(String[] args) {
        YAMLReader reader = new YAMLReader("assets");
        for (Tariff tariff : reader.getTariffs()) {
            System.out.println(tariff.toString() + "\n");
        }
    }
}