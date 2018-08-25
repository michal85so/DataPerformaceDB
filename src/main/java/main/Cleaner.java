package main;

import domain.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class Cleaner {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired


    public void cleanAllData() {
        mongoTemplate.dropCollection(Environment.class);
    }
}
