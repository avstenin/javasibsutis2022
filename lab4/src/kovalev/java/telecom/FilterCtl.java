package telecom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterCtl {

    private double internetLowerBound = Double.NEGATIVE_INFINITY;
    private double callsLowerBound = Double.NEGATIVE_INFINITY;
    private double smsLowerBound = Double.NEGATIVE_INFINITY;
    private double costLowerBound = Double.NEGATIVE_INFINITY;
    private double costUpperBound = Double.POSITIVE_INFINITY;
    private List<String> operatorList = new ArrayList<String>();

    private List<Tariff> tariffs;

    public FilterCtl(Tariff[] t) {
        tariffs = Arrays.asList(t);
    }

    public Tariff[] get(Comparator<Tariff> comp) {
        Tariff[] c = tariffs.stream()
                            .filter(tariff -> (tariff.getCallMinutes() >= callsLowerBound) &&
                                              (tariff.getInternetGB() >= internetLowerBound) &&
                                              (tariff.getSms() >= smsLowerBound) &&
                                              (tariff.getMonthRub() >= costLowerBound) &&
                                              (tariff.getMonthRub() <= costUpperBound) &&
                                              (operatorList.isEmpty() || operatorList.contains(tariff.getOperatorName())))
                            .toArray(Tariff[]::new);
        Arrays.sort(c, comp);
        return c;
    }

    public Tariff[] get() {
        return get(Comparator.comparing(Tariff::getMonthRub));
    }
}
