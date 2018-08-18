package configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("repository")
public class MongoDbConfiguration extends AbstractMongoConfiguration{

    @Override
    protected String getDatabaseName() {
        return "DataPerformanceDB";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient();
    }

    @Override
    protected String getMappingBasePackage() {
        return "domain";
    }

}
