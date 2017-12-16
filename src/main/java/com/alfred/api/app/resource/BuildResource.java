package com.alfred.api.app.resource;

import com.alfred.api.app.model.Build;
import com.alfred.api.useful.constants.BuildStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/build")
public class BuildResource {

    private Build build = new Build();

    public BuildResource() {

    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return new ResponseEntity<>(this.build.findAll(), HttpStatus.OK);
    }

    @RequestMapping(path = "/analytical", method = RequestMethod.POST)
    public ResponseEntity<Object> analytical(@RequestBody Build build) {
        return new ResponseEntity<>(build.analytical(), HttpStatus.OK);
    }

    @RequestMapping(path = "/status", method = RequestMethod.POST)
    public void status(@RequestBody Build build) {
        if (build!=null && build._id!=null && !build._id.toString().isEmpty())
        {
            if (build.status.equals("true"))
            {
                build.updateStatus(BuildStatus.SUCCESS);
            }
            else
            {
                build.updateStatus(BuildStatus.FAIL);
            }
        }
    }

}
