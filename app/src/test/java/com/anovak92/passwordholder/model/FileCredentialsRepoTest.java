package com.anovak92.passwordholder.model;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FileCredentialsRepoTest {

    private final static String DATA_FILE_NAME = "test.df";

    private static File dataFile = null;
    private static Map<Integer, Credentials> testDataset;
    private static List<Credentials> listDataset;

    @BeforeClass
    public static void createDataFile() throws IOException {
        dataFile = new File(DATA_FILE_NAME);
        dataFile.createNewFile();
    }

    @AfterClass
    public static void deleteDataFile(){
        dataFile.delete();
    }

    @BeforeClass
    public static void createTestDataset(){
        testDataset = new HashMap<>(5);
        listDataset = new ArrayList<>(5);

        testDataset.put(0, createTestCredentials(0,"testacc_1","test_username1","test_password_1"));
        testDataset.put(1, createTestCredentials(1,"testacc_2","test_username2","test_password_2"));
        testDataset.put(2, createTestCredentials(2,"testacc_3","test_username3","test_password_3"));
        testDataset.put(3, createTestCredentials(3,"testacc_4","test_username4","test_password_4"));
        testDataset.put(4, createTestCredentials(4,"testacc_5","test_username5","test_password_5"));

        listDataset.add(createTestCredentials(0,"testacc_1","test_username1","test_password_1"));
        listDataset.add(createTestCredentials(1,"testacc_2","test_username2","test_password_2"));
        listDataset.add(createTestCredentials(2,"testacc_3","test_username3","test_password_3"));
        listDataset.add(createTestCredentials(3,"testacc_4","test_username4","test_password_4"));
        listDataset.add(createTestCredentials(4,"testacc_5","test_username5","test_password_5"));
    }

    private static Credentials createTestCredentials(int id, String acc, String user, String passwd){
        Credentials result = new Credentials(id);
        result.setAccountname(acc);
        result.setUsername(user);
        result.setPassword(passwd);
        return result;
    }

    @Test
    public void fullTest() throws IOException {
        CredentialsRepo credentialsRepo = new FileCredentialsRepo(dataFile);

        credentialsRepo.saveCredentials(testDataset);

        Map<Integer, Credentials> loadedCredentials = credentialsRepo.loadCredentials();

        assertEquals(testDataset, loadedCredentials);

    }

    @Test
    public void saveLoadListTest() throws IOException {
        CredentialsRepo credentialsRepo = new FileCredentialsRepo(dataFile);

        credentialsRepo.saveCredentialsList(listDataset);

        List<Credentials> loaded = credentialsRepo.loadCredentialsList();

        assertEquals(listDataset, loaded);

    }

    @Test
    public void parseLineTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        FileCredentialsRepo credentialsRepo = new FileCredentialsRepo(dataFile);

        Method parseLine = credentialsRepo.getClass().getDeclaredMethod("parseLine", String.class);
        parseLine.setAccessible(true);

        Credentials result = (Credentials) parseLine
                .invoke(credentialsRepo, "id:0;;account:testacc_1;;username:test_username1;;passwd:test_password_1");

        System.out.println("[FileCredentialsRepoTest:getLineTest]");
        System.out.println("Expected " + "0;testacc_1;test_username1;test_password_1");
        System.out.println("Actual " + String.format(Locale.US,"%d;%s;%s;%s",
                result.getId(),
                result.getAccountname(),
                result.getUsername(),
                result.getPassword()
        ));

        assertEquals(0,result.getId());
        assertEquals("testacc_1",result.getAccountname());
        assertEquals("test_username1",result.getUsername());
        assertEquals("test_password_1",result.getPassword());

    }

    @Test
    public void getLineTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        FileCredentialsRepo credentialsRepo = new FileCredentialsRepo(dataFile);

        Method getLineMethod = credentialsRepo.getClass().getDeclaredMethod("getLine", Credentials.class);
        getLineMethod.setAccessible(true);

        String expected = "id:0;;account:testacc_1;;username:test_username1;;passwd:test_password_1";

        Credentials credential = createTestCredentials(0,"testacc_1","test_username1","test_password_1");
        String result = (String) getLineMethod
                .invoke(credentialsRepo, credential);

        System.out.println("[FileCredentialsRepoTest:getLineTest]");
        System.out.println("Expected " + expected);
        System.out.println("Actual " + result);

        assertEquals(expected, result);

    }
}