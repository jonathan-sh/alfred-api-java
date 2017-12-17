package com.alfred.api.app.resource;

import com.alfred.api.app.model.WebHook;
import com.alfred.api.useful.constants.App;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/web-hook")
@Scope("request")
public class WebHooksResource {

    @RequestMapping(method = RequestMethod.POST)
    public Object webhooks(@RequestHeader(App.TOKEN_EVENT_GIT) String event,
                           @RequestBody WebHook webHook) {

        webHook.setEvent(event)
               .validEvent()
               .validRepository()
               .validApplication()
               .validMachine()
               .save();

        Map<String,String> map = new HashMap<>();
        map.put("status","ok");
        return map;
    }


    @RequestMapping(method = RequestMethod.GET)
    public List<WebHook> webhooks() {
        return new WebHook().findAll();
    }

}

