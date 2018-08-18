package controller;

import domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.PersonRepository;

import javax.sql.DataSource;

@Component
public class PersonController {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private DataSource dataSource;

    public void save() {
        Person person = new Person();
        repository.insert(person);
    }
}
