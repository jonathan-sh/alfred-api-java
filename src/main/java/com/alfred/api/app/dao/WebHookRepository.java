package com.alfred.api.app.dao;

import com.alfred.api.app.dao.mongoDB.MongoCrud;
import com.alfred.api.app.model.WebHook;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class WebHookRepository extends MongoCrud {
    public WebHookRepository(String collection, Class clazz) {
        super(collection, clazz);
    }

    private static Logger logger = LoggerFactory.getLogger(WebHookRepository.class);

    public List<WebHook> findAll() {
        try
        {
            Document querySort = new Document();
            querySort.put("_id",-1);
            List<WebHook> webHooks = (List<WebHook>) super.readAll(querySort);
            webHooks.forEach(WebHook::treatsForResponse);
            return webHooks;
        }
        catch (Exception e)
        {
            logger.error("Error to find all webHooks ");
            logger.error("Possible cause: " + e.getCause());
        }
        return new ArrayList<>();
    }
}
