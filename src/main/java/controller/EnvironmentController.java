package controller;

import com.google.common.base.Stopwatch;
import domain.Environment;
import generator.EnvironmentGenerator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;
import repository.EnvironmentNoSQLRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class EnvironmentController {

    private final EnvironmentGenerator environmentGenerator;
    private final EnvironmentNoSQLRepository environmentMongoRepository;
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final SessionFactory sessionFactory;

    private final int RECORDS_IN_ONE_EXECUCION = 10000;
    private final String INSERT_ENVIRONMENT = "INSERT INTO " +
            "environment(id, producer, category, model, price, customer_price, items, warranty) " +
            "VALUES (?,?,?,?,?,?,?,?)";

    @Autowired
    public EnvironmentController(EnvironmentGenerator environmentGenerator,
                                 EnvironmentNoSQLRepository environmentMongoRepository,
                                 DataSource dataSource,
                                 JdbcTemplate jdbcTemplate,
                                 SessionFactory sessionFactory) {
        this.environmentGenerator = environmentGenerator;
        this.environmentMongoRepository = environmentMongoRepository;
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        this.sessionFactory = sessionFactory;
    }

    public long insertsViaJdbcPreparedStatement() {
        try (Connection connection = dataSource.getConnection()) {
            List<Environment> environments = environmentGenerator.generateANumber(RECORDS_IN_ONE_EXECUCION);

            try (PreparedStatement statement = connection.prepareStatement(INSERT_ENVIRONMENT)) {
                Stopwatch timer = Stopwatch.createStarted();
                for (Environment environment : environments) {
                    fillStatement(environment, statement);
                    statement.execute();
                }
                timer.stop();
                return timer.elapsed(TimeUnit.MILLISECONDS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long insertsViaJdbcBatch() {
        try (Connection connection = dataSource.getConnection()) {
            List<Environment> environments = environmentGenerator.generateANumber(RECORDS_IN_ONE_EXECUCION);

            try (PreparedStatement statement = connection.prepareStatement(INSERT_ENVIRONMENT)) {
                Stopwatch timer = Stopwatch.createStarted();
                for (Environment environment : environments) {
                    fillStatement(environment, statement);
                    statement.addBatch();
                }
                statement.executeBatch();
                timer.stop();
                return timer.elapsed(TimeUnit.MILLISECONDS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long insertsViaJdbcTemplate() {
        List<Environment> environments = environmentGenerator.generateANumber(RECORDS_IN_ONE_EXECUCION);

        Stopwatch timer = Stopwatch.createStarted();
        for (Environment environment : environments) {
            jdbcTemplate.execute(INSERT_ENVIRONMENT, (PreparedStatementCallback<Boolean>) preparedStatement -> {
                fillStatement(environment, preparedStatement);
                return preparedStatement.execute();
            });
        }
        timer.stop();
        return timer.elapsed(TimeUnit.MILLISECONDS);
    }

    public long insertsViaMongo() {
        List<Environment> environments = environmentGenerator.generateANumber(RECORDS_IN_ONE_EXECUCION);

        Stopwatch timer = Stopwatch.createStarted();
        environmentMongoRepository.insert(environments);
        timer.stop();

        return timer.elapsed(TimeUnit.MILLISECONDS);
    }

    public long insertsViaHibernate() {
        List<Environment> environments = environmentGenerator.generateANumber(RECORDS_IN_ONE_EXECUCION);

        Session session = sessionFactory.openSession();

        Stopwatch timer = Stopwatch.createStarted();
        environments.forEach(session::save);
        session.flush();
        timer.stop();

        session.close();

        return timer.elapsed(TimeUnit.MILLISECONDS);
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
