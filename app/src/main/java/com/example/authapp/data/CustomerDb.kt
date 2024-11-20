package com.example.authapp.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [Customer::class],
    version = 3
)
abstract class CustomerDb : RoomDatabase(){

    abstract fun customerDao(): CustomerDao

}