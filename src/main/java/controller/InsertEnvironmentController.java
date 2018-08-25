package controller;

import com.google.common.base.Stopwatch;
import domain.Environment;
import org.hibernate.Session;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class InsertEnvironmentController extends AbstractEnvironmentController{

    private final int RECORDS_IN_ONE_EXECUTION = 10000;
    private final String INSERT_ENVIRONMENT = "INSERT INTO " +
            "environment(id, producer, category, model, price, customer_price, items, warranty) " +
            "VALUES (?,?,?,?,?,?,?,?)";

    public long viaJdbcPreparedStatement() {
        try (Connection connection = dataSource.getConnection()) {
            List<Environment> environments = environmentGenerator.generateANumber(RECORDS_IN_ONE_EXECUTION);

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

    public long viaJdbcBatch() {
        try (Connection connection = dataSource.getConnection()) {
            List<Environment> environments = environmentGenerator.generateANumber(RECORDS_IN_ONE_EXECUTION);

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

    public long viaJdbcTemplate() {
        List<Environment> environments = environmentGenerator.generateANumber(RECORDS_IN_ONE_EXECUTION);

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

    public long viaMongo() {
        List<Environment> environments = environmentGenerator.generateANumber(RECORDS_IN_ONE_EXECUTION);

        Stopwatch timer = Stopwatch.createStarted();
        environmentMongoRepository.insert(environments);
        timer.stop();

        return timer.elapsed(TimeUnit.MILLISECONDS);
    }

    public long viaHibernate() {
        List<Environment> environments = environmentGenerator.generateANumber(RECORDS_IN_ONE_EXECUTION);

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
