package com.example.authapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Entity
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @SerialName("Q1")
    val q1: Int? = null,
    @SerialName("Q2")
    val q2: Int,
    @SerialName("Q3")
    val q3: String? = null,
    val recording: String,
    val gps: String,
    @SerialName("submit_time")
    val submitTime: String,
    @Transient
    val json: String = ""
)
