package com.example.worktracker.network

data class Database(
    val host: String = "http://192.168.0.10//",
    //coworkewrtracker.mysql.database.azure.com
    //myadmin@coworkewrtracker
    //port 3306
    val databaseName: String = "loginregister/"
)

val WifiSSIDList = listOf(
    "AndroidWifi",
    "Bendzsike-Network"
)