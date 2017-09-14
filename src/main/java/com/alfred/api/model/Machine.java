package com.alfred.api.model;

import com.alfred.api.dao.MachineRepository;
import com.alfred.api.dto.Validation;
import com.alfred.api.util.mongo.MongoHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

import java.util.List;

public class Machine {
    @Expose
    public Object _id;
    @Expose
    public String name;
    @Expose
    public String ip;
    @Expose
    public Boolean status;

    @JsonIgnore
    @Expose(serialize = false)
    public Validation validation = new Validation();

    public Machine() {
        this.validation.makeOK();
    }


    public Machine validForCreate() {
        Boolean promise = this.validName() &&
                this.validIP() &&
                this.validControllers();
        if (!promise)
        {
            validation.fieldsError(isRequered());
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
            validation.fieldsError(isRequered() + "and id");
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

    private Boolean validControllers() {
        return this.status != null;
    }

    private String isRequered() {
        return "<name, ip, status>";
    }

    public Machine tratesForResponse() {
        this._id = MongoHelper.treatsId(this._id);
        return this;
    }

    public final static String COLLECTION = "machine";
    private MachineRepository machineRepository = new MachineRepository(COLLECTION, this.getClass());

    public Machine create() {
        if (validation.status)
        {
            machineRepository.create(this);
        }
        return this;
    }

    public Machine update() {
        if (validation.status)
        {
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

    public Machine fromName(String serverName) {
        return machineRepository.findByName(serverName);
    }
}
