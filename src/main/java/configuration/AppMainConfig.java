package configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MongoDbConfiguration.class)
@ComponentScan(basePackages = {"main","controller"})
public class AppMainConfig {
}
