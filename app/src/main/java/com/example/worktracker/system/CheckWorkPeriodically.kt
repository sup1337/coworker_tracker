package com.example.worktracker.system

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.*
import com.example.worktracker.network.WifiSSIDList
import com.example.worktracker.network.checkSSID
import com.example.worktracker.network.stopWork
import java.util.concurrent.TimeUnit

class CheckWorkPeriodically(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {
    private val appContext = context
    override fun doWork(): Result {
        // Check the WiFi SSID
        if(WifiSSIDList.contains(checkSSID(appContext))) {
            Log.d("PeriodicWork", "You are working!")
            return Result.success()
        }
        Log.d("PeriodicWork", "You are not working!")
        val userId = inputData.getString("userId") ?: ""
        stopWork(userId, appContext)
        return Result.failure()
    }
}

fun schedulePeriodicWork(context: Context, sharedPreferences: SharedPreferences) {
    Log.d("PeriodicWork", "schedulePeriodicWork() called")
    val workManager = WorkManager.getInstance(context)

    // Set up the constraints for the work
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val data = Data.Builder()
        .putString("userId", sharedPreferences.getString("userId", ""))
        .build()

    val workRequest = PeriodicWorkRequestBuilder<CheckWorkPeriodically>(30, TimeUnit.SECONDS)
        .setInputData(data)
        .setConstraints(constraints)
        .build()

    workManager.enqueue(workRequest)
}

fun cancelPeriodicWork(context: Context) {
    Log.d("PeriodicWork", "stopCheckWifiService() called")
    WorkManager.getInstance(context).cancelAllWork()
}