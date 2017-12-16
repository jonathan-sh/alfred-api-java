package com.alfred.api.app.model.implementatios;

import com.alfred.api.app.dao.BuildRepository;
import com.alfred.api.app.dao.MachineRepository;
import com.alfred.api.app.model.Build;
import com.alfred.api.app.model.Machine;
import com.alfred.api.app.model.interfaces.BobBuilder;
import com.alfred.api.app.model.interfaces.Processor;
import com.alfred.api.util.constants.BuildStatus;
import com.alfred.api.util.mongo.MongoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessorImpl implements Processor {

    private static BuildRepository buildRepository = new BuildRepository(Build.COLLECTION, Build.class);
    private static MachineRepository machineRepository = new MachineRepository(Machine.COLLECTION, Machine.class);
    private List<Build> pendings;
    private Build buildInProcess;
    private BobBuilder bobBuilder;

    @Autowired
    public ProcessorImpl(BobBuilder bobBuilder) {
        this.bobBuilder = bobBuilder;
    }

    @Override
    public void process() {

        machineRepository
                .findAll()
                .stream()
                .filter(Machine::getStatus)
                .forEach(machine ->
                {
                    this.pendings = null;
                    if (nobodyInProcess(machine) && hasPending(machine) )
                    {
                        build();
                    }
                });

    }

    private boolean nobodyInProcess(Machine machine) {
        boolean response = buildRepository.listByStatusAndMachine(BuildStatus.IN_PROGRESS,machine).size() == 0;
        return response;
    }

    private boolean hasPending(Machine machine) {
        this.pendings = buildRepository.listByStatusAndMachine(BuildStatus.WAITING,machine);
        boolean response = this.pendings.size() > 0;
        return response;
    }

    private void build() {
        if (this.pendings.size() > 0)
        {
            this.buildInProcess = pendings.get(0);
            discardOldBuild();
            Object _id = this.buildInProcess._id;
            this.buildInProcess.updateStatus(BuildStatus.IN_PROGRESS);
            this.buildInProcess._id = _id;
            this.bobBuilder.build(this.buildInProcess);
        }
    }

    private void discardOldBuild() {
         this.pendings.remove(this.buildInProcess);
         this.pendings.stream()
                      .filter(this::isSameApplication)
                      .forEach(Build::discard);
    }

    private boolean isSameApplication(Build build) {
       String idbBuildInProcess = MongoHelper.treatsId(this.buildInProcess.application._id);
       String idToCheck = MongoHelper.treatsId(build.application._id);
       return idbBuildInProcess.equals(idToCheck);
    }


}
