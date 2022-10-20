package com.example.worktracker.screen

sealed class Routes(val route: String) {
    object SignUp : Routes("SignUp")
    object Login : Routes("Login")
    object Home : Routes("Home")
}