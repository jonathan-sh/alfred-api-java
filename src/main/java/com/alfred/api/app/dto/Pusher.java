package com.alfred.api.app.dto;

import com.alfred.api.useful.treats.TreatsValue;
import com.google.gson.annotations.Expose;

public class Pusher {
    @Expose
    public String name;
    @Expose
    public String email;

    public void setName(String name) {
        TreatsValue.nullOrEmpty(name);
        this.name = name;
    }

    public void setEmail(String email) {
        TreatsValue.nullOrEmpty(email);
        this.email = email;
    }
}
