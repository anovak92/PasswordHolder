package com.anovak92.passwordholder.model;

public class Credentials {

    private int id;
    private String accountName;
    private String password;

    public Credentials(int id, String accountName, String password) {
        this.id = id;
        this.accountName = accountName;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getPassword() {
        return password;
    }

}
