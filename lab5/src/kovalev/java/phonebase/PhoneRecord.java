package phonebase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PhoneRecord {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    @Override public String toString() {
        return String.format("%20s | %s %s\n", "Фамилия Имя", lastName, firstName) +
               ((phone != null) ? String.format("%20s | %s\n", "Телефон", phone) : "") +
               ((email != null) ? String.format("%20s | %s\n", "Электронная почта", email) : "");
    }

    public String sqlColumns() {
        return "(FirstName, LastName, Phone, EMail)";
    }

    public String sqlValues() {
        return String.format("('%s','%s',%s,%s)", firstName,
                                                         lastName,
                                                         ((phone != null) ? "'" + phone + "'" : "NULL"),
                                                         ((email != null) ? "'" + email + "'" : "NULL") );
    }
}
