package com.alfred.api.app.resource;

import com.alfred.api.app.model.Machine;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/machine")
public class MachineResource {

    private Machine machine = new Machine();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return new ResponseEntity<>(this.machine.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody Machine machine) {
        this.machine = machine.validForCreate()
                              .validExistence()
                              .create();
        return makeResponse();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Machine machine) {
        this.machine = machine.validForUpdate()
                              .update();
        return makeResponse();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@RequestBody Machine machine) {
        this.machine = machine.delete();
        return makeResponse();
    }


    private ResponseEntity<Object> makeResponse() {
        if (this.machine.validation.status)
        {
            return new ResponseEntity<>(this.machine, this.machine.validation.httpStatus);
        }
        else
        {
            return new ResponseEntity<>(this.machine.validation, this.machine.validation.httpStatus);
        }
    }
}
