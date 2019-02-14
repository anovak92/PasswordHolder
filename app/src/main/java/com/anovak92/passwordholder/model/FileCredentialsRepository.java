package com.anovak92.passwordholder.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileCredentialsRepository {

    private static File dataFile;
    private static List<Credentials> dataset;

    private static FileCredentialsRepository instance = null;

    public static FileCredentialsRepository getInstance() {
        if (dataFile == null) {
            throw new IllegalArgumentException("Please set file for credentials storage");
        }

        if (instance == null) {
            StringLineSerializer serializer = new StringLineSerializer();
            instance = new FileCredentialsRepository(
                    new FileCredentialsLoader(dataFile, serializer),
                    new FileCredentialsSaver(dataFile, serializer)
            );
        }
        return instance;
    }

    public static void setFile(File file) throws IOException {
        dataFile = file;
        if (!dataFile.exists()) {
            boolean created = dataFile.createNewFile();
        }
        dataset = null;
    }

    private FileCredentialsLoader loader;
    private FileCredentialsSaver saver;

    private FileCredentialsRepository(
            FileCredentialsLoader loader,
            FileCredentialsSaver saver
    ) {
        this.loader = loader;
        this.saver = saver;
    }

    public List<Credentials> getDataset() {
        return dataset;
    }

    public void saveDataset() throws IOException {
        dataset = loader.load();
    }

    public void loadDataset() throws IOException {
        saver.save(dataset);
    }
}
