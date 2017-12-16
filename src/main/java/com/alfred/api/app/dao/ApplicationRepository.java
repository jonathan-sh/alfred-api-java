package com.alfred.api.app.dao;

import com.alfred.api.app.dao.mongoDB.MongoCrud;
import com.alfred.api.app.model.Application;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ApplicationRepository extends MongoCrud {
    public ApplicationRepository(String collection, Class clazz) {
        super(collection, clazz);
    }

    private static Logger logger = LoggerFactory.getLogger(ApplicationRepository.class);

    public Application findByName(String name) {
        try
        {
            Document query = new Document();
            Document sort = new Document();
            query.append("name", name);
            List<Application> applications = (List<Application>) super.read(query, sort, 0);
            return applications.get(0);
        }
        catch (Exception e)
        {
            logger.error("Error to find the application name");
            logger.error("Possible cause: " + e.getCause());
        }
        return null;
    }


    public List<Application> findAllStatus() {
        try
        {
            Document query = new Document();
            Document sort = new Document();
            query.append("status", true);
            List<Application> applications = (List<Application>) super.read(query, sort, 0);
            applications.forEach(Application::treatsForResponse);
            return applications;
        }
        catch (Exception e)
        {
            logger.error("Error to find all applications ");
            logger.error("Possible cause: " + e.getCause());
        }
        return new ArrayList<>();
    }

    public List<Application> findAll() {
        try
        {
            Document query = new Document();
            Document sort = new Document();
            List<Application> applications = (List<Application>) super.read(query, sort, 0);
            applications.forEach(Application::treatsForResponse);
            return applications;
        }
        catch (Exception e)
        {
            logger.error("Error to find all applications ");
            logger.error("Possible cause: " + e.getCause());
        }
        return new ArrayList<>();
    }

}
