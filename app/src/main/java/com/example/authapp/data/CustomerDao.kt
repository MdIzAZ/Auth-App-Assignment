package com.example.authapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    @Insert
    suspend fun insertCustomer(customer: Customer)

    @Query("SELECT * FROM Customer")
    fun getAllCustomers(): Flow<List<Customer>>

}