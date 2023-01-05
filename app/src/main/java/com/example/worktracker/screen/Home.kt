package com.example.worktracker.screen

import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.worktracker.R
import com.example.worktracker.network.*

@Composable
fun HomePage(navController: NavHostController, sharedPreferences: SharedPreferences) {

    val userId = sharedPreferences.getString("userId", "").toString()

    val localContext = LocalContext.current

    var isWorking by remember { mutableStateOf(false)}

    checkWorking(userId, localContext, object: CallbackListener<Boolean> {
        override fun onSuccess(response: Boolean) {
            isWorking = response
        }})

    val image = if(isWorking) {
        painterResource(id = R.drawable.yes)
    } else {
        painterResource(id = R.drawable.no)
    }

    val text = if(isWorking) {
        stringResource(id = R.string.working)
    } else {
        stringResource(id = R.string.not_working)
    }

    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = image,
            contentDescription = "in work",
            Modifier.clickable {
                if (WifiSSIDList.contains(checkSSID(localContext))) {
                    isWorking = if (isWorking) {
                        stopWork(userId, localContext)
                        !isWorking
                    } else {
                        startWork(userId, localContext)
                        !isWorking
                    }
                } else {
                    Toast.makeText(localContext, "You are not on the correct WIFI!", Toast.LENGTH_SHORT).show()
                }
            }
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = AnnotatedString(text),
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                color = if(isWorking) {
                    colorResource(R.color.app_green)
                } else {
                    colorResource(R.color.app_red)
                }
            )
        )
    }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End
    ) {
        Button(
            onClick = {
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("userId", "")
                editor.apply()

                navController.navigate(Routes.Login.route)
            },
            modifier = Modifier
                .height(70.dp)
                .padding(top = 20.dp, end = 20.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary
            )
        ) {
            Text(text = "Log Out", color = MaterialTheme.colors.background)
        }
    }
}

