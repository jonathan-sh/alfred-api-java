package com.alfred.api.integration.slack.message;

public class Field
{
   private String title ="";
   private String value ="";
   private Boolean sort = false;

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public Boolean getSort() {
        return sort;
    }

    protected Field(String title, String value) {
        this.title = title;
        this.value = value;
    }
}
