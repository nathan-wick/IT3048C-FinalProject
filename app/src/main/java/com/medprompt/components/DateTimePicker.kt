package com.medprompt.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medprompt.ui.theme.MedpromptTheme
import java.util.*

/**
 * This is custom Date and Time picker. Why create this?
 * Well, we're using this in 90% of our screens and I (Alex)
 * wanted to isolate all the date and time logic here because
 * it's going to be easier down the road to handle the edge cases
 * of dates.
 *
 * We need to think about edge cases for our dates.
 * For example, Feb is a month that sometimes has 29 days,
 * but most time it has 28 days.
 *
 * This is only only reason why I wanted to isolate this part of the app.
 */
@Composable
fun DateTimePicker (label: String) {
    Column {
        Text(text = label)
        Row(modifier = Modifier.padding(5.dp).height(60.dp)) {

            // These lists are static data for now... Firebase will come later
            val monthList = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
            val yearList = mutableListOf<String>()
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            var yearToAdd = currentYear
            val availableYears = 10
            while (yearToAdd < (currentYear.toInt() + availableYears)) {
                yearList.add(yearToAdd.toString())
                yearToAdd = yearToAdd + 1
            }

            InputField(weight = 3f, placeholder = "Day", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            DropDown(weight = 3f, items = monthList)
            DropDown(weight = 3f, items = yearList)
        }

        Row(modifier = Modifier.padding(5.dp).height(50.dp)) {
            val timeList = listOf("1:00", "1:30", "2:00", "2:30", "3:00", "3:30", "4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00", "7:30", "8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30")
            val pmAmList = listOf("PM", "AM")

            DropDown(weight = 1f, items = timeList)
            DropDown(weight = 1f, items = pmAmList)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MedicationScreen_Preview() {
    MedpromptTheme {
        DateTimePicker(label = "Label in context here...")
    }
}