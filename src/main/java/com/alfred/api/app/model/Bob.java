package com.alfred.api.app.model;

import com.alfred.api.app.dao.BobBuilderRepository;
import com.google.gson.annotations.Expose;

public class Bob {
    @Expose
    public Integer[] dateTime;
    @Expose
    public String command;
    @Expose
    public String build_id;
    @Expose
    public Integer order;

    public Bob(Integer[] dateTime, String command, String build_id) {
        this.dateTime = dateTime;
        this.command = command;
        this.build_id = build_id;
        this.order = this.bobBuilderRepository.readAll().size() + 1;
    }

    private BobBuilderRepository bobBuilderRepository = new BobBuilderRepository("bob_builder",Bob.class);

    public void save()
    {
        bobBuilderRepository.create(this);
    }

}
