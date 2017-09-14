package com.alfred.api.model;

import com.alfred.api.dao.BuildRepository;
import com.alfred.api.util.constants.BuildStatus;
import com.alfred.api.util.mongo.MongoHelper;
import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.util.List;

public class Build {
    @Expose
    public Object _id;
    @Expose
    public Machine server;
    @Expose
    public String application;
    @Expose
    public String branch;
    @Expose
    public Long order;
    @Expose
    public String status;
    @Expose
    public String details;
    @Expose
    public Integer[] dateTime;

    public LocalDateTime localDateTime;

    public Build() {
    }

    public Build(WebHook webHook) {
        this.application = webHook.repository.name;
        this.server = webHook.server;
        this.dateTime = webHook.dateTime;
        this.localDateTime = webHook.localDateTime;
        this.details = webHook.details;
        this.status = BuildStatus.WAITING;
        this.branch = webHook.branch;
        this.order = getOrder();
        System.out.println(this.order);
    }


    public Build tratesForResponse() {
        this._id = MongoHelper.treatsId(this._id);
        return this;
    }

    public static final String COLLECTION = "build";
    private static BuildRepository buildRepository = new BuildRepository(Build.COLLECTION, Build.class);

    private long getOrder() {
        int order = buildRepository.listOrderApplication(this.server.name, this.application).size();
        return ++order;
    }


    public void save() {
        buildRepository.create(this);
    }

    public void updateStatus(String status) {
        this.status = status;
        String id = MongoHelper.treatsId(this._id);
        this._id = null;
        buildRepository.update(id, this);
    }

    public void discart() {
        this.status = BuildStatus.DISCARDED;
        String id = MongoHelper.treatsId(this._id);
        this._id = null;
        buildRepository.update(id, this);
    }

    public List<Build> findAll() {
        return buildRepository.findAll();
    }
}
