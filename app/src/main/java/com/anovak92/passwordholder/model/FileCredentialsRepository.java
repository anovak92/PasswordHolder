package com.anovak92.passwordholder.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileCredentialsRepository {
    private static File dataFile;
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

    private FileCredentialsLoader loader;
    private FileCredentialsSaver saver;

    private static List<Credentials> dataset;

    private FileCredentialsRepository(
            FileCredentialsLoader loader,
            FileCredentialsSaver saver
    ) {
        if (!dataFile.exists()) {
            try {
                boolean created = dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
