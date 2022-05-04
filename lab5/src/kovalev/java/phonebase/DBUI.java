package phonebase;

import java.sql.SQLException;

public interface DBUI extends AutoCloseable {
    public void insert(PhoneRecord... phoneRecords) throws SQLException;
    public PhoneRecord[] selectAll() throws SQLException;
    public PhoneRecord[] find(String firstName, String lastName, String phone, String email) throws SQLException;
}
