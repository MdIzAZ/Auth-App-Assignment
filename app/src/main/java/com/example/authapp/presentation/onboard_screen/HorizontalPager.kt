package com.example.authapp.presentation.onboard_screen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.authapp.data.Customer
import kotlinx.coroutines.launch

@Composable
fun OnBoardHolder(
    modifier: Modifier = Modifier,
    startRecording: () -> Unit,
    onSubmitForm: (nav: () -> Unit) -> Unit,
    customerList: List<Customer>,
    age: String,
    gender: Gender?,
    profileImage: ImageBitmap?,
    onAgeChange: (String) -> Unit,
    onGenderChange: (Gender) -> Unit,
    onProfileChange: (ImageBitmap?, String?) -> Unit
) {

    val pagerState = rememberPagerState(pageCount = { 5 })
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        userScrollEnabled = false,
        pageContent = {
            when (it) {
                0 -> GenderScreen(
                    selectedGender = gender,
                    onGenderChange = { gen -> onGenderChange(gen) },
                    onNextClick = {
                        scope.launch { pagerState.animateScrollToPage(1) }
                    },
                    startRecording = startRecording
                )

                1 -> AgeScreen(
                    age = age,
                    onAgeChange = { age -> onAgeChange(age) },
                    onBackClick = {
                        scope.launch { pagerState.animateScrollToPage(0) }
                    },
                    onNextClick = {
                        if (age.isEmpty()) {
                            Toast.makeText(context, "Age is Necessary", Toast.LENGTH_SHORT).show()
                            return@AgeScreen
                        }
                        if (age.toIntOrNull() == null) {
                            Toast.makeText(context, "Only numbers Allowed", Toast.LENGTH_SHORT)
                                .show()
                            return@AgeScreen
                        }

                        scope.launch { pagerState.animateScrollToPage(2) }
                    }

                )

                2 -> ProfileScreen(
                    profileImage = profileImage,
                    onProfileChange = onProfileChange,
                    onBackClick = {
                        scope.launch { pagerState.animateScrollToPage(1) }
                    },
                    onNextClick = {
                        scope.launch { pagerState.animateScrollToPage(3) }
                    }
                )

                3 -> SubmitPage(
                    isSubmitButtonEnabled = age.isNotEmpty(),
                    onSubmit = {
                        onSubmitForm(
                            { scope.launch { pagerState.animateScrollToPage(4) } }
                        )

                    },
                    onBackClick = {
                        scope.launch { pagerState.animateScrollToPage(2) }
                    }
                )

                4 -> {
                    CustomerScreen(
                        customers = customerList,
                        onBackClick = {
                            scope.launch { pagerState.animateScrollToPage(3) }
                        },
                        onNextClick = {
                            scope.launch { pagerState.animateScrollToPage(0) }
                        }
                    )
                }
            }
        }
    )

}

enum class Gender(val value: Int) {
    MALE(1), FEMALE(2), OTHER(3)
}
