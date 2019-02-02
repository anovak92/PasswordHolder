package com.anovak92.passwordholder;


public interface CredentialsView {

    enum Mode {
        CREATE,
        VIEW,
        EDIT
    }

    void setMode(Mode mode);

    Mode getCurrentMode();

    void save();

}
