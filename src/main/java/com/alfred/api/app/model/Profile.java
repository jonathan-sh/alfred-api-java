package com.alfred.api.app.model;

import com.alfred.api.app.dao.ProfileRepository;
import com.alfred.api.app.dto.Validation;
import com.alfred.api.useful.encryptions.EncryptionSHA;
import com.alfred.api.useful.mongo.MongoHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public class Profile {
    @Expose
    public Object _id;
    @Expose
    public String name;
    @Expose
    public String email;
    @Expose
    public String password;
    @Expose
    public Boolean adminLevel;
    @Expose
    public Boolean status;

    @JsonIgnore
    @Expose(serialize = false)
    public Validation validation = new Validation();

    public Profile() {
        this.validation.makeOK();
    }


    public Profile validForCreate() {
        Boolean promise = this.validName() &&
                          this.validEmail() &&
                          this.validControllers();
        if (!promise)
        {
            validation.fieldsError(isRequired());
        }
        else
        {
            this.password = EncryptionSHA.generateHash(this.password);
        }
        return this;
    }

    public Profile validExistence() {
        if (validEmail())
        {
            Object found = profileRepository.findByEmail(this.email);
            if (found != null)
            {
                this.validation.alreadyExists(this.email);
            }
        }
        return this;
    }

    public Profile validForUpdate() {
        Boolean promise = this.validId() &&
                          this.validName() &&
                          this.validEmail() &&
                          this.validControllers();
        if (!promise)
        {
            validation.fieldsError(isRequired() + "and id");
        }
        if (validPassword())
        {
            this.password = EncryptionSHA.generateHash(this.password);
        }
        return this;
    }

    private Boolean validId() {
        return this._id != null;
    }

    private Boolean validEmail() {
        return this.email != null && !this.email.isEmpty();
    }

    private Boolean validName() {
        return this.name != null && !this.name.isEmpty();
    }

    private Boolean validControllers() {
        return this.status != null && this.adminLevel != null;
    }

    private Boolean validPassword()  {
        return this.password != null && !this.password.isEmpty();
    }


    private String isRequired() {
        return "<name, email, adminLevel, status>";
    }

    public Profile treatsForResponse() {
        this._id = MongoHelper.treatsId(this._id);
        return this;
    }

    public static String COLLECTION = "profile";
    private ProfileRepository profileRepository = new ProfileRepository(COLLECTION, this.getClass());

    public Profile create() {
        if (validation.status)
        {
            profileRepository.create(this);
        }
        return this;
    }

    public Profile update() {
        if (validation.status)
        {
            profileRepository.update(this._id, this);
        }
        return this;
    }

    public Profile delete() {
        if (validId())
        {
            profileRepository.deleteOne(this._id);
        }
        return this;
    }

    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    public String BCryptEncoderPassword() {
        return new BCryptPasswordEncoder().encode(this.password);
    }
}
