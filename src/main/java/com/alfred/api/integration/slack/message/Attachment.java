package com.alfred.api.integration.slack.message;

import com.alfred.api.app.model.Build;
import com.alfred.api.useful.AlfredConfig;
import com.alfred.api.useful.constants.BuildStatus;
import com.alfred.api.useful.mongo.MongoHelper;

import java.util.ArrayList;
import java.util.List;

public class Attachment
{
    private String color = "";
    private String pretext = "";
    private String title = "";
    private String title_link = "";
    private String author_name="";
    private String author_link="";
    private List<Field> fields = new ArrayList<>();

    protected List<Attachment> mountAttachments(Build build)
    {
        List<Attachment> list = new ArrayList<>();
        try
        {
            setColor(build);
            setTitle(build);
            setTitleLink(build);
            setAuthor(build);
            setAuthorLink(build);
            setFields(build);
            list.add(this);
        }
        catch (Exception e)
        {
            System.out.println("fail");
        }
        return list;
    }

    private void setColor(Build build) {

        switch (build.status)
        {
            case BuildStatus.DISCARDED:
                color="#ee7c12";
                break;
            case BuildStatus.WAITING:
                color="#ffcf00";
                break;
            case BuildStatus.IN_PROGRESS:
                color="#125dee";
                break;
            case BuildStatus.FAIL:
                color="#ff2930";
                break;
            case BuildStatus.SUCCESS:
                color="#2ea664";
                break;
            default:
                color="#000000";
                break;
        }

    }

    private void setTitle(Build build) {
        this.title = build.status;
    }

    private void setTitleLink(Build build) {
        title = build.status;

        if (MongoHelper.treatsId(build._id) != null)
        {
            title_link = AlfredConfig.CLIENT_URL + "/builds/";
        }
        else
        {
            title_link = AlfredConfig.CLIENT_URL + "/builds/" + MongoHelper.treatsId(build._id);
        }

    }

    private void setAuthor(Build build) {
       if (build.commitHash != null)
       {
           this.author_name ="commit : " + build.commitHash ;
       }

    }

    private void setAuthorLink(Build build) {
        if (build.commitUrl != null)
        {
            this.author_link = build.commitUrl;
        }
    }

    private void setFields(Build build) {
        if (build.application!=null)
        {
            fields.add(new Field("application",build.application.name + " | " + build.branch));
        }

        if (build.machine!=null)
        {
            fields.add(new Field("machine",build.machine.name +" | "+ build.machine.ip));
        }
        if (build.time!=null)
        {
            fields.add(new Field("time",String.valueOf(build.time) + " seconds"));
        }

    }

    public String getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle_link() {
        return title_link;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public String getAuthor_link() {
        return author_link;
    }

    public List<Field> getFields() {
        return fields;
    }
}
