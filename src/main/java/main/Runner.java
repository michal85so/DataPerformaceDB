package main;

import controller.PersonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Runner {

    @Autowired
    PersonController personController;

    public void run() {
        personController.save();
    }
}
