package com.anovak92.passwordholder.model;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            String line;
            Credentials tmp;
            while ((line = fileReader.readLine()) != null) {
                tmp = parseLine(line);
                loadedCredentials.put(tmp.getId(), tmp);
            }
            return loadedCredentials;
        }
    }

    @Override
    public List<Credentials> loadCredentialsList() throws IOException {
        List<Credentials> loadedCredentials = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            Credentials tmp;
            while ((line = fileReader.readLine()) != null) {
                tmp = parseLine(line);
                loadedCredentials.add(tmp);
            }
            return loadedCredentials;
        }
    }


    private Credentials parseLine(@NonNull String line) {
        String regex = "^(id:)(\\d+)(;;account:)(\\S+)(;;username:)(\\S+)(;;passwd:)(\\S+)";

        int id = Integer.parseInt(line.replaceAll(regex,"$2"));
        String accountname = line.replaceAll(regex,"$4");
        String username = line.replaceAll(regex,"$6");
        String password = line.replaceAll(regex,"$8");

        Credentials result = new Credentials(id);
        result.setAccountname(accountname);
        result.setUsername(username);
        result.setPassword(password);
        return result;
    }

    @Override
    public void saveCredentials(Map<Integer, Credentials> credentials) throws IOException {
        try (BufferedWriter fileWtriter = new BufferedWriter(new FileWriter(dataFile))) {
            for (Credentials credential: credentials.values()) {
                saveNext(fileWtriter, credential);
            }
        }
    }

    @Override
    public void saveCredentialsList(List<Credentials> credentials) throws IOException {
        try (BufferedWriter fileWtriter = new BufferedWriter(new FileWriter(dataFile))) {
            for (Credentials credential: credentials) {
                saveNext(fileWtriter, credential);
            }
        }
    }

    private void saveNext(BufferedWriter fileWriter, Credentials credential) throws IOException {

        String saveLine = getLine(credential);

        fileWriter.write(saveLine);
        fileWriter.newLine();
    }

    private String getLine(Credentials credential) {
        String format = "id:%d;;account:%s;;username:%s;;passwd:%s";
        return String.format(Locale.US, format,
                credential.getId(),
                credential.getAccountname(),
                credential.getUsername(),
                credential.getPassword()
        );
    }
}
