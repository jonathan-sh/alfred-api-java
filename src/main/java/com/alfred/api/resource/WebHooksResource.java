package com.alfred.api.resource;

import com.alfred.api.model.WebHook;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/web-hook")
public class WebHooksResource {

    private static final String EVENT = "X-GitHub-Event";

    @RequestMapping(value = "/{serverName:.+}/{branch:.+}", method = RequestMethod.POST)
    public void webhooks(@PathVariable String serverName,
                         @PathVariable String branch,
                         @RequestHeader(EVENT) String event,
                         @RequestBody WebHook webHook) {

        webHook.setEvent(event)
                .validEvent()
                .validRepository()
                .setServerName(serverName)
                .validServerName()
                .validMachine()
                .setBranch(branch)
                .validBranch()
                .save();
    }


    @RequestMapping(method = RequestMethod.GET)
    public List<WebHook> webhooks() {
        return new WebHook().findAll();
    }

}

