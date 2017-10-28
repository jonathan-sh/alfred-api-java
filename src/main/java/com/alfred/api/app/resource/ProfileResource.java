package com.alfred.api.app.resource;

import com.alfred.api.app.model.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/profile")
public class ProfileResource {

    private Profile profile = new Profile();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return new ResponseEntity<>(this.profile.findAll(), HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody Profile profile) {
        this.profile = profile.validForCreate()
                .validExistence()
                .create();
        return makeResponse();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Profile profile) {
        this.profile = profile.validForUpdate()
                .update();
        return makeResponse();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@RequestBody Profile profile) {
        this.profile = profile.delete();
        return makeResponse();
    }


    private ResponseEntity<Object> makeResponse() {
        if (this.profile.validation.status)
        {
            return new ResponseEntity<>(this.profile, this.profile.validation.httpStatus);
        }
        else
        {
            return new ResponseEntity<>(this.profile.validation, this.profile.validation.httpStatus);
        }
    }
}
