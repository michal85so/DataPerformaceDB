package benchmark;

import controller.DeleteEnvironmentController;
import controller.InsertEnvironmentController;
import controller.SelectEnvironmentController;
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
    @Autowired
    private SelectEnvironmentController selectEnvironmentController;
    @Autowired
    private DeleteEnvironmentController deleteEnvironmentController;

    private static final int NUMBER_OF_ITERATIONS = 10;
    private static final int NUMBER_OF_RUNNING_UP_TEST_ITERATIONS = 5;

    public void runInserts() {
        execute("inserts via jdbc prepared statement", () -> insertEnvironmentController.viaJdbcPreparedStatement());
        execute("inserts via jdbc batch prepared statement", () -> insertEnvironmentController.viaJdbcBatch());
        execute("inserts via jdbc template", () -> insertEnvironmentController.viaJdbcTemplate());
        execute("inserts via mongodb", () -> insertEnvironmentController.viaMongo());
        execute("inserts via hibernate", () -> insertEnvironmentController.viaHibernate());
    }

    public void runUpdates() {
        execute("updates via jdbc prepared statement", () -> updateEnvironmentController.viaJdbcPreparedStatement());
        execute("updates via jdbc batch prepared statement", () -> updateEnvironmentController.viaJdbcBatch());
        execute("updates via jdbc template", () -> updateEnvironmentController.viaJdbcTemplate());
        execute("updates via mongodb", () -> updateEnvironmentController.viaMongo());
        execute("updates via hibernate", () -> updateEnvironmentController.viaHibernate());
    }

    public void runSelects() {
        execute("selects via jdbc prepared statement", () -> selectEnvironmentController.viaJdbcPreparedStatement());
        execute("selects via jdbc batch prepared statement", () -> selectEnvironmentController.viaJdbcBatch());
        execute("selects via jdbc template", () -> selectEnvironmentController.viaJdbcTemplate());
        execute("selects via mongodb", () -> selectEnvironmentController.viaMongo());
        execute("selects via hibernate", () -> selectEnvironmentController.viaHibernate());
    }

    public void runDeletes() {
        execute("deletes via jdbc prepared statement", () -> deleteEnvironmentController.viaJdbcPreparedStatement());
        execute("deletes via jdbc batch prepared statement", () -> deleteEnvironmentController.viaJdbcBatch());
        execute("deletes via jdbc template", () -> deleteEnvironmentController.viaJdbcTemplate());
        execute("deletes via mongodb", () -> deleteEnvironmentController.viaMongo());
        execute("deletes via hibernate", () -> deleteEnvironmentController.viaHibernate());
    }

    private void execute(String text, Supplier<Long> function) {
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
}
