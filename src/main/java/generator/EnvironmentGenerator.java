package generator;

import domain.Environment;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class EnvironmentGenerator implements Generator<Environment> {
    private static int startNumber = 0;

    public List<Environment> generateANumber(int number) {
        int to = startNumber + number;
        int from = startNumber + 1;
        startNumber += number;

        return IntStream
                .rangeClosed(from, to)
                .mapToObj(this::createEntity)
                .collect(Collectors.toList());
    }

    private Environment createEntity(int id) {
        return new Environment(id, "producer" + id, "category" + id, "model" + id, id, id, id, id);
    }
}
