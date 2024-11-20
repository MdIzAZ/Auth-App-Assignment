package com.example.authapp.presentation.onboard_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderScreen(
    modifier: Modifier = Modifier,
    selectedGender: Gender?,
    onGenderChange: (Gender) -> Unit,
    onNextClick: () -> Unit,
    startRecording: () -> Unit
) {

    val options = listOf(Gender.MALE, Gender.FEMALE, Gender.OTHER)

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        var expanded by remember { mutableStateOf(false) }

        Text(
            textAlign = TextAlign.Center,
            text = "Select Gender to Start Record Audio in .wav format",
            modifier = Modifier.align(Alignment.TopCenter).padding(top= 50.dp),
            fontSize = 28.sp,
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.align(Alignment.Center)
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedGender?.name ?: "",
                onValueChange = { },
                label = { Text(text = "Gender") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = OutlinedTextFieldDefaults.colors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.name) },
                        onClick = {
                            onGenderChange(it)
                            expanded = false
                            startRecording()
                        }
                    )
                }
            }
        }


        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

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