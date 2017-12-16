package com.alfred.api.useful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AlfredConfig {

    public static String CLIENT_URL = "";
    public static String SLACK_URL = "";
    public static Integer UTC = 0;

    @Autowired
    public AlfredConfig(Environment environment) {

        try
        {
            CLIENT_URL = environment.getRequiredProperty("client.front-url");
            SLACK_URL = environment.getRequiredProperty("slack.msg-url");
            UTC = Integer.parseInt(environment.getRequiredProperty("configuration.utc"));
        }
        catch (Exception e)
        {
            System.out.println("fail");
        }
    }
}