package com.alfred.api.app.model;

import com.alfred.api.app.dao.ApplicationRepository;
import com.alfred.api.app.dto.Validation;
import com.alfred.api.util.mongo.MongoHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

public class Application {

    @Expose
    public Object _id;
    @Expose
    public String name;
    @Expose
    public String type;
    @Expose
    public Boolean status;

    @JsonIgnore
    @Expose(serialize = false)
    public Validation validation = new Validation();

    public Application() {
        this.validation.makeOK();
    }

    public boolean getStatus() {
        return this.status;
    }

    public Application validForCreate(){
        if (this.name ==null || this.type ==null)
        {
            this.status = false;
            this.validation.noContains("< name or type >");
        }

        return this;
    }

    public Application validForUpdate() {
        if(this._id == null)
        {
            validation.fieldsError("< _id >");
        }
        return this;
    }

    public static String COLLECTION = "application";
    private static ApplicationRepository applicationRepository = new ApplicationRepository(Application.COLLECTION, Application.class);


    public Application create() {
        if (validation.status)
        {
            applicationRepository.create(this);
        }
        return this;
    }

    public Object findAll() {
        return applicationRepository.findAll();
    }

    public Application validExistence() {
        Application promise = applicationRepository.findByName(this.name);
        if (promise!=null)
        {
            validation.alreadyExists(this.name);
        }

        return this;
    }

    public Application tratesForResponse() {
        this._id = MongoHelper.treatsId(this._id);
        return this;
    }

    public Application update() {
        if (validation.status)
        {
            String id = MongoHelper.treatsId(this._id);
            this._id = null;
            applicationRepository.update(id, this);
        }
        return this;
    }

    public Application delete() {
        applicationRepository.deleteOne(this._id);
        return this;
    }
}
