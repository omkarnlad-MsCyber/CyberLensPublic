# 📱 How to Get Your CyberLens APK File

## 🎯 Quick Summary

**You cannot build Android APKs directly on Replit** because it requires Android SDK (5-10 GB), build tools, and significant resources. However, I've set up everything you need to build it yourself or use cloud services!

---

## ✅ Step 1: Access the Download Page

**👉 Go to: `/download` on this Replit** (click the button below in your browser)

The download page provides:
- **Source code package** (17KB download)
- **Detailed build instructions**  
- **3 different build options** to choose from
- **All necessary configuration files**

---

## 📦 What You'll Get

When you download the source code, you'll receive:
- ✅ Complete Android app source code (Kotlin)
- ✅ Flask backend (already running here on Replit)
- ✅ Build configuration files
- ✅ Step-by-step instructions (`HOW_TO_BUILD_APK.md`)
- ✅ All documentation

---

## 🛠️ Build Options (Choose What Works Best For You)

### Option 1: Android Studio (RECOMMENDED - Most Reliable)
**Best for:** Most users, easiest option  
**Time:** 10 minutes first build, 30 seconds after  
**Cost:** Free

**Steps:**
1. Download & install [Android Studio](https://developer.android.com/studio)
2. Download source code from `/download` page
3. Extract and open in Android Studio
4. Click Build → Build APK
5. Done! APK is in `app/build/outputs/apk/debug/`

### Option 2: GitHub Actions (FREE Cloud Build)
**Best for:** Users who don't want to install Android Studio  
**Time:** 5-10 minutes per build  
**Cost:** Free (GitHub provides 2000 build minutes/month)

**Steps:**
1. Download source code
2. Create GitHub repository and push code
3. Add GitHub Actions workflow (instructions included)
4. GitHub builds the APK in the cloud
5. Download from Actions tab

### Option 3: Command Line (Linux/Mac)
**Best for:** Developers comfortable with terminal  
**Time:** 15-20 minutes setup, 2-3 minutes per build  
**Cost:** Free

Requires manual Android SDK installation. Full commands provided in download package.

---

## 📝 Before You Build - Important!

### Update the Backend URL

Before building, you need to configure the app to connect to THIS Replit backend:

**File to edit:** `app/src/main/java/com/example/cyberlens/ScannerClient.kt`

**Change this:**
```kotlin
.baseUrl("http://10.0.2.2:5000/")  // Old localhost URL
```

**To this:**
```kotlin
.baseUrl("https://YOUR-REPLIT-URL-HERE.repl.co/")  // Your Replit URL
```

You can find your Replit URL in the address bar when viewing the dashboard or webview.

---

## 🚀 After Building the APK

1. **Transfer to Android device**
   - USB cable, email, or cloud storage
   
2. **Enable installation**
   - Settings → Security → Install unknown apps → Enable for your file manager
   
3. **Install the APK**
   - Tap the APK file
   - Follow installation prompts
   - Grant VPN permission when requested

4. **Use the app!**
   - Open CyberLens
   - Tap "Start Capture"
   - Accept VPN permission
   - Browse websites/use apps
   - View captured traffic on the dashboard (this Replit!)

---

## 📊 Live Dashboard

While your Android app captures traffic, you can view the results in real-time:

- **Dashboard:** `/` (main page)
- **Download Page:** `/download`
- **API Docs:** `/api/info`

The dashboard auto-refreshes every 10 seconds to show new captures.

---

## ❓ Why Can't Replit Build APKs?

Android APK building requires:
- ✗ Android SDK (5-10 GB download)
- ✗ Build tools and platform packages  
- ✗ Gradle dependencies (hundreds of MB)
- ✗ Java 17+ with significant RAM
- ✗ Android emulator tools

Replit environment limitations:
- Limited storage space
- Memory constraints
- Build timeout restrictions
- No Android SDK support

**Solution:** Build locally with Android Studio or use GitHub Actions for cloud builds!

---

## 🎓 Learning Resources

Included in your download:
- `HOW_TO_BUILD_APK.md` - Detailed build instructions with commands
- `README.md` - Project overview and features
- `README_REPLIT.md` - Quick start guide
- `replit.md` - Complete project documentation

---

## 💡 Need Help?

Common issues and solutions:

**Q: Android Studio sync fails**  
A: Wait 5-10 minutes for first sync. Check internet connection.

**Q: APK won't install on phone**  
A: Enable "Install unknown apps" in Settings → Security

**Q: App can't connect to backend**  
A: Double-check you updated the backend URL in `ScannerClient.kt`

**Q: VPN permission denied**  
A: App won't work without VPN permission - it's required to capture traffic

---

## 📞 Ready to Start?

**→ Visit `/download` in this Replit's webview**  
**→ Download the source code (17KB)**  
**→ Follow the build instructions**  
**→ Install on your Android device**  
**→ Start capturing network traffic!**

---

**Happy Building! 🎉**
