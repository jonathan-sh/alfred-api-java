package com.alfred.api.app.model;

import com.alfred.api.app.dao.BuildRepository;
import com.alfred.api.app.dto.DataHighcharts;
import com.alfred.api.app.dto.SerieHighcharts;
import com.alfred.api.integration.slack.message.SlackMessage;
import com.alfred.api.useful.constants.BuildStatus;
import com.alfred.api.useful.mongo.MongoHelper;
import com.alfred.api.useful.treats.DateTimeUTC;
import com.alfred.api.useful.treats.TreatsValue;
import com.google.gson.annotations.Expose;

import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Build {
    @Expose
    public Object _id;
    public String old_id;
    @Expose
    public Machine machine;
    @Expose
    public Application application;
    @Expose
    public String branch;
    @Expose
    public String log;
    @Expose
    public String commit;
    @Expose
    public String commitUrl;
    @Expose
    public String commitHash;
    @Expose
    public Long order;
    @Expose
    public Long time;
    @Expose
    public String status;
    @Expose
    public String details;
    @Expose
    public Integer[] start;
    @Expose
    public Integer[] end;

    public LocalDateTime localDateTime;

    public Build() {
    }

    public Build(WebHook webHook) {
        this.application = webHook.application;
        this.machine = webHook.machine;
        this.start = webHook.dateTime;
        this.localDateTime = webHook.localDateTime;
        this.details = webHook.details;
        this.status = BuildStatus.WAITING;
        this.branch = webHook.branch;
        this.commit = webHook.head_commit.message;
        this.commitUrl = webHook.head_commit.url;
        this.commitHash = webHook.after;
        this.order = getOrder();
    }

    public Build treatsForResponse() {
        this._id = MongoHelper.treatsId(this._id);
        this.order = TreatsValue.getLogFromArrayDateTime(this.start);
        return this;
    }

    public static final String COLLECTION = "build";
    private static BuildRepository buildRepository = new BuildRepository(Build.COLLECTION, Build.class);

    private long getOrder() {
        int order = buildRepository.listOrderApplication(this.machine.name, this.application.name).size();
        return order + 1;
    }

    public void updateStatus(String status) {
        this.status = status;
        this.countTime();
        old_id = MongoHelper.treatsId(this._id);
        this._id = null;
        buildRepository.update(old_id, this);
        sendSlackStatusFromId(old_id);
    }

    public void update() {
        countTime();
        old_id = MongoHelper.treatsId(this._id);
        this._id = null;
        buildRepository.update(old_id, this);
    }

    public void discard() {
        this.status = BuildStatus.DISCARDED;
        old_id = MongoHelper.treatsId(this._id);
        this._id = null;
        buildRepository.update(old_id, this);
        sendSlackStatusFromId(old_id);
    }

    private void sendSlackStatusFromId(String id)
    {
        new SlackMessage().write(this.findOne(id)).send();
    }

    private void countTime() {
        LocalDateTime now = DateTimeUTC.now();
        Build build =  buildRepository.findById(MongoHelper.treatsId(this._id));
        if (build != null && build.start != null)
        {
            LocalDateTime start = TreatsValue.toLocalDateTimeFromArrayInteger(build.start);
            this.time = Duration.between(start,now).getSeconds();
        }
        this.end = TreatsValue.toIntegerArrayFromLocalDateTime(now);
    }

    public void save()
    {
        buildRepository.create(this);
    }

    public void sendSlackStatus()
    {
        new SlackMessage().write(this).send();
    }

    public List<Build> findAll() {
        return buildRepository.findAll();
    }

    public Build findOne(Object id) {
        return buildRepository.findById(MongoHelper.treatsId(id));
    }

    public List<SerieHighcharts> analytical() {
        List<Build> builds = buildRepository.listOrderApplication(this.machine.name,this.application.name);
        SerieHighcharts serie = new SerieHighcharts();
        if(builds.size()>0)
        {
            serie.name = "Builds history";

            Integer total = builds.size();

            Map<String, Double> mapStatus = new HashMap<>();

            mapStatus.put(BuildStatus.IN_PROGRESS, getPercent(builds, BuildStatus.IN_PROGRESS, total));
            mapStatus.put(BuildStatus.WAITING, getPercent(builds, BuildStatus.WAITING, total));
            mapStatus.put(BuildStatus.DISCARDED, getPercent(builds, BuildStatus.DISCARDED, total));
            mapStatus.put(BuildStatus.FAIL, getPercent(builds, BuildStatus.FAIL, total));
            mapStatus.put(BuildStatus.SUCCESS, getPercent(builds, BuildStatus.SUCCESS, total));

            mapStatus.forEach((key, value) -> serie.data.add(new DataHighcharts(key, value)));
            serie.data.stream().filter(item -> item.y>0).collect(Collectors.toList()).get(0).makeSelected();
        }
        List<SerieHighcharts> series  = new ArrayList<SerieHighcharts>();
        series.add(serie);
        return series;
    }
    private Double getPercent(List<Build> builds, String status, Integer total) {
        Long count = builds.stream().filter(item -> item.status.equals(status)).count();
        Double percentual = 100 * (new Double(count) / new Double(total));
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(4);
        numberFormat.setMaximumIntegerDigits(4);
        return Double.parseDouble(numberFormat.format(percentual));
    }
}
