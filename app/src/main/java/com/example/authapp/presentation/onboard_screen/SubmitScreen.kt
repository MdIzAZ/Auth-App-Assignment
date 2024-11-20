package com.example.authapp.presentation.onboard_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SubmitPage(
    modifier: Modifier = Modifier,
    isSubmitButtonEnabled: Boolean,
    onBackClick: () -> Unit,
    onSubmit: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            textAlign = TextAlign.Center,
            text = "Audio will be saved on Internal Storage->Downloads",
            modifier = Modifier.align(Alignment.TopCenter).padding(top= 30.dp),
            fontSize = 28.sp,
        )

        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = onSubmit,
            content = { Text(text = "Submit") },
            enabled = isSubmitButtonEnabled
        )



        ElevatedButton(
            modifier = Modifier.align(Alignment.BottomStart),
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

    }
}