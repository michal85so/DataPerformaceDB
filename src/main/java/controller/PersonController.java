package controller;

import domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.PersonRepository;

@Component
public class PersonController {

    @Autowired
    PersonRepository repository;

    public void save() {
        Person person = new Person();
        repository.insert(person);
    }
}
