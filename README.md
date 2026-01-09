# Attendance Bot

Automated attendance marking bot for HROne that runs scheduled check-in/check-out via GitHub Actions.

## Features

- ✅ Automated check-in at 09:15 AM IST
- ✅ Automated check-out at 06:00 PM IST
- ✅ Manual API endpoint for testing
- ✅ Runs on GitHub Actions (no server needed)

## Quick Start

### Local Testing

```bash
export HRONE_USERNAME="your_email@example.com"
export HRONE_PASSWORD="your_password"
export HRONE_COOKIE="<optional cookie string>"

./gradlew bootRun
```

Test API:
```bash
curl -X POST "http://localhost:8080/attendance/manual?type=A"
```

### Deployment

See [DEPLOYMENT.md](./DEPLOYMENT.md) for detailed deployment instructions.

## Configuration

All configuration is done via environment variables:
- `HRONE_USERNAME` - Your HROne email
- `HRONE_PASSWORD` - Your HROne password
- `HRONE_COOKIE` - Optional browser cookie (recommended)

## API Endpoints

- `POST /attendance/manual?type=A` - Manual check-in
- `POST /attendance/manual?type=P` - Manual check-out

## Scheduled Jobs

- **Check-in**: 09:15 AM IST (via GitHub Actions cron)
- **Check-out**: 06:00 PM IST (via GitHub Actions cron)

## Tech Stack

- Java 17
- Spring Boot 3.3.2
- Gradle
- GitHub Actions

