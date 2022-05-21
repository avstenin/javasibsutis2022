package dao;

import dto.PhoneDirectoryFilter;
import entity.PhoneDirectory;
import org.junit.Before;
import org.junit.Test;
import util.ConnectionManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class PhoneDirectoryDaoTest {

    @Before
    public void setUp() throws Exception {
        String sql = """
                DROP TABLE IF EXISTS phone_directory;
                CREATE TABLE IF NOT EXISTS phone_directory
                (
                    id SERIAL PRIMARY KEY ,
                    first_name VARCHAR(32) NOT NULL ,
                    last_name VARCHAR(32) NOT NULL ,
                    phone_number CHAR(10) UNIQUE ,
                    email VARCHAR(64) UNIQUE
                );
                INSERT INTO phone_directory(first_name, last_name, phone_number, email)
                VALUES ('Petr', 'Petrov', '9749370496', NULL),
                       ('Petr', 'Ivanov', NULL, 'arara@gmail.com'),
                       ('Sveta', 'Svetikova', '9756742500', 'svetik@yandex.ru');
                """;
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(sql)) {
            statement.execute();
        }
    }

    @Test
    public void findAll() {
        List<PhoneDirectory> phoneDirectories = PhoneDirectoryDao.getInstance().findAll();
        List<PhoneDirectory> expectedPhoneDirectories = new LinkedList<>();
        expectedPhoneDirectories.add(new PhoneDirectory(1, "Petr", "Petrov",
                "9749370496", null));
        expectedPhoneDirectories.add(new PhoneDirectory(2, "Petr", "Ivanov",
                null, "arara@gmail.com"));
        expectedPhoneDirectories.add(new PhoneDirectory(3, "Sveta", "Svetikova",
                "9756742500", "svetik@yandex.ru"));
        assertEquals(expectedPhoneDirectories, phoneDirectories);

        phoneDirectories = PhoneDirectoryDao.getInstance().findAll(new PhoneDirectoryFilter("Petr", null,
                null, null));
        expectedPhoneDirectories = new LinkedList<>();
        expectedPhoneDirectories.add(new PhoneDirectory(1, "Petr", "Petrov",
                "9749370496", null));
        expectedPhoneDirectories.add(new PhoneDirectory(2, "Petr", "Ivanov",
                null, "arara@gmail.com"));
        assertEquals(expectedPhoneDirectories, phoneDirectories);
    }

    @Test
    public void findById() {
        Optional<PhoneDirectory> phoneDirectory = PhoneDirectoryDao.getInstance().findById(3);
        Optional<PhoneDirectory> expectedPhoneDirectory = Optional.of(new PhoneDirectory(3, "Sveta",
                "Svetikova", "9756742500", "svetik@yandex.ru"));
        assertEquals(expectedPhoneDirectory, phoneDirectory);

        phoneDirectory = PhoneDirectoryDao.getInstance().findById(100);
        assertEquals(Optional.empty(), phoneDirectory);
    }

    @Test
    public void update() {
        int updateCount = PhoneDirectoryDao.getInstance().update(new PhoneDirectory(2,
                "Ivan", "Petrov", "8005553535", "bank@rambler.ru"));
        assertEquals(1, updateCount);

        updateCount = PhoneDirectoryDao.getInstance().update(new PhoneDirectory(100,
                "Ivan", "Petrov", "8005553535", "bank@rambler.ru"));
        assertEquals(0, updateCount);
    }

    @Test
    public void save() {
        List<PhoneDirectory> phoneDirectories = new ArrayList<>();
        phoneDirectories.add(new PhoneDirectory(null,
                "Dasha", "Dashina", null, "rabota@mail.ru"));
        phoneDirectories.add(new PhoneDirectory(1,
                "Misha", "Solodov", null, null));
        phoneDirectories = PhoneDirectoryDao.getInstance().save(phoneDirectories);

        List<PhoneDirectory> expectedPhoneDirectories = new ArrayList<>();
        expectedPhoneDirectories.add(new PhoneDirectory(4,
                "Dasha", "Dashina", null, "rabota@mail.ru"));
        expectedPhoneDirectories.add(new PhoneDirectory(5,
                "Misha", "Solodov", null, null));
        assertEquals(expectedPhoneDirectories, phoneDirectories);
    }

    @Test
    public void delete() {
        boolean isDeleted = PhoneDirectoryDao.getInstance().delete(1);
        assertTrue(isDeleted);

        isDeleted = PhoneDirectoryDao.getInstance().delete(100);
        assertFalse(isDeleted);
    }
}