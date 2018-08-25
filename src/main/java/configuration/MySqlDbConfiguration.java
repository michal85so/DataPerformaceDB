package configuration;

import domain.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class MySqlDbConfiguration {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/DataPerformanceDB");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        Properties properties = new Properties();
//        properties.put("defaultRowPrefetch", "10000");
//        properties.put("defaultBatchValue", "10000");
        dataSource.setConnectionProperties(properties);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        jdbcTemplate.setFetchSize(10000);
        return jdbcTemplate;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setAnnotatedClasses(Environment.class);
        sessionFactoryBean.setPackagesToScan(new String[] {"domain"});
        Properties properties = new Properties();
        properties.setProperty("dialect", "org.hibernate.dialect.MySQL55Dialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
//        properties.setProperty("hibernate.show_sql", "true");
//        properties.setProperty("hibernate.jdbc.fetch_size", "10000");
        sessionFactoryBean.setHibernateProperties(properties);
        return sessionFactoryBean;
    }
}
