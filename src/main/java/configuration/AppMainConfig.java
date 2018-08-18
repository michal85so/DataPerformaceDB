package configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({MongoDbConfiguration.class, MySqlDbConfiguration.class})
@ComponentScan(basePackages = {"main", "controller", "generator", "benchmark"})
public class AppMainConfig {
}
