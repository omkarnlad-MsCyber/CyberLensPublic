
CyberLens Android App (Kotlin) - Complete Runnable Project
=========================================================

This Android Studio project provides a runnable Kotlin app that uses a local VPN service (VpnService)
to capture destination IPs and DNS names and can forward them to the CyberLens backend for analysis.

Steps to run:
1. Open this folder in Android Studio: File -> Open -> select the folder containing settings.gradle.
2. Let Android Studio sync Gradle. It will download required SDKs etc.
3. In app/src/main/java/com/example/cyberlens/ScannerClient.kt set the baseUrl to your backend (ngrok or LAN IP).
   - For emulator use: http://10.0.2.2:5000/
   - For physical device: http://<your-pc-ip>:5000/
4. Connect device (enable USB debugging) or start an emulator.
5. Run the app from Android Studio (Run -> Run 'app').
6. In the app tap Start Capture and accept the VPN permission dialog.
7. Visit websites/apps on the phone to generate traffic. Captured hosts will appear in the list.

Notes & Warnings:
- Only test on devices you own or have explicit permission.
- Encrypted DNS (DoH/DoT) and apps using their own DNS may prevent domain extraction.
- The VPN-based capture is basic; for production, enhance parsing, error handling, performance, and privacy safeguards.
