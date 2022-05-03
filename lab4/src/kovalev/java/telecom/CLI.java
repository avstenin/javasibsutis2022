package telecom;

import java.util.Comparator;

public class CLI
{
    public static void main(String[] args) {
        YAMLReader reader = new YAMLReader("assets");
        FilterCtl fltr = new FilterCtl(reader.getTariffs());
        fltr.setCostLowerBound(500);
        fltr.setCostUpperBound(1000);
        fltr.setInternetLowerBound(20);
        for (Tariff tariff : fltr.get(Comparator.comparing(Tariff::getMonthRub))) {
            System.out.println(tariff.toString() + "\n");
        }
    }
}