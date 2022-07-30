package com.medprompt.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.medprompt.AppState
import com.medprompt.Screen
import com.medprompt.components.*
import com.medprompt.dto.Appointment
import com.medprompt.dto.HomeFeedItem
import com.medprompt.dto.ScreenType
import com.medprompt.ui.theme.MedpromptTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun AppointmentScreen(appState: AppState) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()

    val freqList = listOf("Week", "Month", "Year")

    var appName by remember { mutableStateOf("") }
    var freqAmount by remember { mutableStateOf(0) }
    var dateTime by remember { mutableStateOf(getFormattedDateTime()) }
    var selectedFreqType by remember { mutableStateOf(freqList[0]) }

    MedpromptTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            HeaderOptions(
                navController = appState.navController,
                contextLabel = "Add Appointment",
                addButtonOnClick = {

                    // check for user and check that application name is not empty so we don't add blanks
                    if (user != null && appName.isNotEmpty()) {
                        val addApp = firestore
                            .collection("appointments")
                            .document(user.uid)
                            .collection("appointments")
                            .add(
                                // add to main appointments node
                                Appointment(
                                datetime = dateTime,
                                freqAmount = freqAmount,
                                freqType = selectedFreqType,
                                appName = appName
                            ))

                        addApp.addOnSuccessListener(OnSuccessListener {
                            firestore
                                .collection("appointments")
                                .document(user.uid)
                                .collection("home-feed")
                                .document(it.id)
                                .set(
                                    // add to home-feed node with the same doc id as main node
                                    HomeFeedItem(
                                        documentId = it.id,
                                        screenType = ScreenType.APPOINTMENT,
                                        title = appName,
                                        datetime = dateTime
                                    )
                                )
                            appState.navController.navigate(Screen.Home.route)
                        })
                    }
                }
            )

            Text(text = "Appointment Name")
            Row(modifier = Modifier
                .padding(5.dp)
                .height(55.dp)) {
                
                InputField(weight = 1f, value = appName, onValueChange = { appName = it })
            }

            DateTimePicker(label = "Date and Time of Appointment", onSelectedValue = { dateTime = it.toString() })

            Text(text = "Frequency of the Appointment")
            Row(
                modifier = Modifier
                .padding(5.dp)
                .height(55.dp)
            ) {
                InputField(
                    weight = 3f,
                    placeholder = "Times every...",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = freqAmount.toString(),
                    onValueChange = {
                        if (it.isNullOrBlank() || it.isNullOrEmpty() || it.toInt() > 100) {
                            freqAmount = 1
                            Toast.makeText(context, "Must be between 0 and 100", Toast.LENGTH_LONG).show()
                        } else {
                            freqAmount = it.toInt()
                        }
                    }
                )
                DropDown(
                    weight = 3f,
                    items = freqList,
                    selectedValue = selectedFreqType,
                    onSelectedValue = { selectedFreqType = it }
                )
            }
        }
    }
}

fun getFormattedDateTime() : String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    return current.format(formatter)
}

@Preview(showBackground = true)
@Composable
fun AppointmentScreen_Preview() {
    MedpromptTheme {
        AppointmentScreen(appState = AppState(rememberScaffoldState(), rememberNavController(), rememberCoroutineScope()))
    }
}