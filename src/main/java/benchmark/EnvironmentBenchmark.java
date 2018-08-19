package benchmark;

import controller.EnvironmentController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;
import java.util.stream.IntStream;

@Component
public class EnvironmentBenchmark {

    @Autowired
    private EnvironmentController environmentController;
    private static final int NUMBER_OF_ITERATIONS = 10;

    public void runInserts() {
        executeInserts("inserts via jdbc prepared statement", () -> environmentController.insertsViaJdbcPreparedStatement());
        executeInserts("inserts via jdbc batch prepared statement", () -> environmentController.insertsViaJdbcBatch());
        executeInserts("inserts via jdbc template", () -> environmentController.insertsViaJdbcTemplate());
        executeInserts("inserts via mongodb", () -> environmentController.insertsViaMongo());
        executeInserts("inserts via hibernate", () -> environmentController.insertsViaHibernate());
    }

    private void executeInserts(String text, Supplier<Long> function) {
        long counter = 0;
        IntStream.rangeClosed(1, 5).forEach(i -> System.out.println("running up test: " + function.get()));
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            long singleExecutionTime = function.get();
            counter += singleExecutionTime;
            System.out.println(text + ": " + (singleExecutionTime));
        }
        System.out.println(text + "(avg): " + (counter / NUMBER_OF_ITERATIONS));
    }

    public void runUpdates() {

    }

    public void runSelects() {

    }

    public void runDeletes() {

    }


}
