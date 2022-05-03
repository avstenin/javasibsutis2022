package telecom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tariff {
    private String operatorName;
    private String tariffName;
    private Integer monthRub;
    private Double internetGB;
    private Integer callMinutes;
    private Integer sms;

    @Override public String toString() {
        return String.format("Имя оператора: %s\n", operatorName) +
               String.format("Название тарифа: '%s'\n", tariffName) +
               String.format("Стоимость: %d руб./мес.\n", monthRub) +
               String.format("- Интернет (в ГБ): %d ГБ\n", internetGB) +
               String.format("- Звонки: %d мин.\n", callMinutes) +
               String.format("- СМС: %d сообщений\n", sms);
    }
}
