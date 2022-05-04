package phonebase;

import java.sql.SQLException;

public interface DBUI extends AutoCloseable {
    public void insert(PhoneRecord... phoneRecords) throws SQLException;
    public PhoneRecord[] selectAll() throws SQLException;
    public PhoneRecord findByName(String name) throws SQLException;
    public PhoneRecord findByPhone(String phone) throws SQLException;
    public PhoneRecord findByEmail(String email) throws SQLException;
}
