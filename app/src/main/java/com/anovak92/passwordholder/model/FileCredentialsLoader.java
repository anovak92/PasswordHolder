package com.anovak92.passwordholder.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class FileCredentialsLoader {
    private File sourceFile;
    private StringLineSerializer serializer;

    FileCredentialsLoader(File sourceFile, StringLineSerializer serializer) {
        this.sourceFile = sourceFile;
        this.serializer = serializer;
    }

    List<Credentials> load() throws IOException {
        List<Credentials> loadedCredentials = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(sourceFile))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                loadedCredentials.add(serializer.deserialize(line));
            }
            return loadedCredentials;
        }
    }
}
