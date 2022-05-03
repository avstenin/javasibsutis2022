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
    private double monthRub;
    private double internetGB;
    private double callMinutes;
    private double sms;

    @Override public String toString() {
        return String.format("Имя оператора: %s.\n", operatorName) +
               String.format("Название тарифа: '%s'.\n", tariffName) +
               String.format("Стоимость: %.0f руб./мес.\n", monthRub) +
               (internetGB != Double.POSITIVE_INFINITY ? String.format("- Интернет: %.0f ГБ.\n", internetGB) : String.format("- Интернет: безлимит.\n")) +
               (callMinutes != Double.NEGATIVE_INFINITY ? String.format("- Звонки: %.0f мин.\n", callMinutes) : String.format("- Звонки: не включены.\n")) +
               (sms != Double.NEGATIVE_INFINITY ? String.format("- СМС: %.0f сообщений.\n", sms) : String.format("- СМС: не включены.\n"));
    }
}
