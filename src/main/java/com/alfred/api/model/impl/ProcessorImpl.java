package com.alfred.api.model.impl;

import com.alfred.api.dao.BuildRepository;
import com.alfred.api.model.Build;
import com.alfred.api.model.face.BobBuilder;
import com.alfred.api.model.face.Processor;
import com.alfred.api.util.constants.BuildStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessorImpl implements Processor {

    private static BuildRepository buildRepository = new BuildRepository(Build.COLLECTION, Build.class);
    private List<Build> pendings;
    private Build buildInProcess;
    private BobBuilder bobBuilder;

    @Autowired
    public ProcessorImpl(BobBuilder bobBuilder) {
        this.bobBuilder = bobBuilder;
    }

    @Override
    public void process() {
        if (nobodyInProcess())
        {
            getPendding();
            build();
        }
    }

    private boolean nobodyInProcess() {
        return buildRepository.listByStatus(BuildStatus.IN_PROGRESS).size() == 0;
    }

    private void getPendding() {
        this.pendings = buildRepository.listByStatus(BuildStatus.WAITING);

    }

    private void build() {
        if (this.pendings.size() > 0)
        {
            this.buildInProcess = pendings.get(0);
            discartOldBuild();
            Object _id = this.buildInProcess._id;
            this.buildInProcess.updateStatus(BuildStatus.IN_PROGRESS);
            this.buildInProcess._id = _id;
            this.bobBuilder.build(this.buildInProcess);
        }
    }

    private void discartOldBuild() {
        this.pendings.stream()
                .filter(this::isOld)
                .forEach(Build::discart);
    }

    private boolean isOld(Build build) {
        return build.application.equals(this.buildInProcess.application) &&
                build.branch.equals(this.buildInProcess.branch) &&
                build.server.name.equals(this.buildInProcess.server.name) &&
                build.order < this.buildInProcess.order;
    }


}
