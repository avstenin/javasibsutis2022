package telecom;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
class YAMLTariff {
    private String name;
    private Integer monthRub;
    private Integer internetGB;
    private Integer callMinutes;
    private Integer sms;
}

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class YAMLOperator {
    private String operatorName;
    private List<YAMLTariff> tariffs;

    public Tariff[] toTariffs() 
    {
        return tariffs.stream()
                      .map(tariff -> new Tariff(operatorName,
                                                tariff.getName(),
                                                tariff.getMonthRub(),
                                                tariff.getInternetGB(),
                                                tariff.getCallMinutes(),
                                                tariff.getSms()))
                      .toArray(Tariff[]::new);
    }
}
