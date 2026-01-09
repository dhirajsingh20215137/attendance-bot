package com.example.attendancebot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private static final Logger log = LogManager.getLogger(AttendanceController.class);
    private final HrOneClient client;

    public AttendanceController(HrOneClient client) {
        this.client = client;
    }

    /**
     * Trigger attendance manually for testing.
     * requestType: "A" = check-in, "P" = check-out.
     *
     * Examples:
     *  - POST /attendance/manual?type=A
     *  - POST /attendance/manual?type=P
     */
    @PostMapping("/manual")
    public ResponseEntity<String> triggerAttendance(@RequestParam("type") String requestType) {
        String upperType = requestType == null ? "" : requestType.trim().toUpperCase();
        log.info("Triggering attendance for type: " + upperType);
        if (!upperType.equals("A") && !upperType.equals("P")) {
            return ResponseEntity.badRequest().body("Invalid type. Use 'A' for IN or 'P' for OUT.");
        }

        String token = client.loginAndGetToken();
        client.markAttendance(token, upperType);
        return ResponseEntity.ok("Attendance request sent for type: " + upperType);
    }
}


