package controller;

import generator.EnvironmentGenerator;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import repository.EnvironmentNoSQLRepository;

import javax.sql.DataSource;

abstract class AbstractEnvironmentController implements Executes {

    @Autowired
    EnvironmentGenerator environmentGenerator;
    @Autowired
    EnvironmentNoSQLRepository environmentMongoRepository;
    @Autowired
    DataSource dataSource;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    SessionFactory sessionFactory;
}
