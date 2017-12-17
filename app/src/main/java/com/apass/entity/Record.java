package com.apass.entity;

import java.io.Serializable;

/**
 * Created by Ivan on 12.11.2017.
 */

public class Record implements Serializable {
    private String name;
    private String login;
    private String pass;
    private String comment;

    public Record(String name, String login, String pass, String comment) {
        this.name = name;
        this.login = login;
        this.pass = pass;
        this.comment = comment;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
