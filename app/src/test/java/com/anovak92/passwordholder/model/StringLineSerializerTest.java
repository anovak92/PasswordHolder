package com.anovak92.passwordholder.model;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringLineSerializerTest {

    private static String testLine = "id:0;;account:test_account;;username:test_username;;passwd:test_password";
    private static Credentials testCredentials;

    @BeforeClass
    public static void createTestData() {
        testCredentials = new Credentials(0);
        testCredentials.setUsername("test_username");
        testCredentials.setPassword("test_password");
        testCredentials.setAccountname("test_account");
    }

    @Test
    public void serializeTest() {
        StringLineSerializer serializer = new StringLineSerializer();
        String actual = serializer.serialize(testCredentials);

        assertEquals(testLine, actual);
    }

    public void deserializeTest() {
        StringLineSerializer serializer = new StringLineSerializer();
        Credentials actual = serializer.deserialize(testLine);

        assertEquals(testCredentials, actual);

    }
}