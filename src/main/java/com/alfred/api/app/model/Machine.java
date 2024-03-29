package com.alfred.api.app.model;

import com.alfred.api.app.dao.MachineRepository;
import com.alfred.api.app.dto.Validation;
import com.alfred.api.useful.mongo.MongoHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Machine {
    @Expose
    public Object _id;
    @Expose
    public String name;
    @Expose
    public String ip;
    public String branch;
    @Expose
    public List<String> branchs;
    @Expose
    public Boolean status = false;
    @Expose
    public List<String> applications = new ArrayList<>();
    @JsonIgnore
    @Expose(serialize = false)
    public Validation validation = new Validation();

    public Machine() {
        this.validation.makeOK();
    }

    public Machine validForCreate() {
        Boolean promise = this.validName() &&
                this.validIP() &&
                this.validControllers() &&
                this.validBranch();
        if (!promise)
        {
            validation.fieldsError(isRequired());
        }
        return this;
    }

    public Machine validExistence() {
        if (validIP())
        {
            Object found = machineRepository.findByIP(this.ip);
            if (found != null)
            {
                this.validation.alreadyExists(this.ip);
            }
        }
        return this;
    }

    public Machine validForUpdate() {
        Boolean promise = this.validId() &&
                this.validName() &&
                this.validIP() &&
                this.validControllers();
        if (!promise)
        {
            validation.fieldsError(isRequired() + "and id");
        }
        return this;
    }

    private Boolean validId() {
        return this._id != null;
    }

    private Boolean validIP() {
        return this.ip != null && !this.ip.isEmpty();
    }

    private Boolean validName() {
        return this.name != null && !this.name.isEmpty();
    }

    private Boolean validBranch() {
        return this.branch != null && !this.branch.isEmpty();
    }

    private Boolean validControllers() {
        return this.status != null;
    }

    private String isRequired() {
        return "<name, ip, status>";
    }

    public Machine treatsForResponse() {
        this._id = MongoHelper.treatsId(this._id);
        this.branch = this.branchs.toString().replace("[", "").replace("]", "");
        return this;
    }

    public final static String COLLECTION = "machine";
    private MachineRepository machineRepository = new MachineRepository(COLLECTION, this.getClass());

    public Machine create() {
        if (validation.status)
        {
            treatsForSave();
            machineRepository.create(this);
        }
        return this;
    }

    private void treatsForSave() {
        this.branch = this.branch.replace(" ", "").trim();
        this.branchs = Arrays.asList(this.branch.split(","));
    }

    public Machine update() {
        if (validation.status)
        {
            treatsForSave();
            machineRepository.update(this._id, this);
        }
        return this;
    }

    public Machine delete() {
        if (validId())
        {
            machineRepository.deleteOne(this._id);
        }
        return this;
    }

    public List<Machine> findAll() {

        return machineRepository.findAll();
    }

    public boolean getStatus() {
        return this.status;
    }
}
