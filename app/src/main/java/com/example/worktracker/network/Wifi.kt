package com.example.worktracker.network

import android.content.Context
import android.net.wifi.WifiManager

fun checkWifiEnabled(context: Context): Boolean {
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    if (wifiManager.isWifiEnabled) {
        return true
    }
    return false
}

fun checkSSID(context: Context): String {
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    return if (wifiManager.isWifiEnabled) {
        val wifiInfo = wifiManager.getConnectionInfo()
        val ssid = wifiInfo.ssid
        ssid.trim('"')
    } else {
        // Handle case where WiFi is not enabled
        "Wifi is disabled"
    }
}