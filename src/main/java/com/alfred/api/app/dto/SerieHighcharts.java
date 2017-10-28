package com.alfred.api.app.dto;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class SerieHighcharts {
    @Expose
    public String name;
    @Expose
    public Boolean colorByPoint = true;
    @Expose
    public List<DataHighcharts> data = new ArrayList<DataHighcharts>();

}
