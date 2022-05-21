package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class PropertiesUtilTest {

    @Test
    public void get() {
        assertEquals("jdbc:postgresql://localhost:5432/phone_repository", PropertiesUtil.get("db.url"));
        assertEquals("postgres", PropertiesUtil.get("db.username"));
        assertEquals("qwerty", PropertiesUtil.get("db.password"));
    }
}