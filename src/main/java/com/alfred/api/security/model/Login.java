package com.alfred.api.security.model;

import com.alfred.api.app.dao.ProfileRepository;
import com.alfred.api.app.model.Profile;
import com.alfred.api.util.constants.DetailsDescription;
import com.alfred.api.util.encryptions.EncryptionSHA;
import com.alfred.api.util.mongo.MongoHelper;
import com.alfred.api.util.token.TokenUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Login {
    private static Logger logger = LoggerFactory.getLogger(Login.class);


    @Expose
    public Object _id;
    @Expose
    public String password;
    @Expose
    public String email;
    @Expose
    public String token;

    public Login() {
    }

    public Login(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private void setPassword(String password) {
        this.password =  EncryptionSHA.generateHash(password);;
    }

    @JsonIgnore
    public String getUserNameSpring() {
        if (!isValid())
        {
            return null;
        }
        return this.email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    private boolean isValid() {
        return this.email != null &&
               this.password != null &&
               !this.email.isEmpty() &&
               !this.password.isEmpty();
    }


    private ProfileRepository profileRepository = new ProfileRepository(Profile.COLLECTION, Profile.class);

    @JsonIgnore
    public Object makeLogin(TokenUtils tokenUtils) {

        if (isValid())
        {
            Profile profile  = profileRepository.findByEmail(this.email);
            this._id = MongoHelper.treatsId(profile._id);
            this.password = DetailsDescription.PASSWORD.get();
            this.token = tokenUtils.generateToken(this);
            return this;
        }
        return null;
    }
}

