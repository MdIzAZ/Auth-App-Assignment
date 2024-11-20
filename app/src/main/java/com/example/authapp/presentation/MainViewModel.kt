package com.example.authapp.presentation

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authapp.data.Customer
import com.example.authapp.data.repo.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CustomerRepository
) : ViewModel() {


    private val _state = MutableStateFlow<List<Customer>>(emptyList())
    val state = _state.asStateFlow()

    init {
        getAllCustomers()
    }


    fun getAllCustomers() {
        viewModelScope.launch {
            try {
                repository.getAllCustomers().collect{ newList->
                    _state.value = newList
                }
            } catch (e: Exception) {
                Log.d("izaz", "Error: ${e.message}")
            }
        }
    }

    fun insertCustomer(customer: Customer) {
        viewModelScope.launch {
            try {
                repository.insetCustomer(customer)
                Log.d("izaz", "customer Added Successfully")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }




}