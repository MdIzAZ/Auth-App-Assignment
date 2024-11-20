package com.example.authapp.audiorecoder


import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import java.io.ByteArrayOutputStream

class VoiceRecorder {

    private var recorder: AudioRecord? = null
    private lateinit var buffer: ByteArray
    private val record: ByteArrayOutputStream = ByteArrayOutputStream()
    var start = false

    @SuppressLint("MissingPermission")
    fun prepare(sampleRate: Int, frameSize: Int) : VoiceRecorder {
        val minBufferSize =
            AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)

        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            minBufferSize
        )

        buffer = ByteArray(frameSize)
        recorder?.let { it.startRecording() }
        return this
    }

    fun start() {
        if (recorder == null) return
        if (!start) {
            start = true
            Thread {
                while (start) {
                    recorder?.read(buffer, 0, buffer.size)
                    record.write(buffer)
                }
            }.start()
        }
    }


    fun stop() : ByteArray {
        start = false
        recorder?.let { it.stop() }
        val result = record.toByteArray()
        record.reset()
        return result
    }

}