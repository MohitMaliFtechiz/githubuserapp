package com.ftechiz.githubuserapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ftechiz.githubuserapp.repository.UserRepository


class MainViewModelFactory(private val userRepository: UserRepository,private val page:Int,private val loadOfflineData:Boolean) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(userRepository,page,loadOfflineData) as T
    }
}