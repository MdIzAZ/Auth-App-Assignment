package com.example.authapp.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.authapp.audiorecoder.AudioRecorder
import com.example.authapp.audiorecoder.VoiceRecorder
import com.example.authapp.data.Customer
import com.example.authapp.presentation.onboard_screen.Gender
import com.example.authapp.presentation.onboard_screen.OnBoardHolder
import com.example.authapp.presentation.theme.AuthAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val voiceRecorder = VoiceRecorder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            )

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = {

                }
            )

            LaunchedEffect(Unit) {
                permissionLauncher.launch(permissions)
            }


            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            val viewModel = hiltViewModel<MainViewModel>()
            val state by viewModel.state.collectAsState()
            var capturedImage by remember { mutableStateOf<ImageBitmap?>(null) }
            var imageFileName by rememberSaveable { mutableStateOf<String?>(null) }
            var age by rememberSaveable { mutableStateOf("") }
            var gender by rememberSaveable { mutableStateOf<Gender?>(null) }
            var audioFileName by rememberSaveable { mutableStateOf<String>("") }
            var shouldShowLoader by rememberSaveable { mutableStateOf(false) }

            AuthAppTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        OnBoardHolder(
                            modifier = Modifier.fillMaxSize(),
                            age = age,
                            gender = gender,
                            profileImage = capturedImage,
                            onAgeChange = { age = it },
                            onGenderChange = { gender = it },
                            onProfileChange = { bit, name ->
                                capturedImage = bit
                                imageFileName = name
                            },
                            customerList = state,
                            startRecording = {
                                voiceRecorder.prepare(44100, 320).start()
                            },
                            onSubmitForm = { nav ->
                                if (!isLocationEnabled()) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Please Enable Location",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    return@OnBoardHolder
                                }
                                shouldShowLoader = true
                                audioFileName = if (voiceRecorder.start) saveRecord() else ""
                                val dateAndTime = getDateAndTime()
                                fetchLocation(
                                    fusedLocationProviderClient,
                                    onReceive = { location ->

                                        val temp = Customer(
                                            q1 = gender?.value,
                                            q2 = age.toInt(),
                                            q3 = imageFileName,
                                            recording = audioFileName,
                                            gps = location,
                                            submitTime = dateAndTime
                                        )
                                        val json = customerToJson(temp)
                                        Log.d("izaz", json)

                                        val customer = temp.copy(json = json)

                                        viewModel.insertCustomer(customer)

                                        age = ""
                                        gender = null
                                        capturedImage = null
                                        imageFileName = null
                                        audioFileName = ""
                                        shouldShowLoader = false
                                        nav()
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Json Saved to DB",
                                            Toast.LENGTH_LONG
                                        ).show()

                                    }
                                )

                            },
                        )

                        if (shouldShowLoader) CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(innerPadding)
                                .size(80.dp)
                        )
                    }


                }
            }
        }
    }

    private fun getDateAndTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentDate = Date()
        val formattedDate = dateFormat.format(currentDate)
        val formattedTime = timeFormat.format(currentDate)
        return "$formattedDate $formattedTime"
    }

    private fun fetchLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        onReceive: (String) -> Unit
    ) {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                location?.let {
                    val lat = it.latitude
                    val lon = it.longitude
                    onReceive("$lat, $lon")
                }
            }
    }


    private fun saveRecord(): String {
        val wavFile = AudioRecorder()
            .setAudioFormat(AudioRecorder.PCM_AUDIO_FORMAT)
            .setSampleRate(44100)
            .setBitsPerSample(AudioRecorder.BITS_PER_SAMPLE_16)
            .setNumChannels(AudioRecorder.CHANNELS_MONO)
            .setSubChunk1Size(AudioRecorder.SUBCHUNK_1_SIZE_PCM)
            .build(voiceRecorder.stop())

        val rootFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val pre = getDateAndTime().replace(" ", "_").replace(":", "-")
        val file = File(rootFolder, "Audio-$pre.wav")
        if (!file.exists()) file.createNewFile()

        val fos = FileOutputStream(file)
        fos.write(wavFile)
        fos.flush()
        fos.close()

        return file.name
    }


    private fun customerToJson(customer: Customer): String {
        return Json.encodeToString(customer)
    }


    fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}

