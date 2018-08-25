package controller;

import com.google.common.base.Stopwatch;
import domain.Environment;
import org.hibernate.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class DeleteEnvironmentController extends AbstractEnvironmentController {
    private final String DELETE_STATEMENT = "delete from environment where id = ?";
    private static int counter = 1;

    @Override
    public long viaJdbcPreparedStatement() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(DELETE_STATEMENT)) {
                Stopwatch timer = Stopwatch.createStarted();
                for (int i = counter; i < 10000; i++) {
                    statement.setInt(1, i);
                    statement.execute();
                }
                timer.stop();
                counter += 10000;
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
                for (int i = counter; i < 20000; i++) {
                    statement.setInt(1, i);
                    statement.addBatch();
                }
                Stopwatch timer = Stopwatch.createStarted();
                statement.executeBatch();
                timer.stop();
                counter += 10000;
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
        for (int i = counter; i < 30000; i++) {
            jdbcTemplate.update(DELETE_STATEMENT, i);
        }
        timer.stop();
        counter += 10000;
        return timer.elapsed(TimeUnit.MILLISECONDS);
    }

    @Override
    public long viaMongo() {
        return 0;
    }

    @Override
    public long viaHibernate() {
        Session session = sessionFactory.openSession();
        for (int i = counter; i < 50000; i++) {
            Environment environment = session.load(Environment.class, i);
            session.delete(environment);
        }
        Stopwatch timer = Stopwatch.createStarted();
        session.beginTransaction().commit();
        timer.stop();
        counter += 10000;
        return timer.elapsed(TimeUnit.MILLISECONDS);
    }
}
