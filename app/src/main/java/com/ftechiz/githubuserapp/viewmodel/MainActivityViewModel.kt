package com.ftechiz.githubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ftechiz.githubuserapp.data.UserModel
import com.ftechiz.githubuserapp.data.UserProfileModel
import com.ftechiz.githubuserapp.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val userRepository: UserRepository,
    private var page: Int,
    private val loadOfflineData: Boolean
) : ViewModel() {


    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadData(page, loadOfflineData)
        }
    }

    val users: LiveData<List<UserModel>>
        get() = userRepository.users

    fun loadData(page: Int, loadOfflineData: Boolean) {
        userRepository.getUserData(page, loadOfflineData)
    }

    fun searchDatabase(query: String):LiveData<List<UserModel>> {
        return userRepository.searchDatabase(query).asLiveData()
    }


}