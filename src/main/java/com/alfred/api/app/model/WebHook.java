package com.alfred.api.app.model;

import com.alfred.api.app.dao.ApplicationRepository;
import com.alfred.api.app.dao.MachineRepository;
import com.alfred.api.app.dao.WebHookRepository;
import com.alfred.api.app.dto.HeadCommit;
import com.alfred.api.app.dto.Pusher;
import com.alfred.api.app.dto.Repository;
import com.alfred.api.useful.constants.DetailsDescription;
import com.alfred.api.useful.mongo.MongoHelper;
import com.alfred.api.useful.treats.DateTimeUTC;
import com.alfred.api.useful.treats.TreatsValue;
import com.google.gson.annotations.Expose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public String after;
    @Expose
    public Pusher pusher;
    @Expose
    public HeadCommit head_commit;
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
    public Machine machine;
    @Expose
    public Application application;
    @Expose
    public String branch;

    public long order;
    @Expose
    public List<Machine> machines = new ArrayList<>();

    public LocalDateTime localDateTime;

    private static Logger log = LoggerFactory.getLogger(WebHook.class);

    private final String REFS = "refs/heads/";

    public void setRef(String ref) {
        this.localDateTime = DateTimeUTC.now();
        this.dateTime = TreatsValue.toIntegerArrayFromLocalDateTime(this.localDateTime);
        TreatsValue.nullOrEmpty(ref);
        this.ref = ref.replace(this.REFS, "");
        this.branch = ref.replace(this.REFS, "");
    }

    public WebHook setEvent(String event) {
        this.event = (event != null) ? event.replace("event-", "") : event;
        return this;
    }

    public WebHook validEvent() {

        if (this.isValid)
        {
            if (this.event == null || !this.event.equals("push"))
            {
                this.isValid = false;
                this.details = DetailsDescription.NOT_PUSH.get();
            }
        }
        return this;
    }

    public WebHook validRepository() {
        if (this.isValid)
        {
            if (this.repository == null || this.repository.name == null)
            {
                this.isValid = false;
                this.details = DetailsDescription.NOT_CONTAINS_REPOSITORY.get();
            }
        }
        return this;
    }

    public WebHook validApplication() {

        if (this.isValid)
        {
            this.application = applicationRepository.findByName(this.repository.name);
            if (this.application == null)
            {
                this.isValid = false;
                this.details = DetailsDescription.NOT_CONTAINS_VALID_APPLICATION.get();
            }
        }

        return this;
    }

    public WebHook validMachine() {

        if (this.isValid)
        {
            this.machines = machineRepository
                           .findByApplicationIdAndBranchName(MongoHelper.treatsId(application._id),this.branch);
            if (machines.size() == 0)
            {
                this.isValid = false;
                this.details = DetailsDescription.NOT_CONTAINS_VALID_MACHINE.get();
            }
        }

        return this;
    }

    private final static String COLLECTION = "webhook";
    private final static WebHookRepository webHookRepository = new WebHookRepository(WebHook.COLLECTION, WebHook.class);
    private final static ApplicationRepository applicationRepository = new ApplicationRepository(Application.COLLECTION, Application.class);
    private MachineRepository machineRepository = new MachineRepository(Machine.COLLECTION, Machine.class);

    public void save() {
        if (this.isValid)
        {
            this.machines.forEach(server ->
            {
                this.machine = server;
                Build build = new Build(this);
                build.save();
                build.sendSlackStatus();
            });

        }
        webHookRepository.create(this);
    }

    public WebHook treatsForResponse() {
        this._id = MongoHelper.treatsId(this._id);
        this.order = TreatsValue.getLogFromArrayDateTime(this.dateTime);
        return this;
    }

    public List<WebHook> findAll() {
        return webHookRepository.findAll();
    }

}
