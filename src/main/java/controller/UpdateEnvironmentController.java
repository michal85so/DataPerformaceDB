package controller;

import com.google.common.base.Stopwatch;
import domain.Environment;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class UpdateEnvironmentController extends AbstractEnvironmentController {

    private final int RECORDS_IN_ONE_EXECUTION = 10000;
    private final String UPDATE_STATEMENT = "UPDATE environment SET producer = ? WHERE id = ?";

    public long viaJdbcPreparedStatement() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_STATEMENT)) {
                Stopwatch timer = Stopwatch.createStarted();
                for (int i = 0; i < RECORDS_IN_ONE_EXECUTION; i++) {
                    statement.setString(1, "jdbc" + i);
                    statement.setInt(2, i);
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
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_STATEMENT)) {
                for (int i = 0; i < RECORDS_IN_ONE_EXECUTION; i++) {
                    statement.setString(1, "jdbc batch" + i);
                    statement.setInt(2, i);
                    statement.addBatch();
                }
                Stopwatch timer = Stopwatch.createStarted();
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
        Stopwatch timer = Stopwatch.createStarted();
        for (int i = 0; i < RECORDS_IN_ONE_EXECUTION; i++) {
            jdbcTemplate.update(UPDATE_STATEMENT, "jdbc template" + i, i);
        }
        timer.stop();
        return timer.elapsed(TimeUnit.MILLISECONDS);
    }

    public long viaMongo() {
        List<Environment> environments = environmentMongoRepository.findAll();
        environments.forEach(environment -> environment.setProducer("mongo producer " + environment.getId()));

        Stopwatch timer = Stopwatch.createStarted();
        environmentMongoRepository.save(environments);
        timer.stop();

        return timer.elapsed(TimeUnit.MILLISECONDS);
    }

    public long viaHibernate() {
        Session session = sessionFactory.openSession();
        List<Environment> environments = session.createQuery("from environment", Environment.class)
                .setMaxResults(RECORDS_IN_ONE_EXECUTION)
                .list();
        environments.forEach(i -> i.setProducer("hibernate" + i.getId()));

        Stopwatch timer = Stopwatch.createStarted();
        session.beginTransaction().commit();
        timer.stop();

        session.close();

        return timer.elapsed(TimeUnit.MILLISECONDS);
    }
}
