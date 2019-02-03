package com.anovak92.passwordholder.model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CredentialsRepo {

    @Deprecated
    Map<Integer, Credentials> loadCredentials() throws IOException;

    List<Credentials> loadCredentialsList() throws IOException;

    @Deprecated
    void saveCredentials(Map<Integer, Credentials> credentials) throws IOException;

    void saveCredentialsList(List<Credentials> credentials) throws IOException;

}
