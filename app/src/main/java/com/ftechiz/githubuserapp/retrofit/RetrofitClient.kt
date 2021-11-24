package com.ftechiz.githubuserapp.retrofit

import com.ftechiz.githubuserapp.utils.App
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object{
        fun getRetrofitInstance(): Retrofit{
            return Retrofit.Builder()
                .baseUrl(App.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}