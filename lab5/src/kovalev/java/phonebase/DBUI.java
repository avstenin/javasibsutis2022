package phonebase;

import java.sql.SQLException;

public interface DBUI {
    public void connect() throws ClassNotFoundException, SQLException;
    public void closeConnection() throws SQLException;
    public boolean insert(PhoneRecord... phoneRecords);
    public PhoneRecord[] selectAll();
    public PhoneRecord findByName(String name);
    public PhoneRecord findByPhone(String phone);
    public PhoneRecord findByEmail(String email);
}
