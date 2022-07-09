package com.example.bottomnavigationexample.ui.health

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HealthViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is health Fragment"
    }

    val text: LiveData<String> = _text
}