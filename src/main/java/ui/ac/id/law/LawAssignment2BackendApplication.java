package ui.ac.id.law;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import ui.ac.id.law.service.EmailSenderService;

import java.io.IOException;

@SpringBootApplication
public class LawAssignment2BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LawAssignment2BackendApplication.class, args);
    }
}
