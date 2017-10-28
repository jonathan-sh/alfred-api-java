package com.alfred.api.app.dto;

import com.google.gson.annotations.Expose;

public class DataHighcharts {
    public DataHighcharts() {
    }

    public DataHighcharts(String name, Double y) {
        this.name = name;
        this.y = y;
    }

    @Expose
    public String name;
    @Expose
    public Double y;
    @Expose
    public boolean selected = false;
    @Expose
    public boolean sliced = false;

    public void makeSelected(){
        this.selected = true;
        this.sliced = true;
    }


}
