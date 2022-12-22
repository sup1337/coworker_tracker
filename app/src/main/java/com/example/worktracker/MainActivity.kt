package com.example.worktracker

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.worktracker.screen.*
import com.example.worktracker.ui.theme.WorkTrackerTheme

class MainActivity : ComponentActivity() {

    private val REQUEST_ACCESS = 1

    var loggedIn = false

    lateinit var sharedPreferences: SharedPreferences
    var PREFS_KEY = "prefs"
    var USER_KEY = "userId"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_ACCESS
            )

            sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
            val userId = remember {
                mutableStateOf("")
            }

            userId.value = sharedPreferences.getString(USER_KEY, "").toString()


        }
    }

    override fun onStart() {
        super.onStart()
        sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

        val userId = sharedPreferences.getString(USER_KEY, "").toString()

        if (userId != "") {
            loggedIn = true
        }


        //Check if location is enabled. Required for SSID requesting.
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isLocationEnabled) {
            // Location is not enabled
            val alertDialog: AlertDialog? = this.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setMessage("Your location is currently disabled. Please enable it in settings to use this app.")
                    setPositiveButton("Go to Settings") { _, _ ->
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                    }
                    setNegativeButton("Cancel") { _, _ ->
                        finish()
                        Toast.makeText(this@MainActivity, "Location services are required to use this app.", Toast.LENGTH_SHORT).show()
                    }
                }
                builder.create()
            }
            alertDialog?.show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        setContent {
            if (requestCode == REQUEST_ACCESS) {
                // Check if all permissions were granted
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    WorkTrackerTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.background
                        ) {
                            ScreenMain(sharedPreferences, loggedIn)
                        }
                    }
                } else {
                    this.finish()
                    Toast.makeText(LocalContext.current, "You can't use the app without those permissions.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
fun ScreenMain(sharedPreferences: SharedPreferences, loggedIn: Boolean){
    val navController = rememberNavController()

    val startDestination = if(loggedIn) {
        Routes.Home.route
    } else {
        Routes.Login.route
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.Login.route) {
            LoginPage(navController = navController, sharedPreferences)
        }

        composable(Routes.SignUp.route) {
            SignUpPage(navController = navController)
        }

        composable(Routes.Home.route) {
            HomePage(navController = navController, sharedPreferences)
        }
    }
}