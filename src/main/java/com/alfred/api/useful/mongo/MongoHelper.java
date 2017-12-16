package com.alfred.api.useful.mongo;

/**
 * Created by jonathan on 7/14/17.
 */
public class MongoHelper {
    private static final String START = "{$oid=";
    private static final String END = "}";

    public static String treatsId(Object mongoId) {
       try
       {
           return mongoId.toString()
                   .replace(START, "")
                   .replace(END, "");
       }
       catch (Exception e)
       {
           return null;
       }
    }
}
