# CyberLens Android Application

## Project Overview
**Type:** Android Mobile Application (Kotlin)  
**Purpose:** Network traffic monitoring app that uses Android VPN Service to capture destination IPs and DNS names  
**Last Updated:** October 29, 2025

## What This Project Is
This is a complete Android application written in Kotlin that:
- Uses Android's VpnService to create a local VPN and intercept network traffic
- Captures destination IP addresses and DNS names from device traffic
- Can forward captured data to a backend API for security analysis
- Displays captured hosts in a real-time list within the app

## Running on Replit

### Backend Server (✅ Ready to Use)
This Replit now includes a **Flask backend server** that:
- Receives network traffic data from the CyberLens Android app
- Performs basic security analysis on captured hosts
- Provides a web dashboard to view captured data in real-time
- Exposes REST API endpoints for the Android app

The backend server is **running now** on Replit and accessible via the web preview!

### Android Mobile App (Requires Android Studio)
⚠️ The Android app itself cannot run on Replit (requires Android device/emulator).

To use the complete CyberLens system:
1. **Keep this Replit running** - The backend server stays active
2. **Download the Android app code** to your local machine
3. **Open in Android Studio** and build the APK
4. **Configure the app** to connect to this Replit's backend URL
5. **Run on Android device/emulator** and start capturing traffic

### Quick Start
1. ✅ Backend is already running on Replit (view the web preview)
2. 📱 To use Android app: Download project → Open in Android Studio → Update backend URL in `ScannerClient.kt`

## Project Structure
```
CyberLens/
├── backend/                             # Flask Backend Server (NEW)
│   ├── app.py                           # Main Flask application
│   └── templates/
│       └── dashboard.html               # Web dashboard
├── app/                                 # Android Application
│   ├── src/main/
│   │   ├── java/com/example/cyberlens/
│   │   │   ├── MainActivity.kt          # Main UI activity
│   │   │   ├── CaptureVpnService.kt     # VPN service for traffic capture
│   │   │   └── ScannerClient.kt         # Retrofit API client
│   │   ├── res/                         # Android resources (layouts, strings)
│   │   └── AndroidManifest.xml          # App permissions and components
│   └── build.gradle                     # App-level Gradle config
├── build.gradle                         # Project-level Gradle config
├── settings.gradle                      # Gradle project settings
├── requirements.txt                     # Python dependencies
└── gradle.properties                    # Gradle properties
```

## Technology Stack

### Backend (Running on Replit)
- **Language:** Python 3.11
- **Framework:** Flask 3.0
- **Libraries:** Flask-CORS, Gunicorn
- **Features:** REST API, Web Dashboard, Security Analysis

### Android App (Run locally)
- **Language:** Kotlin 1.8.21
- **Build System:** Gradle 8.1.0
- **Target SDK:** Android 13 (API 33)
- **Min SDK:** Android 5.0 (API 21)
- **Key Libraries:**
  - AndroidX Core & AppCompat
  - Kotlin Coroutines (async operations)
  - Retrofit 2 + OkHttp (HTTP client)
  - Gson (JSON serialization)
  - Material Design Components

## Key Features
1. **VPN-based Traffic Capture:** Creates a local VPN to intercept outgoing connections
2. **Real-time Host Display:** Shows captured domains/IPs in a ListView
3. **Backend Integration:** Can send data to a backend API for analysis
4. **Permission Handling:** Properly requests VPN permissions from user

## Configuration

### Backend API Endpoints
The Flask backend provides these endpoints:
- `GET /` - Web dashboard (view in browser)
- `GET /api/info` - API information
- `POST /api/scan-batch` - Submit hosts for analysis (used by Android app)
- `GET /api/history` - Get scan history
- `GET /api/stats` - Get statistics

### Connecting Android App to Replit Backend
Update `ScannerClient.kt` with your Replit URL:

```kotlin
// In app/src/main/java/com/example/cyberlens/ScannerClient.kt
object ScannerClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://your-replit-url.repl.co/")  // Change this!
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(ScannerApi::class.java)
}
```

Get your Replit URL from the webview preview at the top of this page.

## Security & Privacy Notes
⚠️ **Important Warnings:**
- This app intercepts all device network traffic via VPN
- Only use on devices you own or have explicit permission to monitor
- Encrypted DNS (DoH/DoT) may bypass capture
- Apps with custom DNS may not be captured
- This is a demonstration/research tool, not production-ready

## Build Instructions (for Android Studio)
1. Open project in Android Studio
2. Sync Gradle dependencies (automatically downloads Android SDK components)
3. Configure backend URL in `ScannerClient.kt`
4. Connect device or start emulator
5. Click Run → Run 'app'
6. Accept VPN permission dialog in the app
7. Visit websites/use apps to generate traffic

## Recent Changes
- Initial import: October 29, 2025

## User Preferences
None specified yet.

## Potential Enhancements
If you want to use this project on Replit, consider:
1. **Adding a Web Backend:** Create a separate backend service (Flask/Node.js) that:
   - Receives captured traffic data from the Android app
   - Performs security analysis
   - Provides a web dashboard to view results
2. **Web-based Alternative:** Build a browser extension or web app that monitors network requests (limited to browser traffic only)
3. **Documentation Site:** Create a static website documenting the app and security research
