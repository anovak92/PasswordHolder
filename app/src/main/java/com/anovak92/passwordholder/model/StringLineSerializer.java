package com.anovak92.passwordholder.model;

import java.util.Locale;

final class StringLineSerializer {

    Credentials deserialize(String line) {
        String regex = "^(id:)(\\d+)(;;account:)(\\S+)(;;username:)(\\S+)(;;passwd:)(\\S+)";

        int id = Integer.parseInt(line.replaceAll(regex,"$2"));
        String accountname = line.replaceAll(regex,"$4");
        String username = line.replaceAll(regex,"$6");
        String password = line.replaceAll(regex,"$8");

        Credentials result = new Credentials(id);
        result.setAccountname(accountname);
        result.setUsername(username);
        result.setPassword(password);
        return result;
    }

    String serialize(Credentials credential) {
        String format = "id:%d;;account:%s;;username:%s;;passwd:%s";
        return String.format(Locale.US, format,
                credential.getId(),
                credential.getAccountname(),
                credential.getUsername(),
                credential.getPassword()
        );
    }
}
