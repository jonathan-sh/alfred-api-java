package com.alfred.api.resource;

import com.alfred.api.model.Build;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/build")
public class BuildResource {

    private Build build = new Build();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return new ResponseEntity<>(this.build.findAll(), HttpStatus.OK);
    }


}
