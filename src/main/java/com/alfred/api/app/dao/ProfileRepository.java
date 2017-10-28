package com.alfred.api.app.dao;

import com.alfred.api.app.dao.mongoDB.MongoCrud;
import com.alfred.api.app.model.Profile;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ProfileRepository extends MongoCrud {
    public ProfileRepository(String collection, Class clazz) {
        super(collection, clazz);
    }

    private static Logger logger = LoggerFactory.getLogger(ProfileRepository.class);

    public Profile findByEmail(String email) {
        Document query = new Document();
        query.append("email", email);
        Profile profile = null;
        List<Profile> profiles = super.read(query, new Document(), 0);
        try
        {
            profile = profiles.get(0);
        }
        catch (Exception e)
        {
            logger.error("Error to find the profile by ip");
            logger.error("Possible cause: " + e.getCause());
        }
        return profile;
    }


    public List<Profile> findAll() {
        try
        {
            List<Profile> profiles = (List<Profile>) super.readAll();
            profiles.forEach(Profile::tratesForResponse);
            return profiles;
        }
        catch (Exception e)
        {
            logger.error("Error to find all profiles ");
            logger.error("Possible cause: " + e.getCause());
        }
        return new ArrayList<>();
    }

}
