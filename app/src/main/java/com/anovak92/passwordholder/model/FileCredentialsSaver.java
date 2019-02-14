package com.anovak92.passwordholder.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

class FileCredentialsSaver {
    private File sourceFile;
    private StringLineSerializer serializer;

    FileCredentialsSaver(File sourceFile, StringLineSerializer serializer) {
        this.sourceFile = sourceFile;
        this.serializer = serializer;
    }

    void save(List<Credentials> credentialsList) throws IOException {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(sourceFile))) {
            for (Credentials credentials: credentialsList) {
                fileWriter.write(serializer.serialize(credentials));
                fileWriter.newLine();
            }
        }
    }
}
