package com.anovak92.passwordholder.model;

public class Credentials {

    private int id;
    private String accountName;
    private String password;

    public Credentials(int id) {
        this(id, "", "");
    }

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Credentials) {
            Credentials cmp = (Credentials) obj;

            return this.id == cmp.id
                    && this.accountName.equals(cmp.accountName)
                    && this.password.equals(cmp.password);

        } else {
            return false;
        }
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
