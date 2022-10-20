package com.example.worktracker.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.widget.Toast

fun checkWifiChannels(
    localContext: Context
) {
    val wifiManager = localContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if(success) {
                scanSuccess(wifiManager, localContext)
            } else {
                scanFailure(wifiManager, localContext)
            }
        }
    }

    val intentFilter = IntentFilter()
    intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
    localContext.registerReceiver(wifiScanReceiver, intentFilter)

    val success = wifiManager.startScan()
    if(!success) {
        scanFailure(wifiManager, localContext)
    }
}

private fun scanSuccess(
    wifiManager: WifiManager,
    localContext: Context
) {
    val results = wifiManager.scanResults
    Toast.makeText(localContext, "Scan successful", Toast.LENGTH_SHORT).show()
    println(results.toString())
}

private fun scanFailure(
    wifiManager: WifiManager,
    localContext: Context
) {
    val results = wifiManager.scanResults
    println("Scan not successful, old results:")
    Toast.makeText(localContext, "Scan not successful", Toast.LENGTH_SHORT).show()
    println(results.toString())
}