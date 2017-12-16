package com.alfred.api.app.resource;

import com.alfred.api.app.model.Application;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/application")
public class ApplicationResource {

    private Application application = new Application();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return new ResponseEntity<>(this.application.findAll(), HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody Application application) {
        this.application = application.validForCreate()
                                      .validExistence()
                                      .create();
        return makeResponse();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Application application) {
        this.application = application.validForUpdate()
                                      .update();
        return makeResponse();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@RequestBody Application application) {
        this.application = application.delete();
        return makeResponse();
    }


    private ResponseEntity<Object> makeResponse() {
        if (this.application.validation.status)
        {
            return new ResponseEntity<>(this.application, this.application.validation.httpStatus);
        }
        else
        {
            return new ResponseEntity<>(this.application.validation, this.application.validation.httpStatus);
        }
    }
}
