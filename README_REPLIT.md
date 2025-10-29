# CyberLens on Replit - Quick Start Guide

## 🎉 Your Backend Server is Running!

This Replit provides a **Flask backend server** for the CyberLens Android app. The backend:
- ✅ Receives network traffic data from the Android app
- ✅ Performs security analysis on captured hosts/domains
- ✅ Provides a beautiful web dashboard to view results
- ✅ Exposes REST API endpoints

## 📱 How to Use the Complete System

### Step 1: View the Dashboard (Right Now!)
Click the **Webview** tab above to see the CyberLens dashboard. It's already running and waiting for data from the Android app.

### Step 2: Set Up the Android App (On Your Computer)
1. **Download this project** from Replit to your local machine
2. **Open the project in Android Studio**
3. **Update the backend URL** in the Android app:
   - Open file: `app/src/main/java/com/example/cyberlens/ScannerClient.kt`
   - Change the `baseUrl` to your Replit URL:
     ```kotlin
     .baseUrl("YOUR_REPLIT_URL_HERE/")  // e.g., https://abc123.repl.co/
     ```
   - Get your Replit URL from the address bar of the webview

4. **Build and run** the Android app on your device or emulator
5. **Accept VPN permissions** when prompted
6. **Use apps and browse websites** on your Android device
7. **Watch the dashboard** - captured hosts will appear in real-time!

## 🔧 API Endpoints

The backend provides these endpoints:

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/` | GET | Web dashboard (view in browser) |
| `/api/info` | GET | API information |
| `/api/scan-batch` | POST | Submit hosts for analysis (used by Android app) |
| `/api/history` | GET | Get scan history (supports `?limit=N` parameter) |
| `/api/stats` | GET | Get statistics about captured data |

## 📊 Dashboard Features

The web dashboard shows:
- **Total Scans**: Number of hosts analyzed
- **Risk Breakdown**: High, Medium, and Low risk counts
- **Scan History**: Real-time list of captured hosts with security ratings
- **Auto-refresh**: Updates every 10 seconds automatically

## 🔒 Security Analysis

The backend performs basic security analysis:
- Detects suspicious keywords (malware, phishing, trackers)
- Flags unusual TLDs (.xyz, .top, .click, etc.)
- Identifies tracking/analytics domains
- Assigns risk ratings from 1-10

## 🚀 Publishing to Production

When you're ready to deploy:
1. Click the **Deploy** button in Replit
2. Your backend will be live with a permanent URL
3. Update the Android app's `baseUrl` to use the deployment URL
4. The backend uses Gunicorn for production-ready performance

## ⚠️ Important Notes

- The Android app requires **Android Studio** and a physical device or emulator
- The app uses **VPN permissions** to capture network traffic
- Only use on devices you own or have permission to monitor
- This is a research/educational tool, not production-ready for commercial use

## 📝 Example Request

To test the API, send a POST request:
```bash
curl -X POST https://YOUR_REPLIT_URL/api/scan-batch \
  -H "Content-Type: application/json" \
  -d '{"targets": ["example.com", "google.com", "suspicious-ads.xyz"]}'
```

## 🛠️ Development

- **Backend code**: `backend/app.py`
- **Dashboard template**: `backend/templates/dashboard.html`
- **Python dependencies**: `requirements.txt`
- **Android app code**: `app/src/main/java/com/example/cyberlens/`

## 📚 More Information

See `replit.md` for detailed project documentation.

---

**Ready to start?** Check out the webview to see your dashboard! 🎯
