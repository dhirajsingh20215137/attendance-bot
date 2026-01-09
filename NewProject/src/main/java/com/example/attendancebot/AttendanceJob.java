package com.example.attendancebot;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AttendanceJob {

    private final HrOneClient client;

    public AttendanceJob(HrOneClient client) {
        this.client = client;
    }

    // 09:15 AM IST → Check-in
    @Scheduled(cron = "0 15 9 * * *", zone = "Asia/Kolkata")
    public void checkIn() {
        String token = client.loginAndGetToken();
        client.markAttendance(token, "A");
        System.out.println("Checked IN at " + LocalDateTime.now());
//        notificationService.sendAttendanceMail("A");
    }

    // 06:00 PM IST → Check-out
    @Scheduled(cron = "0 0 18 * * *", zone = "Asia/Kolkata")
    public void checkOut() {
        String token = client.loginAndGetToken();
        client.markAttendance(token, "P");
        System.out.println("Checked OUT at " + LocalDateTime.now());
//        notificationService.sendAttendanceMail("P");
    }
}


