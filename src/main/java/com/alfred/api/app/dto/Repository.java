package com.alfred.api.app.dto;

import com.alfred.api.useful.treats.TreatsValue;
import com.google.gson.annotations.Expose;

public class Repository {
    @Expose
    public Long id;
    @Expose
    public String name;
    @Expose
    public String full_name;

    public void setName(String name) {
        TreatsValue.nullOrEmpty(name);
        this.name = name;
    }
}
