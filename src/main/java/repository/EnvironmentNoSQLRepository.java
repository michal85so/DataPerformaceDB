package repository;

import domain.Environment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EnvironmentNoSQLRepository extends MongoRepository<Environment, String> {
}
