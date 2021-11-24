package com.ftechiz.githubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftechiz.githubuserapp.data.UserProfileModel
import com.ftechiz.githubuserapp.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val username: String
) :
    ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUserProfileData(username)
        }
    }

    val user: LiveData<UserProfileModel>
        get() = userRepository.userProfile

}