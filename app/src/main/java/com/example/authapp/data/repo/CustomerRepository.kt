package com.example.authapp.data.repo

import androidx.room.RoomDatabase
import com.example.authapp.data.Customer
import com.example.authapp.data.CustomerDb
import javax.inject.Inject

class CustomerRepository @Inject constructor(
    private val customerDb: CustomerDb
) {

    private val dao = customerDb.customerDao()


    suspend fun insetCustomer(customer: Customer) {
        dao.insertCustomer(customer)
    }

    fun getAllCustomers() = dao.getAllCustomers()

}