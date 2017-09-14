package com.alfred.api.model;

import com.alfred.api.dao.WebHookRepository;
import com.alfred.api.dto.Pusher;
import com.alfred.api.dto.Repository;
import com.alfred.api.util.TreatsValue;
import com.alfred.api.util.constants.DetailsDescription;
import com.alfred.api.util.mongo.MongoHelper;
import com.google.gson.annotations.Expose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;


public class WebHook {

    public WebHook() {
        this.isValid = true;
        this.details = " - ";
    }

    @Expose
    public Object _id;
    @Expose
    public String ref;
    @Expose
    public Pusher pusher;
    @Expose
    public Repository repository;
    @Expose
    public String event;
    @Expose
    public Integer[] dateTime;
    @Expose
    public boolean isValid;
    @Expose
    public String details;
    @Expose
    public String serverName;
    @Expose
    public Machine server;
    @Expose
    public String branch;

    public LocalDateTime localDateTime;

    private static Logger log = LoggerFactory.getLogger(WebHook.class);

    private final String REFS = "refs/heads/";

    public void setRef(String ref) {
        this.localDateTime = LocalDateTime.now();
        this.dateTime = TreatsValue.toIntegerArrayFromLocalDateTime(this.localDateTime);
        TreatsValue.nullOrEmpty(ref);
        this.ref = ref.replace(this.REFS, "");
    }

    public WebHook setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public WebHook setEvent(String event) {
        this.event = (event != null) ? event.replace("event-", "") : event;
        return this;
    }

    public WebHook setBranch(String branch) {
        this.branch = branch;
        return this;
    }

    public WebHook validEvent() {
        if (this.event == null || !this.event.equals("push"))
        {
            this.isValid = false;
            this.details = DetailsDescription.NOT_PUSH.get();
        }
        return this;
    }

    public WebHook validRepository() {
        if (this.repository == null || this.repository.name == null)
        {
            this.isValid = false;
            this.details = DetailsDescription.NOT_CONTAINS_REPOSITORY.get();
        }
        return this;
    }

    public WebHook validServerName() {
        if (this.serverName == null)
        {
            this.isValid = false;
            this.details = DetailsDescription.NOT_CONTAINS_SERVER_NAME.get();
        }
        return this;
    }

    public WebHook validMachine() {
        this.server = new Machine().fromName(this.serverName);
        if (this.server == null)
        {
            this.isValid = false;
            this.details = DetailsDescription.NOT_CONTAINS_MACHINE.get();
        }
        else if (!this.server.status)
        {
            this.isValid = false;
            this.details = DetailsDescription.NOT_CONTAINS_ACTIVE_MACHINE.get();
        }
        return this;
    }

    public WebHook validBranch() {
        try
        {
            if (!this.branch.equals(this.ref))
            {
                this.isValid = false;
                this.details = DetailsDescription.NOT_CONTAINS_VALID_BRANCH_.get();
            }
        }
        catch (Exception e)
        {
            this.isValid = false;
            this.details = DetailsDescription.NOT_CONTAINS_BRANCH_.get();
        }
        return this;
    }

    public final static String COLLECTION = "webhook";
    private final static WebHookRepository webHookRepository = new WebHookRepository(WebHook.COLLECTION, WebHook.class);

    public void save() {
        if (this.isValid)
        {
            new Build(this).save();
        }
        webHookRepository.create(this);
    }

    public WebHook tratesForResponse() {
        this._id = MongoHelper.treatsId(this._id);
        return this;
    }

    public List<WebHook> findAll() {
        return webHookRepository.findAll();
    }


}