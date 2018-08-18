package main;

import configuration.AppMainConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        Runner runner = main.registerContext();
        runner.run();
    }

    private Runner registerContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppMainConfig.class);
        context.refresh();
        return (Runner) context.getBean("runner");
    }
}
