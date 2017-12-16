package com.alfred.api.integration.slack.message;

import com.alfred.api.app.model.Build;
import com.alfred.api.useful.AlfredConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
@Component
public class SlackMessage {
    private List<Attachment>  attachments = new ArrayList<>();
    public List<Attachment> getAttachments() {
        return attachments;
    }


    @JsonIgnore
    public SlackMessage write(Build build)
    {
        this.attachments = new Attachment().mountAttachments(build);
        return this;
    }

    @JsonIgnore
    public Boolean send()
    {

        RestTemplate restTemplate = new RestTemplate();
        String URL = AlfredConfig.SLACK_URL;
        try
        {
            HttpEntity<Object> request = new HttpEntity<>(new Gson().toJson(this));
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            System.out.println("fail");
        }
        return false;
    }
}
