
package com.example.cyberlens

import android.app.Service
import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import kotlinx.coroutines.*
import java.io.FileInputStream
import java.net.InetAddress
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentLinkedQueue

class CaptureVpnService : VpnService() {
    private val TAG = "CaptureVpnService"
    private var vpnInterface: ParcelFileDescriptor? = null
    private var job: Job? = null
    val capturedHosts = ConcurrentLinkedQueue<String>()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startVpn()
        return Service.START_STICKY
    }

    override fun onDestroy() {
        stopVpn()
        super.onDestroy()
    }

    private fun startVpn() {
        if (vpnInterface != null) return
        val builder = Builder()
            .setSession("CyberLensCapture")
            .setMtu(1500)
            .addAddress("10.0.0.2", 32)
            .addRoute("0.0.0.0", 0)
            .addDnsServer("8.8.8.8")
        vpnInterface = builder.establish()

        job = CoroutineScope(Dispatchers.IO).launch {
            vpnInterface?.fileDescriptor?.let { fd -> readPackets(fd) }
        }
    }

    private suspend fun readPackets(fd: java.io.FileDescriptor) {
        val input = FileInputStream(fd)
        val packet = ByteArray(32767)
        val buffer = ByteBuffer.wrap(packet)
        while (isActive) {
            try {
                val length = input.read(packet)
                if (length > 0) {
                    buffer.limit(length)
                    val host = parsePacketForHost(packet, length)
                    if (host != null) {
                        capturedHosts.add(host)
                        val b = Intent("com.cyberlens.CAPTURE")
                        b.putExtra("host", host)
                        sendBroadcast(b)
                    }
                    buffer.clear()
                } else {
                    delay(10)
                }
            } catch (e: Exception) {
                Log.e(TAG, "read error", e)
                break
            }
        }
    }

    private fun parsePacketForHost(data: ByteArray, length: Int): String? {
        if (length < 20) return null
        val version = (data[0].toInt() shr 4) and 0x0F
        if (version == 4) {
            val ihl = (data[0].toInt() and 0x0F) * 4
            val protocol = data[9].toInt() and 0xFF
            val dstIp = InetAddress.getByAddress(byteArrayOf(data[16],data[17],data[18],data[19])).hostAddress
            if (protocol == 17 && length >= ihl + 8) {
                val srcPort = ((data[ihl].toInt() and 0xFF) shl 8) or (data[ihl+1].toInt() and 0xFF)
                val dstPort = ((data[ihl+2].toInt() and 0xFF) shl 8) or (data[ihl+3].toInt() and 0xFF)
                if (srcPort == 53 || dstPort == 53) {
                    val dnsName = parseDnsName(data, ihl + 8, length - (ihl + 8))
                    if (dnsName != null) return dnsName
                }
                return dstIp
            } else {
                return dstIp
            }
        }
        return null
    }

    private fun parseDnsName(packet: ByteArray, offset: Int, payloadLen: Int): String? {
        if (payloadLen < 12) return null
        val qdcount = ((packet[offset+4].toInt() and 0xFF) shl 8) or (packet[offset+5].toInt() and 0xFF)
        if (qdcount < 1) return null
        var idx = offset + 12
        val nameParts = mutableListOf<String>()
        var passed = 0
        while (idx < offset + payloadLen) {
            val len = packet[idx].toInt() and 0xFF
            if (len == 0) {
                return nameParts.joinToString(".")
            }
            if ((len and 0xC0) == 0xC0) return null
            if (idx + 1 + len > packet.size) return null
            val part = String(packet, idx+1, len)
            nameParts.add(part)
            idx += 1 + len
            passed += 1
            if (passed > 20) break
        }
        return null
    }

    private fun stopVpn() {
        job?.cancel()
        try { vpnInterface?.close() } catch (e: Exception) {}
        vpnInterface = null
    }

    override fun onRevoke() {
        stopVpn()
        super.onRevoke()
    }
}
