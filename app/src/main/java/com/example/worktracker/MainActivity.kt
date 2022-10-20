package com.example.worktracker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.worktracker.screen.*
import com.example.worktracker.ui.theme.WorkTrackerTheme

class MainActivity : ComponentActivity() {

    var loggedIn = false

    lateinit var sharedPreferences: SharedPreferences
    var PREFS_KEY = "prefs"
    var USER_KEY = "userId"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
            val userId = remember {
                mutableStateOf("")
            }

            userId.value = sharedPreferences.getString(USER_KEY, "").toString()

            WorkTrackerTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ScreenMain(sharedPreferences, loggedIn)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

        val userId = sharedPreferences.getString(USER_KEY, "").toString()

        if (userId != "") {
            loggedIn = true
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