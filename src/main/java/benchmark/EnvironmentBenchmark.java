package benchmark;

import domain.Environment;
import generator.EnvironmentGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.EnvironmentRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class EnvironmentBenchmark {

    @Autowired
    private EnvironmentGenerator environmentGenerator;

    @Autowired
    private EnvironmentRepository environmentMongoRepository;

    @Autowired
    private DataSource dataSource;


    private final String INSERT_ENVIRONMENT = "insert into environment(id, producer, category, model, price, customer_price, items, warranty) values (?,?,?,?,?,?,?,?)";

    public void testInsertsViaJdbcPreparedStatement() throws SQLException {
        Connection connection = dataSource.getConnection();

        List<PreparedStatement> statements = new ArrayList<>();

        List<Environment> environments = environmentGenerator.generateANumber(1000);

        for (Environment environment : environments) {
            PreparedStatement statement = connection.prepareStatement(INSERT_ENVIRONMENT);
            fillStatement(environment, statement);
            statements.add(statement);
        }

        for (PreparedStatement statement : statements) {
            statement.execute();
        }

    }

    private void fillStatement(Environment environment, PreparedStatement statement) {
        try {
            statement.setInt(1, environment.getId());
            statement.setString(2, environment.getProducer());
            statement.setString(3, environment.getCategory());
            statement.setString(4, environment.getModel());
            statement.setDouble(5, environment.getPrice());
            statement.setDouble(6, environment.getCustomerPrice());
            statement.setInt(7, environment.getItems());
            statement.setInt(8, environment.getWarranty());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
