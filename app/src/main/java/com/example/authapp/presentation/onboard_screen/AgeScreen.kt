package com.example.authapp.presentation.onboard_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AgeScreen(
    modifier: Modifier = Modifier,
    age: String,
    onAgeChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        OutlinedTextField(
            modifier = Modifier.align(Alignment.Center),
            value = age,
            onValueChange = {
                onAgeChange(it)
            },
            label = { Text(text = "Age") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            maxLines = 1,
            isError = age.isNotBlank() && age.toIntOrNull() == null,
            supportingText = {
                if (age.isNotBlank() && age.toIntOrNull() == null) {
                    Text(text = "Only numbers are allowed")
                }
            }
        )


        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ElevatedButton(
                shape = RoundedCornerShape(5.dp),
                onClick = onBackClick,
                content = {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back Icon"
                    )
                }
            )
            ElevatedButton(
                shape = RoundedCornerShape(5.dp),
                onClick = onNextClick,
                content = {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Next Icon"
                    )
                }
            )
        }
    }
}