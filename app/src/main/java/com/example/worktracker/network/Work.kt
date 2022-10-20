package com.example.worktracker.network

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.vishnusivadas.advanced_httpurlconnection.PutData
import java.text.SimpleDateFormat
import java.util.*


fun startWork(
    userId: String,
    localContext: Context
) {
    if (userId != "") {

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val currentDateAndTime = sdf.format(Date())

        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val field = arrayOfNulls<String>(2)
            field[0] = "user_id"
            field[1] = "time_of_arrival"
            val data = arrayOfNulls<String>(2)
            data[0] = userId
            data[1] = currentDateAndTime
            val putData = PutData(
                Database().host + Database().databaseName + "logArrival.php",
                "POST",
                field,
                data
            )
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    val result = putData.result
                    Toast.makeText(localContext, result, Toast.LENGTH_SHORT).show()
                }
            }
        }
    } else {
        Toast.makeText(
            localContext,
            "How did you even manage to get this error?",
            Toast.LENGTH_LONG
        ).show()
    }
}

fun stopWork(
    userId: String,
    localContext: Context
) {
    if (userId != "") {

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val currentDateAndTime = sdf.format(Date())

        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val field = arrayOfNulls<String>(2)
            field[0] = "user_id"
            field[1] = "time_of_leave"
            val data = arrayOfNulls<String>(2)
            data[0] = userId
            data[1] = currentDateAndTime
            val putData = PutData(
                Database().host + Database().databaseName + "logLeave.php",
                "POST",
                field,
                data
            )
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    val result = putData.result
                    Toast.makeText(localContext, result, Toast.LENGTH_SHORT).show()
                }
            }
        }
    } else {
        Toast.makeText(
            localContext,
            "How did you even manage to get this error?",
            Toast.LENGTH_LONG
        ).show()
    }
}

fun checkWorking(
    userId: String,
    localContext: Context,
    callbackListener: CallbackListener<Boolean>
) {
    if (userId != "") {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val field = arrayOfNulls<String>(1)
            field[0] = "user_id"
            val data = arrayOfNulls<String>(1)
            data[0] = userId
            val putData = PutData(
                Database().host + Database().databaseName + "checkWorking.php",
                "POST",
                field,
                data
            )
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    val result = putData.result
                    if (result == "You are working") {
                        callbackListener.onSuccess(true)
                    } else {
                        callbackListener.onSuccess(false)
                    }
                }
            }
        }
    } else {
        Toast.makeText(
            localContext,
            "How did you even manage to get this error?",
            Toast.LENGTH_LONG
        ).show()
    }
}

interface CallbackListener<T> {
    fun onSuccess(response: T)
}