package main;

import benchmark.EnvironmentBenchmark;
import controller.PersonController;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
class Runner {

    @Autowired
    private EnvironmentBenchmark environmentBenchmark;
    @Autowired
    private Cleaner cleaner;

    public void run() {
        cleaner.cleanAllData();

        environmentBenchmark.runInserts();
        environmentBenchmark.runUpdates();
        environmentBenchmark.runSelects();
        environmentBenchmark.runDeletes();
    }
}
