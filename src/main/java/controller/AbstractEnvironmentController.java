package controller;

import generator.EnvironmentGenerator;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import repository.EnvironmentRepository;

import javax.sql.DataSource;

abstract class AbstractEnvironmentController implements Executes {

    @Autowired
    EnvironmentGenerator environmentGenerator;
    @Autowired
    EnvironmentRepository environmentMongoRepository;
    @Autowired
    DataSource dataSource;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    SessionFactory sessionFactory;
}
