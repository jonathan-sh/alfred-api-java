package com.alfred.api.app.resource;

import com.alfred.api.app.model.WebHook;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/web-hook")
public class WebHooksResource {

    private static final String EVENT = "X-GitHub-Event";

    @RequestMapping(method = RequestMethod.POST)
    public void webhooks(@RequestHeader(EVENT) String event,
                         @RequestBody WebHook webHook) {

        webHook.setEvent(event)
               .validEvent()
               .validRepository()
               .validApplication()
               .validMachine()
               .save();
    }


    @RequestMapping(method = RequestMethod.GET)
    public List<WebHook> webhooks() {
        return new WebHook().findAll();
    }

}

