package com.ftechiz.githubuserapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ftechiz.githubuserapp.repository.UserRepository

class ProfileViewModelFactory(
    private val userRepository: UserRepository,
    private val username: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(userRepository,username)as T
    }
}