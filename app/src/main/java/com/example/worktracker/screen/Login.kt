package com.example.worktracker.screen

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.worktracker.network.Database
import com.vishnusivadas.advanced_httpurlconnection.PutData

@Composable
fun LoginPage(navController: NavHostController, sharedPreferences: SharedPreferences) {
    val focusManager = LocalFocusManager.current
    val localContext = LocalContext.current

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val userId = remember { mutableStateOf(TextFieldValue()) }

        Text(
            text = "Login",
            style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive),
            color = MaterialTheme.colors.primary
        )

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "User ID") },
            value = userId.value,
            onValueChange = { userId.value = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
                unfocusedLabelColor = MaterialTheme.colors.primary,
                focusedLabelColor = MaterialTheme.colors.primary,
                textColor = MaterialTheme.colors.primary,
                unfocusedIndicatorColor = MaterialTheme.colors.primary
            )
        )

        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = { loggingIn(
                    navController,
                    userId.value.text,
                    localContext,
                    sharedPreferences
                ) },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary
                )
            ) {
                Text(text = "Login", color = MaterialTheme.colors.background)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("Sign Up"),
            onClick = { navController.navigate(Routes.SignUp.route) },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                color = MaterialTheme.colors.primary
            )
        )
    }
}

private fun loggingIn(
    navController: NavHostController,
    userId: String,
    localContext: Context,
    sharedPreferences: SharedPreferences
) {

    if (userId != "") {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val field = arrayOfNulls<String>(1)
            field[0] = "user_id"
            val data = arrayOfNulls<String>(1)
            data[0] = userId
            val putData = PutData(
                Database().host + Database().databaseName + "login.php",
                "POST",
                field,
                data
            )
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    val result = putData.result
                    if(result == "Login Success") {
                        saveData(userId, sharedPreferences)
                        navController.navigate(Routes.Home.route)
                    }
                    Toast.makeText(localContext, result, Toast.LENGTH_SHORT).show()
                }
            }
        }
    } else {
        Toast.makeText(localContext, "All fields are mandatory!", Toast.LENGTH_SHORT).show()
    }
}

fun saveData(userId: String, sharedPreferences: SharedPreferences) {
    val editor: SharedPreferences.Editor = sharedPreferences.edit()
    editor.putString("userId", userId)
    editor.apply()
}