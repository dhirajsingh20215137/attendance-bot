package com.example.attendancebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class AttendanceBotApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AttendanceBotApplication.class);
    private final HrOneClient client;

    public AttendanceBotApplication(HrOneClient client) {
        this.client = client;
    }

    public static void main(String[] args) {
        log.info("🚀 Attendance Bot Application Starting...");
        SpringApplication.run(AttendanceBotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        String mode = System.getenv("ATT_MODE");
        log.info("➡ ATT_MODE received: {}", mode);

        String token = client.loginAndGetToken();
        log.info("🔐 Login Token: {}", token != null ? "Received" : "NULL TOKEN!");

        if ("IN".equalsIgnoreCase(mode)) {
            log.info("🟢 Marking IN attendance...");
            client.markAttendance(token, "A");
            log.info("🏁 Finished IN attempt");
        } 
        else if ("OUT".equalsIgnoreCase(mode)) {
            log.info("🔵 Marking OUT attendance...");
            client.markAttendance(token, "P");
            log.info("🏁 Finished OUT attempt");
        } 
        else {
            log.error("❌ ERROR: Unknown attendance mode '{}'", mode);
        }

        log.info("🛑 Bot execution completed. Exiting.");
        System.exit(0);
    }
}
