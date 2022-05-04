package phonebase;

import java.sql.SQLException;

public interface DBUI {
    public void connect() throws ClassNotFoundException, SQLException;
    public void closeConnection() throws SQLException;
    public void insert(PhoneRecord... phoneRecords) throws SQLException;
    public PhoneRecord[] selectAll() throws SQLException;
    public PhoneRecord findByName(String name) throws SQLException;
    public PhoneRecord findByPhone(String phone) throws SQLException;
    public PhoneRecord findByEmail(String email) throws SQLException;
}
