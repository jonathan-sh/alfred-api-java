package com.alfred.api.app.dao;

import com.alfred.api.app.dao.mongoDB.MongoCrud;

public class BobBuilderRepository extends MongoCrud {
    public BobBuilderRepository(String collection, Class clazz) {
        super(collection, clazz);
    }
}
