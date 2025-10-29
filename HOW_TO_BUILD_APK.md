# How to Build the CyberLens APK

Building Android APKs on Replit's cloud environment is challenging due to Android SDK requirements. Here are your best options:

## ✅ Option 1: Build on Your Computer (Recommended)

### Prerequisites
- Download and install [Android Studio](https://developer.android.com/studio)
- Your computer needs at least 8GB RAM and 10GB free disk space

### Steps
1. **Download this project**
   - Click the three dots menu in Replit
   - Select "Download as zip"
   - Extract the zip file to your computer

2. **Open in Android Studio**
   - Launch Android Studio
   - Click "Open an Existing Project"
   - Select the extracted CyberLens folder
   - Wait for Gradle to sync (first time takes 5-10 minutes)

3. **Build the APK**
   - Click **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
   - Wait for build to complete (you'll see a notification)
   - Click "locate" in the notification to find your APK
   - APK location: `app/build/outputs/apk/debug/app-debug.apk`

4. **Install on your phone**
   - Transfer the APK to your Android phone
   - Enable "Install unknown apps" in Settings
   - Tap the APK file to install

---

## ✅ Option 2: Use GitHub Actions (Free Cloud Build)

### Steps
1. **Push to GitHub**
   - Create a new GitHub repository
   - Push this project code to GitHub

2. **Create GitHub Actions Workflow**
   - In your repo, create `.github/workflows/build.yml`:
   
```yaml
name: Build Android APK

on:
  push:
    branches: [ main ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          
      - name: Grant execute permission for gradlew
        run: chmod +x gradlew
        
      - name: Build Debug APK
        run: ./gradlew assembleDebug
        
      - name: Upload APK
        uses: actions/upload-artifact@v3
        with:
          name: app-debug
          path: app/build/outputs/apk/debug/app-debug.apk
```

3. **Download APK**
   - Go to Actions tab in GitHub
   - Click on your workflow run
   - Download the APK artifact

---

## ✅ Option 3: Command Line Build (Linux/Mac)

If you have Linux or Mac with Java installed:

```bash
# 1. Download Android SDK command line tools
wget https://dl.google.com/android/repository/commandlinetools-linux-latest.zip
mkdir -p ~/Android/Sdk/cmdline-tools
unzip commandlinetools-linux-latest.zip -d ~/Android/Sdk/cmdline-tools
mv ~/Android/Sdk/cmdline-tools/cmdline-tools ~/Android/Sdk/cmdline-tools/latest

# 2. Set environment variables
export ANDROID_HOME=~/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin

# 3. Install SDK packages
yes | ~/Android/Sdk/cmdline-tools/latest/bin/sdkmanager --licenses
~/Android/Sdk/cmdline-tools/latest/bin/sdkmanager "platform-tools" "platforms;android-33" "build-tools;33.0.0"

# 4. Build APK
cd /path/to/CyberLens
chmod +x gradlew
./gradlew assembleDebug

# 5. Find APK
ls -lh app/build/outputs/apk/debug/app-debug.apk
```

---

## ✅ Option 4: Online Build Services

### AppGyver / BuildAppe
1. Upload your project as ZIP
2. Configure build settings
3. Download APK when ready

### Expo EAS (for React Native apps)
- Not applicable for this native Kotlin app

---

## 📱 Before Installing

1. **Update Backend URL**
   - Open `app/src/main/java/com/example/cyberlens/ScannerClient.kt`
   - Change the backend URL to your Replit URL:
   ```kotlin
   .baseUrl("YOUR_REPLIT_URL_HERE/")
   ```

2. **Enable Installation**
   - On Android: Settings → Security → Install unknown apps → Enable for your file manager

---

## ⚠️ Why Not Build on Replit?

Building Android APKs requires:
- Android SDK (5-10 GB download)
- Build tools and platform packages
- Gradle dependencies (hundreds of MB)
- Java 17+ and significant RAM

Replit's environment has limitations:
- Storage constraints
- Memory limits
- Build timeout restrictions
- Missing Android emulator/tools

**Best practice:** Use Android Studio locally or GitHub Actions for free cloud builds.

---

## 🎯 Quick Start (Easiest Path)

1. Download Android Studio (one-time setup, ~1 GB)
2. Open this project in Android Studio
3. Click Build → Build APK
4. Done! You have your APK

**Build time:** 5-10 minutes first time, 30 seconds after that.
