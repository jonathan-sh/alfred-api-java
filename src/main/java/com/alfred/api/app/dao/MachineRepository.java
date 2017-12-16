package com.alfred.api.app.dao;

import com.alfred.api.app.dao.mongoDB.MongoCrud;
import com.alfred.api.app.model.Machine;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MachineRepository extends MongoCrud {
    public MachineRepository(String collection, Class clazz) {
        super(collection, clazz);
    }

    private static Logger logger = LoggerFactory.getLogger(MachineRepository.class);

    public Machine findByIP(String ip) {
        Document query = new Document();
        query.append("ip", ip);
        Machine machine = null;
        return getMachine(query, machine);
    }


    public Machine findByName(String machineName) {
        Document query = new Document();
        query.append("name", machineName);
        Machine machine = null;
        return getMachine(query, machine);
    }



    List<Machine> machines = new ArrayList<Machine>();

    public List<Machine> findByApplicationIdAndBranchName(String applicationId, String branchName) {
        List<Machine> allMachines = this.findAll();
        List<Machine> filterMachines = new ArrayList<Machine>();
        allMachines.forEach(item ->
        {
            item.branchs
                    .stream()
                    .forEach(c -> System.out.println(c));

            long branchs = item.branchs
                               .stream()
                               .filter(c -> c.equals(branchName))
                               .count();
            if (branchs>0)
            {
                machines.add(item);
            }
        });

        if (machines != null && machines.size() > 0)
        {
            filterMachines  =     machines.stream()
                                          .filter(item -> item.applications.contains(applicationId))
                                          .collect(Collectors.toList());
        }
        return filterMachines;
    }


    private Machine getMachine(Document query, Machine machine) {
        List<Machine> machines = super.read( query, new Document(), 0);
        try
        {
            machine = machines.get(0);
        }
        catch (Exception e)
        {
            logger.error("Error to find the profile");
            logger.error("Possible cause: " + e.getCause());
        }
        return machine;
    }


    public List<Machine> findAll() {
        try
        {
            List<Machine> machines = (List<Machine>) super.readAll();
            machines.forEach(Machine::treatsForResponse);
            return machines;
        }
        catch (Exception e)
        {
            logger.error("Error to find all profiles ");
            logger.error("Possible cause: " + e.getCause());
        }
        return new ArrayList<>();
    }


}
