package com.alfred.api.dao;

import com.alfred.api.dao.mongoDB.MongoCrud;
import com.alfred.api.model.Build;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BuildRepository extends MongoCrud {
    public BuildRepository(String collection, Class clazz) {
        super(collection, clazz);
    }

    private static Logger logger = LoggerFactory.getLogger(BuildRepository.class);

    public Build findByIP(String ip) {
        Document query = new Document();
        query.append("ip", ip);
        Build build = null;
        return getMachine(query, build);
    }


    private Build getMachine(Document query, Build machine) {
        List<Build> builds = super.read(query, new Document(), 0);
        try
        {
            machine = builds.get(0);
        }
        catch (Exception e)
        {
            logger.error("Error to find the profile");
            logger.error("Possible cause: " + e.getCause());
        }
        return machine;
    }


    public List<Build> findAll() {
        try
        {
            List<Build> builds = (List<Build>) super.readAll();
            builds.forEach(Build::tratesForResponse);
            return builds;
        }
        catch (Exception e)
        {
            logger.error("Error to find all builds ");
            logger.error("Possible cause: " + e.getCause());
        }
        return new ArrayList<>();
    }

    public List<Build> listOrderApplication(String serverName, String application) {
        try
        {
            Document query = new Document();
            query.append("server.name", serverName);
            query.append("application", application);
            List<Build> builds = (List<Build>) super.read(query, new Document(), 0);
            return builds;
        }
        catch (Exception e)
        {
            logger.error("Error to find all builds ");
            logger.error("Possible cause: " + e.getCause());
        }
        return new ArrayList<>();
    }

    public List<Build> listByStatus(String status) {
        try
        {
            Document query = new Document();
            Document sort = new Document();
            query.append("status", status);
            sort.append("order", -1);
            List<Build> builds = (List<Build>) super.read(query, sort, 0);
            return builds;
        }
        catch (Exception e)
        {
            logger.error("Error to find all builds ");
            logger.error("Possible cause: " + e.getCause());
        }
        return new ArrayList<>();
    }


}
