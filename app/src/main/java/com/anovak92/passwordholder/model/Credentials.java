package com.anovak92.passwordholder.model;

public class Credentials {

    private int id;
    private String accountname;
    private String username;
    private String password;

    public Credentials(int id) {
        this.id = id;
        accountname = "";
        username = "";
        password = "";
    }

    public int getId() {
        return id;
    }

    public String getAccountname() {
        return accountname;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Credentials) {
            Credentials cmp = (Credentials) obj;

            return this.id == cmp.id
                    && this.accountname.equals(cmp.accountname)
                    && this.password.equals(cmp.password);

        } else {
            return false;
        }
    }
}
