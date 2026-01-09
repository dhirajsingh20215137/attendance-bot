package com.example.attendancebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class AttendanceBotApplication implements CommandLineRunner {

    private final HrOneClient client;

    public AttendanceBotApplication(HrOneClient client) {
        this.client = client;
    }

    public static void main(String[] args) {
        SpringApplication.run(AttendanceBotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Determine whether this run is IN or OUT based on argument
        String mode = System.getenv("ATT_MODE");  // passed from workflow

        String token = client.loginAndGetToken();

        if ("IN".equalsIgnoreCase(mode)) {
            client.markAttendance(token, "A");   // A = IN
            System.out.println("CHECKED IN");
        } else if ("OUT".equalsIgnoreCase(mode)) {
            client.markAttendance(token, "P");   // P = OUT
            System.out.println("CHECKED OUT");
        } else {
            System.out.println("Unknown mode.");
        }

        // exit properly
        System.exit(0);
    }
}


