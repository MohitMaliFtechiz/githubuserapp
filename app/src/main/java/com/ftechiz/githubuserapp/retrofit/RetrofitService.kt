package com.ftechiz.githubuserapp.retrofit

import com.ftechiz.githubuserapp.data.UserModel
import com.ftechiz.githubuserapp.data.UserProfileModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RetrofitService {
    @GET("/users")
    fun getUserList(@Query("since") page: Int): Call<List<UserModel>>
    @GET("/users/{username}")
    fun getUser(@Path("username")username : String): Call<UserProfileModel>
}