package com.anovak92.passwordholder.model;

import java.io.IOException;
import java.util.Map;

public interface CredentialsRepo {

    Map<Integer, Credentials> loadCredentials() throws IOException;

    void saveCredentials(Map<Integer, Credentials> credentials) throws IOException;

}
