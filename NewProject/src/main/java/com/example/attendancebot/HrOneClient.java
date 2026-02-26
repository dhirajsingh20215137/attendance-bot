package com.example.attendancebot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class HrOneClient {

    private static final Logger log = LoggerFactory.getLogger(HrOneClient.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${hrone.username}")
    private String username;

    @Value("${hrone.password}")
    private String password;

    @Value("${hrone.employeeId}")
    private int employeeId;

    @Value("${hrone.cookie:}")
    private String cookieHeader;

    public String loginAndGetToken() {

        log.info("➡ Starting login request to HROne...");
        String url = "https://gateway.app.hrone.cloud/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", username);
        body.add("password", password);
        body.add("grant_type", "password");
        body.add("loginType", "1");
        body.add("companyDomainCode", "handyonline");

        log.info("📤 Login request body: {}", body);

        HttpEntity<?> request = new HttpEntity<>(body, headers);

        Map<?, ?> response = restTemplate.postForObject(url, request, Map.class);
        log.info("✅ Login response: {}", response);

        return (String) response.get("access_token");
    }



    public void markAttendance(String token, String requestType) {

    log.info("➡ Starting attendance marking...");
    log.info("➡ Request Type (A=IN, P=OUT): {}", requestType);

    String url = "https://app.hrone.cloud/api/timeoffice/mobile/checkin/Attendance/Request";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);
    headers.add("domaincode", "handyonline");

    headers.add("accept", "application/json, text/plain, */*");
    headers.add("origin", "https://app.hrone.cloud");
    headers.add("referer", "https://app.hrone.cloud/app");

    if (cookieHeader != null && !cookieHeader.isBlank()) {
        headers.add("Cookie", cookieHeader);
        log.info("🍪 Attached Cookie Header");
    }

    ZonedDateTime istTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
    String punchTime = istTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

    Map<String, Object> body = new HashMap<>();
    body.put("applyRequestSource", 10);
    body.put("attendanceSource", "W");        // Added
    body.put("attendanceType", "Online");     // Added
    body.put("employeeId", employeeId);
    body.put("geoAccuracy", "");
    body.put("geoLocation", "");
    body.put("latitude", "");
    body.put("longitude", "");
    body.put("punchTime", punchTime);
    body.put("remarks", "");
    body.put("requestType", requestType);

    // Newly required photo fields
    body.put("uploadedPhotoOneName", "");
    body.put("uploadedPhotoOnePath", "");
    body.put("uploadedPhotoTwoName", "");
    body.put("uploadedPhotoTwoPath", "");

    log.info("🕒 Punch Time: {}", punchTime);
    log.info("📤 Attendance body: {}", body);

    HttpEntity<?> entity = new HttpEntity<>(body, headers);

    try {
        var response = restTemplate.postForEntity(url, entity, String.class);
        log.info("✅ Attendance API Response: {}", response.getBody());
    } catch (HttpClientErrorException ex) {
        log.error("❌ HRONE returned an ERROR");
        log.error("Status: {}", ex.getStatusCode());
        log.error("Response Body: {}", ex.getResponseBodyAsString());
    } catch (Exception ex) {
        log.error("❌ Unexpected error while marking attendance", ex);
    }
}
}
