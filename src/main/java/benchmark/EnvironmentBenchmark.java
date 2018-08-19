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
    int numberOfIterations = 10;

    public void runInserts() {
        run("inserts via jdbc prepared statement", () -> environmentController.insertsViaJdbcPreparedStatement());
        run("inserts via jdbc batch prepared statement", () -> environmentController.insertsViaJdbcBatch());
        run("inserts via jdbc template", () -> environmentController.insertsViaJdbcTemplate());
        run("inserts via mongodb", () -> environmentController.insertsViaMongo());
        run("inserts via hibernate", () -> environmentController.insertsViaHibernate());
    }

    public void runUpdates() {

    }

    public void runSelects() {

    }

    public void runDeletes() {

    }

    public void run(String text, Supplier<Long> function) {
        long counter = 0;
        IntStream.rangeClosed(1, 5).forEach(i -> System.out.println("running up test: " + function.get()));
        for (int i = 0; i < numberOfIterations; i++) {
            long singleExecutionTime = function.get();
            counter += singleExecutionTime;
            System.out.println(text + ": " + (singleExecutionTime));
        }
        System.out.println(text + "(avg): " + (counter / numberOfIterations));
    }
}
