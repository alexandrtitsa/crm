package ua.com.astone.acrm;

import org.springframework.boot.SpringApplication;

public class TestACrmApplication {

    public static void main(String[] args) {
        SpringApplication.from(ACrmApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
