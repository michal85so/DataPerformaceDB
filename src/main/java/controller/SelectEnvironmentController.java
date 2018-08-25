package controller;

import com.google.common.base.Stopwatch;
import domain.Environment;
import org.hibernate.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SelectEnvironmentController extends AbstractEnvironmentController {

    private final String SELECT_STATEMENT = "select * from environment where id = ?";
    private final int NUMBER_OF_SELECTS = 10000;
    private List<Integer> ids = new ArrayList<Integer>(NUMBER_OF_SELECTS) {{
            Random random = new Random(NUMBER_OF_SELECTS);
            ArrayList<Integer> list = new ArrayList<>();
            for (int i = 0; i < NUMBER_OF_SELECTS; i++)
                list.add(random.nextInt());
    }};

    @Override
    public long viaJdbcPreparedStatement() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_STATEMENT)) {
                Stopwatch timer = Stopwatch.createStarted();
                for (Integer id : ids) {
                    statement.setInt(1, id);
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

    @Override
    public long viaJdbcBatch() {
        return 0;
    }

    @Override
    public long viaJdbcTemplate() {
        Stopwatch timer = Stopwatch.createStarted();
        for (Integer id : ids) {
            jdbcTemplate.queryForObject(SELECT_STATEMENT, Environment.class, id);
        }
        timer.stop();
        return timer.elapsed(TimeUnit.MILLISECONDS);
    }

    @Override
    public long viaMongo() {
        return 0;
    }

    @Override
    public long viaHibernate() {
        Session session = sessionFactory.openSession();
        Stopwatch timer = Stopwatch.createStarted();
        for (Integer id : ids) {
            session.find(Environment.class, id);
        }
        timer.stop();
        return timer.elapsed(TimeUnit.MILLISECONDS);
    }
}
