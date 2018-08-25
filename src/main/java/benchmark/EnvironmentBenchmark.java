package benchmark;

import controller.InsertEnvironmentController;
import controller.UpdateEnvironmentController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;
import java.util.stream.IntStream;

@Component
public class EnvironmentBenchmark {

    @Autowired
    private InsertEnvironmentController insertEnvironmentController;
    @Autowired
    private UpdateEnvironmentController updateEnvironmentController;
    private static final int NUMBER_OF_ITERATIONS = 1;
    private static final int NUMBER_OF_RUNNING_UP_TEST_ITERATIONS = 2;

    public void runInserts() {
        executeInserts("inserts via jdbc prepared statement", () -> insertEnvironmentController.viaJdbcPreparedStatement());
        executeInserts("inserts via jdbc batch prepared statement", () -> insertEnvironmentController.viaJdbcBatch());
//        executeInserts("inserts via jdbc template", () -> insertEnvironmentController.viaJdbcTemplate());
//        executeInserts("inserts via mongodb", () -> insertEnvironmentController.viaMongo());
//        executeInserts("inserts via hibernate", () -> insertEnvironmentController.viaHibernate());
    }

    private void executeInserts(String text, Supplier<Long> function) {
        long counter = 0;
        IntStream.rangeClosed(1, NUMBER_OF_RUNNING_UP_TEST_ITERATIONS)
                .forEach(i -> System.out.println("running up test: " + function.get()));
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            long singleExecutionTime = function.get();
            counter += singleExecutionTime;
            System.out.println(text + ": " + (singleExecutionTime));
        }
        System.out.println(text + "(avg): " + (counter / NUMBER_OF_ITERATIONS));
    }

    public void runUpdates() {
//        executeInserts("updates via jdbc prepared statement", () -> updateEnvironmentController.updatesViaJdbcPreparedStatement());
//        executeInserts("updates via jdbc batch prepared statement", () -> updateEnvironmentController.updatesViaJdbcBatch());
//        executeInserts("updates via jdbc template", () -> updateEnvironmentController.updatesViaJdbcTemplate());
//        executeInserts("updates via hibernate", () -> updateEnvironmentController.viaHibernate());
    }

    public void runSelects() {

    }

    public void runDeletes() {

    }


}
