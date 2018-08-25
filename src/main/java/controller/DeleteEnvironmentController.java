package controller;

import com.google.common.base.Stopwatch;
import domain.Environment;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@Component
public class DeleteEnvironmentController extends AbstractEnvironmentController {
    private final String DELETE_STATEMENT = "delete from environment where id = ?";
    private final int NUMBER_OF_DELETES = 10000;
    private static int counter = 1;

    @Override
    public long viaJdbcPreparedStatement() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(DELETE_STATEMENT)) {
                Stopwatch timer = Stopwatch.createStarted();
                for (int i = counter; i < NUMBER_OF_DELETES + counter; i++) {
                    statement.setInt(1, i);
                    statement.execute();
                }
                timer.stop();
                counter += NUMBER_OF_DELETES;
                return timer.elapsed(TimeUnit.MILLISECONDS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public long viaJdbcBatch() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(DELETE_STATEMENT)) {
                for (int i = counter; i < NUMBER_OF_DELETES + counter; i++) {
                    statement.setInt(1, i);
                    statement.addBatch();
                }
                Stopwatch timer = Stopwatch.createStarted();
                statement.executeBatch();
                timer.stop();
                counter += NUMBER_OF_DELETES;
                return timer.elapsed(TimeUnit.MILLISECONDS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public long viaJdbcTemplate() {
        Stopwatch timer = Stopwatch.createStarted();
        for (int i = counter; i < NUMBER_OF_DELETES + counter; i++) {
            jdbcTemplate.update(DELETE_STATEMENT, i);
        }
        timer.stop();
        counter += NUMBER_OF_DELETES;
        return timer.elapsed(TimeUnit.MILLISECONDS);
    }

    @Override
    public long viaMongo() {
        Stopwatch timer = Stopwatch.createStarted();
        for (int i = 1; i <= NUMBER_OF_DELETES; i++)
            environmentMongoRepository.delete(String.valueOf(i));
        timer.stop();

        return timer.elapsed(TimeUnit.MILLISECONDS);
    }

    @Override
    public long viaHibernate() {
        Session session = sessionFactory.openSession();
        for (int i = counter; i < NUMBER_OF_DELETES + counter; i++) {
            Environment environment = session.load(Environment.class, i);
            session.delete(environment);
        }
        Stopwatch timer = Stopwatch.createStarted();
        session.beginTransaction().commit();
        timer.stop();
        counter += NUMBER_OF_DELETES;
        return timer.elapsed(TimeUnit.MILLISECONDS);
    }
}
