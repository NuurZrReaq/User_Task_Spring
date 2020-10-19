package com.tasks.usertaskweb.Models;

public class AuthenticationRequest {

    private int id;
    private String password;

    public AuthenticationRequest(int id, String password) {
        this.id = id;
        this.password = password;
    }

    public AuthenticationRequest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
