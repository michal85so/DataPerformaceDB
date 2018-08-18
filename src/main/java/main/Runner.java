package main;

import benchmark.EnvironmentBenchmark;
import controller.PersonController;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class Runner {

    @Autowired
    PersonController personController;

    @Autowired
    private EnvironmentBenchmark environmentBenchmark;

    @Autowired
    private SessionFactory sessionFactory;

    public void run() {
//        personController.save();
        try {
            environmentBenchmark.testInsertsViaJdbcPreparedStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
