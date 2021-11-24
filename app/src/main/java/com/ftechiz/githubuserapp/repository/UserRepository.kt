package com.ftechiz.githubuserapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ftechiz.githubuserapp.data.UserModel
import com.ftechiz.githubuserapp.data.UserProfileModel
import com.ftechiz.githubuserapp.database.UserDatabase
import com.ftechiz.githubuserapp.retrofit.RetrofitService
import com.ftechiz.githubuserapp.utils.App
import com.ftechiz.githubuserapp.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserRepository(
    private val userDatabase: UserDatabase,
    private val retrofitService: RetrofitService,
    private val applicationContext: Context
) {

    private val usersLiveData = MutableLiveData<List<UserModel>>()
    val users: LiveData<List<UserModel>>
        get() = usersLiveData

    private val userProfileData = MutableLiveData<UserProfileModel>()
    val userProfile: LiveData<UserProfileModel>
        get() = userProfileData

    fun getUserData(page: Int, loadOfflineData: Boolean) {
        if (loadOfflineData) {
            CoroutineScope(Dispatchers.IO).launch {
                val user = userDatabase.getDao().getAllUser()
                Log.e("TAG", "onResponse: Old list")
                if (user.isNotEmpty()) {
                    usersLiveData.postValue(user)
                }
            }
        }
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result = retrofitService.getUserList(page)
            result.enqueue(object : Callback<List<UserModel>> {
                override fun onResponse(
                    call: Call<List<UserModel>>,
                    response: Response<List<UserModel>>
                ) {
                    if (response.body() != null) {
                        setPageNo()
                        CoroutineScope(Dispatchers.IO).launch {
                            userDatabase.getDao().addUserList(response.body()!!)
                            Log.e("TAG", "onResponse: Save list")
                            usersLiveData.postValue(response.body()!!)
                        }
                    } else {
                        Log.e("TAG", "Response Null: ........Error in fetching list..........")
                    }
                }

                override fun onFailure(call: Call<List<UserModel>>, t: Throwable) {
                    Log.e("TAG", "onFailure: ........Error in fetching list..........")
                }
            })

        } else {
            Log.e("TAG", "...............No Network: ..................")
            if (loadOfflineData) {
                val user = userDatabase.getDao().getAllUser()
                usersLiveData.postValue(user)
            }
        }
    }

    private fun setPageNo() {
        var page = App.getInteger(applicationContext, App.PAGE_NO)
        page++
        App.setInteger(applicationContext, App.PAGE_NO, page)
    }

    fun getUserProfileData(username: String) {
        //get user from local database
        val user = userDatabase.getDao().getUserProfile(username)
        if (user != null) {
            userProfileData.postValue(user)
        } else {
            if (NetworkUtils.isInternetAvailable(applicationContext)) {
                val result = retrofitService.getUser(username)

                result.enqueue(object : Callback<UserProfileModel> {

                    override fun onResponse(
                        call: Call<UserProfileModel>,
                        response: Response<UserProfileModel>
                    ) {
                        if (response.body() != null) {
                            userProfileData.postValue(response.body())
                            CoroutineScope(Dispatchers.IO).launch {
                                userDatabase.getDao().insertUserProfile(response.body()!!)
                            }
                        } else {
                            Log.e(
                                "TAG",
                                "Response Null: ........Error in Loading User Data........."
                            )
                        }
                    }

                    override fun onFailure(call: Call<UserProfileModel>, t: Throwable) {
                        Log.e("TAG", "onFailure: ........Error in Failed To fetch User..........")
                    }

                })


            }
        }
    }

    fun searchDatabase(query: String): Flow<List<UserModel>> {
        return userDatabase.getDao().searchDatabase(query)
    }
}