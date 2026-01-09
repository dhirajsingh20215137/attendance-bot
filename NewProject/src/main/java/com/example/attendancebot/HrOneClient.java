package com.example.attendancebot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class HrOneClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${hrone.username}")
    private String username;

    @Value("${hrone.password}")
    private String password;

    @Value("${hrone.employeeId}")
    private int employeeId;

    /**
     * Optional: full Cookie header value copied from browser, e.g.
     * "JwtTokenCookie=...; RefreshTokenCookie=..."
     */
    @Value("${hrone.cookie:}")
    private String cookieHeader;

    public String loginAndGetToken() {

        String url = "https://gateway.app.hrone.cloud/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", username);
        body.add("password", password);
        body.add("grant_type", "password");
        body.add("loginType", "1");
        body.add("companyDomainCode", "handyonline");

        HttpEntity<?> request = new HttpEntity<>(body, headers);

        Map<?, ?> response =
                restTemplate.postForObject(url, request, Map.class);

        return (String) response.get("access_token");
    }

    public void markAttendance(String token, String requestType) {

        String url =
                "https://app.hrone.cloud/api/timeoffice/mobile/checkin/Attendance/Request";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        headers.add("domaincode", "handyonline");

        // Match working browser curl as closely as possible
        headers.add("accept", "application/json, text/plain, */*");
        headers.add("accept-language", "en-GB,en-US;q=0.9,en;q=0.8");
        headers.add("accessmode", "W");
        headers.add("cache-control", "no-cache");
        headers.add("pragma", "no-cache");
        headers.add("origin", "https://app.hrone.cloud");
        headers.add("referer", "https://app.hrone.cloud/app");
        headers.add("priority", "u=1, i");
        headers.add("sec-ch-ua", "\"Chromium\";v=\"142\", \"Google Chrome\";v=\"142\", \"Not_A Brand\";v=\"99\"");
        headers.add("sec-ch-ua-mobile", "?0");
        headers.add("sec-ch-ua-platform", "\"macOS\"");
        headers.add("sec-fetch-dest", "empty");
        headers.add("sec-fetch-mode", "cors");
        headers.add("sec-fetch-site", "same-origin");
        headers.add("x-requested-with", "https://app.hrone.cloud");
        headers.add(
                "user-agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/142.0.0.0 Safari/537.36"
        );
        // If user has provided browser cookies, send them as-is
        if (cookieHeader != null && !cookieHeader.isBlank()) {
            headers.add("Cookie", cookieHeader);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("requestType", requestType); // A = IN, P = OUT
        body.put("applyRequestSource", 10);
        body.put("employeeId", employeeId);
        body.put("latitude", "");
        body.put("longitude", "");
        body.put("geoAccuracy", "");
        body.put("geoLocation", "");
        body.put("remarks", "");
        body.put("uploadedPhotoOneName", "");
        body.put("uploadedPhotoOnePath", "");
        body.put("uploadedPhotoTwoName", "");
        body.put("uploadedPhotoTwoPath", "");
        body.put("attendanceSource", "W");
        body.put("attendanceType", "Online");

        // Match punchTime format: 2026-01-09T14:13 (IST, no seconds)
        ZonedDateTime istTime =
                ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        String punchTime =
                istTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        body.put("punchTime", punchTime);

        HttpEntity<?> entity = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(url, entity, String.class);
    }
}


