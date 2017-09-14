package com.alfred.api.dao;

import com.alfred.api.dao.mongoDB.MongoCrud;
import com.alfred.api.model.WebHook;
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
            List<WebHook> webHooks = (List<WebHook>) super.readAll();
            webHooks.forEach(WebHook::tratesForResponse);
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
