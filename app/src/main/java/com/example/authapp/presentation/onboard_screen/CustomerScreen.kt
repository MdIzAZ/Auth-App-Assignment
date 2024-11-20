package com.example.authapp.presentation.onboard_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.authapp.data.Customer
import kotlinx.serialization.json.JsonNull.content

@Composable
fun CustomerScreen(
    modifier: Modifier = Modifier,
    customers: List<Customer>,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth()
                .height(800.dp)
                .padding(4.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                ) {
                    TableCell(text = "Q1", isHeader = true, 3f)
                    TableCell(text = "Q2", isHeader = true, 3f)
                    TableCell(text = "Q3", isHeader = true, 5f)
                    TableCell(text = "Recording", isHeader = true, 5f)
                    TableCell(text = "GPS", isHeader = true, 5f)
                    TableCell(text = "Submit Time", isHeader = true, 7f)
                }
            }

            items(customers) {
                Row(
                    modifier = Modifier
                ) {
                    TableCell(text = it.q1.toString(), weight = 3f)
                    TableCell(text = it.q2.toString(), weight = 3f)
                    TableCell(text = it.q3.toString(), weight = 5f)
                    TableCell(text = it.recording, weight = 5f)
                    TableCell(text = it.gps, weight = 5f)
                    TableCell(text = it.submitTime, weight = 7f)
                }
            }
        }




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


        ElevatedCard(
            modifier = Modifier.align(Alignment.TopCenter),
            shape = RoundedCornerShape(5.dp),
            content = {
                Text("View in LandScape to get proper view", modifier= Modifier.padding(5.dp))
            }
        )


    }

}


@Composable
fun RowScope.TableCell(text: String, isHeader: Boolean = false, weight: Float) {
    Text(
        text = text,
        modifier = Modifier
            .height(80.dp)
            .weight(weight)
            .border(0.5.dp, Color.Black)
            .background(if (isHeader) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
            .padding(vertical = 2.dp, horizontal = 2.dp),
        fontSize = 12.sp,
        lineHeight = 13.sp,
        textAlign = TextAlign.Center,
        color = if (isHeader) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
    )
}