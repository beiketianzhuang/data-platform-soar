package com.bektz.dataplatformsoar.service;

import com.bektz.dataplatformsoar.req.SchemaReq;
import com.bektz.dataplatformsoar.service.jdbc.DataSourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchemaService {

    @Autowired
    private DataSourceManager dataSourceManager;


    public void addSchema(SchemaReq schemaReq) {
        dataSourceManager.initDataSource(schemaReq);
    }
}
