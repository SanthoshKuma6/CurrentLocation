package com.example.currentlocation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StudentFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StudentViewModel(repository) as T
    }


}