# Deployment Guide - Attendance Bot

## Step-by-Step GitHub Deployment

### Step 1: Initialize Git Repository

```bash
cd /Users/userongrid/Desktop/Attendence_bot_main/NewProject

# Initialize git
git init

# Add all files
git add .

# Make initial commit
git commit -m "Initial commit: Attendance bot with scheduled jobs"
```

### Step 2: Create GitHub Repository

1. Go to [GitHub.com](https://github.com) and sign in
2. Click the **"+"** icon in the top right → **"New repository"**
3. Repository name: `attendance-bot` (or any name you prefer)
4. Description: "Automated attendance marking bot for HROne"
5. **Visibility**: Choose **Private** (recommended, since it contains credentials setup)
6. **DO NOT** check "Add a README file" (we already have files)
7. Click **"Create repository"**

### Step 3: Connect Local Repository to GitHub

After creating the repo, GitHub will show you commands. Use these:

```bash
# Add remote (replace YOUR_USERNAME with your GitHub username)
git remote add origin https://github.com/YOUR_USERNAME/attendance-bot.git

# Rename branch to main (if needed)
git branch -M main

# Push code to GitHub
git push -u origin main
```

**Note**: You'll be prompted for GitHub username and password/token.

### Step 4: Set Up GitHub Secrets (CRITICAL)

1. Go to your repository on GitHub: `https://github.com/YOUR_USERNAME/attendance-bot`
2. Click **Settings** (top menu)
3. In the left sidebar, click **"Secrets and variables"** → **"Actions"**
4. Click **"New repository secret"** and add these secrets:

   **Secret 1: HRONE_USERNAME**
   - Name: `HRONE_USERNAME`
   - Value: `dhiraj.kumar@ongrid.in`
   - Click **"Add secret"**

   **Secret 2: HRONE_PASSWORD**
   - Name: `HRONE_PASSWORD`
   - Value: `Dk@motihari1`
   - Click **"Add secret"**

   **Secret 3: HRONE_COOKIE** (Optional but recommended)
   - Name: `HRONE_COOKIE`
   - Value: `<paste your full Cookie string from browser>`
   - Click **"Add secret"**

### Step 5: Verify GitHub Actions Workflow

1. Go to your repository on GitHub
2. Click the **"Actions"** tab (top menu)
3. You should see **"Attendance Bot"** workflow listed
4. The workflow will run automatically at:
   - **09:15 IST** (03:45 UTC) - Check-in
   - **18:00 IST** (12:30 UTC) - Check-out

### Step 6: Test Manually (Optional)

You can manually trigger the workflow:

1. Go to **Actions** tab
2. Click **"Attendance Bot"** workflow
3. Click **"Run workflow"** button (right side)
4. Click **"Run workflow"** to trigger it immediately

### Step 7: Monitor Execution

1. Go to **Actions** tab
2. Click on any workflow run to see:
   - Logs
   - Execution status
   - Any errors

---

## Important Notes

### ⚠️ Security
- **Never commit** `application.yml` with hardcoded passwords (already fixed)
- All credentials are stored as **GitHub Secrets** (encrypted)
- Repository should be **Private** if possible

### ⏰ Scheduled Times
- **Check-in**: 09:15 AM IST (03:45 UTC)
- **Check-out**: 06:00 PM IST (12:30 UTC)

### 🔧 Troubleshooting

**Workflow fails?**
- Check **Actions** tab → Click failed run → Check logs
- Verify secrets are set correctly
- Ensure cron times are correct (GitHub uses UTC)

**Attendance not marked?**
- Check HROne API response in workflow logs
- Verify `HRONE_COOKIE` secret is set (if required)
- Check if HROne validation rules are blocking (device/location checks)

**Want to change schedule?**
- Edit `.github/workflows/attendance.yml`
- Update cron expressions (use UTC time)
- Commit and push changes

---

## Local Testing (Before Deployment)

Test locally first:

```bash
export HRONE_USERNAME="dhiraj.kumar@ongrid.in"
export HRONE_PASSWORD="Dk@motihari1"
export HRONE_COOKIE="<your cookie>"

./gradlew bootRun
```

Then test API:
```bash
curl -X POST "http://localhost:8080/attendance/manual?type=A"
```

---

## Next Steps After Deployment

1. ✅ Monitor first few automated runs
2. ✅ Check HROne dashboard to verify attendance is marked
3. ✅ Adjust cron times if needed
4. ✅ Consider adding error notifications (optional)

