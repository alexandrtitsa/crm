package ua.com.astone.acrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ACrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(ACrmApplication.class, args);
    }

}
