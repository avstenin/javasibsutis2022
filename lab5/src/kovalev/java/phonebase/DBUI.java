package phonebase;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBUI {
    public Connection connect() throws ClassNotFoundException, SQLException;
    public boolean insert(PhoneRecord... phoneRecords);
    public PhoneRecord[] selectAll();
    public PhoneRecord findByName(String name);
    public PhoneRecord findByPhone(String phone);
    public PhoneRecord findByEmail(String email);
}
