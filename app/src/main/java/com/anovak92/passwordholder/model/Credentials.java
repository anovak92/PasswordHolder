package com.anovak92.passwordholder.model;

public class Credentials {

    private String accountName;
    private String password;

    public Credentials(String accountName, String password) {
        this.accountName = accountName;
        this.password = password;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getPassword() {
        return password;
    }

}
