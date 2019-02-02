package com.anovak92.passwordholder.model;

import android.annotation.SuppressLint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FileCredentialsRepo implements CredentialsRepo {

    private File dataFile;

    public FileCredentialsRepo(File dataFile) {
        this.dataFile = dataFile;
    }

    @Override
    @SuppressLint("UseSparseArrays")
    public Map<Integer, Credentials> loadCredentials() throws IOException {
        Map<Integer, Credentials> loadedCredentials = new HashMap<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(dataFile))) {
            Credentials next;
            while ((next = getNext(fileReader)) != null) {
                loadedCredentials.put(next.getId(),next);
            }
            return loadedCredentials;
        }
    }

    private Credentials getNext(BufferedReader fileReader) throws IOException {
        String regex = "^(id:)(%d+)(;;account:)(\\W+)(;;passwd:)(\\W+)$";
        String line = fileReader.readLine();
        if (line != null) {

            int id = Integer.parseInt(line.replaceAll(regex,"$2"));
            String accountName = line.replaceAll(regex,"$4");
            String password = line.replaceAll(regex,"$6");

            return new Credentials(id, accountName, password);
        } else {
            return null;
        }
    }

    @Override
    public void saveCredentials(Map<Integer, Credentials> credentials) throws IOException {
        try (BufferedWriter fileWtriter = new BufferedWriter(new FileWriter(dataFile))) {
            for (Credentials credential: credentials.values()) {
                saveNext(fileWtriter, credential);
            }
        }
    }

    private void saveNext(BufferedWriter fileWriter, Credentials credential) throws IOException {
        String format = "id:%d;;account:%s;;passwd:%s";
        String saveLine = String.format(Locale.US, format,
                credential.getId(),
                credential.getAccountName(),
                credential.getPassword()
        );

        fileWriter.write(saveLine);
        fileWriter.newLine();
    }
}
