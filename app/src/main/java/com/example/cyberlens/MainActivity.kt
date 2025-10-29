
package com.example.cyberlens

import android.app.Activity
import android.content.*
import android.net.VpnService
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private var vpnStarted = false
    private lateinit var startBtn: Button
    private lateinit var stopBtn: Button
    private lateinit var listView: ListView
    private val captured = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>
    private var serviceIntent: Intent? = null
    private val uiScope = CoroutineScope(Dispatchers.Main + Job())

    private val captureReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val host = intent?.getStringExtra("host") ?: return
            captured.add(0, host)
            adapter.notifyDataSetChanged()
            Toast.makeText(this@MainActivity, "Captured: $host", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startBtn = findViewById(R.id.btnStart)
        stopBtn = findViewById(R.id.btnStop)
        listView = findViewById(R.id.listHosts)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, captured)
        listView.adapter = adapter

        serviceIntent = Intent(this, CaptureVpnService::class.java)
        registerReceiver(captureReceiver, IntentFilter("com.cyberlens.CAPTURE"))

        startBtn.setOnClickListener {
            if (!vpnStarted) {
                val intent = VpnService.prepare(this)
                if (intent != null) {
                    startActivityForResult(intent, 0)
                } else {
                    startService(serviceIntent)
                    vpnStarted = true
                    startBtn.isEnabled = false
                    stopBtn.isEnabled = true
                    startPeriodicUpload()
                }
            }
        }

        stopBtn.setOnClickListener {
            if (vpnStarted) {
                stopService(serviceIntent)
                vpnStarted = false
                startBtn.isEnabled = true
                stopBtn.isEnabled = false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            startService(serviceIntent)
            vpnStarted = true
            startBtn.isEnabled = false
            stopBtn.isEnabled = true
            startPeriodicUpload()
        } else {
            Toast.makeText(this, "VPN permission denied", Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun startPeriodicUpload() {
        uiScope.launch {
            while (vpnStarted) {
                delay(30_000)
                // collect hosts from service via broadcast or shared storage; here we rely on broadcasts to update list
                // Optionally send collected hosts in batches to backend via ScannerClient
                // Example: ScannerClient.api.scanBatch(ScanBatchRequest(captured)).enqueue(...)
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(captureReceiver)
        uiScope.cancel()
        super.onDestroy()
    }
}
