package com.alfred.api.app.dto;

import com.alfred.api.app.dao.BobBuilderRepository;
import com.google.gson.annotations.Expose;

public class Bob {
    @Expose
    public Integer[] dateTime;
    @Expose
    public String commad;

    public Bob(Integer[] dateTime, String commad) {
        this.dateTime = dateTime;
        this.commad = commad;
    }

    private BobBuilderRepository bobBuilderRepository = new BobBuilderRepository("bob_builder",Bob.class);

    public void save()
    {
        bobBuilderRepository.create(this);
    }

}
