package com.alfred.api.dao;

import com.alfred.api.dao.mongoDB.MongoCrud;
import com.alfred.api.model.Machine;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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


    public Machine findByName(String name) {
        Document query = new Document();
        query.append("name", name);
        Machine machine = null;
        return getMachine(query, machine);
    }


    private Machine getMachine(Document query, Machine machine) {
        List<Machine> machines = super.read(query, new Document(), 0);
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
            machines.forEach(Machine::tratesForResponse);
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
