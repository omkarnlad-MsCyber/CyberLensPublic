"""
CyberLens Backend API Server
This Flask server receives network traffic data from the CyberLens Android app
and provides basic security analysis and logging.
"""

from flask import Flask, request, jsonify, render_template, send_file, send_from_directory
from flask_cors import CORS
from datetime import datetime
import json
import os

app = Flask(__name__)
CORS(app)  # Allow cross-origin requests from Android app

# In-memory storage for captured data (use a database in production)
captured_hosts = []
analysis_results = []

# Simple threat indicators (expand with real threat intelligence in production)
SUSPICIOUS_KEYWORDS = [
    'malware', 'phishing', 'suspicious', 'tracker', 'ads', 'analytics',
    'doubleclick', 'adservice', 'telemetry'
]

def analyze_host(target):
    """
    Perform basic security analysis on a captured host/domain.
    Returns a rating (1-10, where 10 is most suspicious) and issues list.
    """
    issues = []
    rating = 0
    
    target_lower = target.lower()
    
    # Check for suspicious keywords
    for keyword in SUSPICIOUS_KEYWORDS:
        if keyword in target_lower:
            issues.append(f"Contains suspicious keyword: '{keyword}'")
            rating += 3
    
    # Check for unusual TLDs
    suspicious_tlds = ['.xyz', '.top', '.click', '.loan', '.download']
    for tld in suspicious_tlds:
        if target_lower.endswith(tld):
            issues.append(f"Unusual TLD: {tld}")
            rating += 2
    
    # Check for IP addresses instead of domains
    if target.replace('.', '').isdigit():
        issues.append("Direct IP address connection")
        rating += 1
    
    # Check for known tracking domains
    if 'google-analytics' in target_lower or 'facebook.com' in target_lower:
        issues.append("Known tracking/analytics domain")
        rating += 1
    
    # Cap rating at 10
    rating = min(rating, 10)
    
    # If no issues, it's relatively safe
    if not issues:
        issues.append("No immediate threats detected")
        rating = 1
    
    return rating, issues

@app.route('/')
def home():
    """Dashboard page"""
    return render_template('dashboard.html')

@app.route('/download')
def download_page():
    """Download page for APK build instructions"""
    return send_file('../download.html')

@app.route('/downloads/<path:filename>')
def download_file(filename):
    """Serve downloadable files"""
    return send_from_directory('../downloads', filename, as_attachment=True)

@app.route('/api/info')
def api_info():
    """API information endpoint"""
    return jsonify({
        'service': 'CyberLens Backend API',
        'version': '1.0',
        'status': 'running',
        'endpoints': {
            'GET /': 'Web dashboard',
            'GET /api/info': 'API information',
            'POST /api/scan-batch': 'Submit hosts for security analysis',
            'GET /api/history': 'Retrieve scan history',
            'GET /api/stats': 'Get statistics'
        },
        'timestamp': datetime.now().isoformat()
    })

@app.route('/api/scan-batch', methods=['POST'])
def scan_batch():
    """
    Receives a batch of captured hosts from the Android app
    Expected format: {"targets": ["example.com", "192.168.1.1", ...]}
    Returns: {"findings": [{"target": "...", "rating": 5, "issues": [...]}]}
    """
    try:
        data = request.get_json()
        
        if not data or 'targets' not in data:
            return jsonify({'error': 'Invalid request. Expected {"targets": [...]}'}), 400
        
        targets = data['targets']
        
        if not isinstance(targets, list):
            return jsonify({'error': 'targets must be a list'}), 400
        
        findings = []
        
        for target in targets:
            # Store the capture
            captured_hosts.append({
                'target': target,
                'timestamp': datetime.now().isoformat()
            })
            
            # Analyze the host
            rating, issues = analyze_host(target)
            
            result = {
                'target': target,
                'rating': rating,
                'issues': issues
            }
            
            findings.append(result)
            analysis_results.append({
                **result,
                'timestamp': datetime.now().isoformat()
            })
            
            # Log to console
            print(f"[SCAN] {target} - Rating: {rating}/10 - Issues: {len(issues)}")
        
        return jsonify({'findings': findings}), 200
    
    except Exception as e:
        print(f"Error processing scan-batch: {str(e)}")
        return jsonify({'error': 'Internal server error'}), 500

@app.route('/api/history', methods=['GET'])
def get_history():
    """Retrieve scan history with optional limit"""
    limit = request.args.get('limit', 100, type=int)
    limit = min(limit, 1000)  # Max 1000 results
    
    return jsonify({
        'total': len(analysis_results),
        'results': analysis_results[-limit:][::-1]  # Most recent first
    }), 200

@app.route('/api/stats', methods=['GET'])
def get_stats():
    """Get statistics about captured data"""
    total_scans = len(analysis_results)
    high_risk = sum(1 for r in analysis_results if r.get('rating', 0) >= 7)
    medium_risk = sum(1 for r in analysis_results if 4 <= r.get('rating', 0) < 7)
    low_risk = sum(1 for r in analysis_results if r.get('rating', 0) < 4)
    
    return jsonify({
        'total_scans': total_scans,
        'total_hosts': len(captured_hosts),
        'risk_breakdown': {
            'high_risk': high_risk,
            'medium_risk': medium_risk,
            'low_risk': low_risk
        },
        'last_scan': analysis_results[-1]['timestamp'] if analysis_results else None
    }), 200

if __name__ == '__main__':
    print("=" * 60)
    print("CyberLens Backend API Server Starting...")
    print("=" * 60)
    print("This server receives traffic data from the CyberLens Android app")
    print("and provides basic security analysis.")
    print()
    print("Configure your Android app to connect to:")
    print("  - Emulator: http://10.0.2.2:5000/")
    print("  - Physical Device: http://<this-server-ip>:5000/")
    print("  - Replit Deploy: Use your Replit deployment URL")
    print("=" * 60)
    
    # Run on 0.0.0.0:5000 for Replit compatibility
    app.run(host='0.0.0.0', port=5000, debug=True)
