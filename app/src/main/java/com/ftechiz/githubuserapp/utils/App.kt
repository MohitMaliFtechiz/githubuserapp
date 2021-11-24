package com.ftechiz.githubuserapp.utils

import android.app.Application
import android.content.Context
import com.ftechiz.githubuserapp.database.UserDatabase
import com.ftechiz.githubuserapp.repository.UserRepository
import com.ftechiz.githubuserapp.retrofit.RetrofitClient
import com.ftechiz.githubuserapp.retrofit.RetrofitService

class App : Application() {

    lateinit var userRepository: UserRepository

    companion object {
        const val BASE_URL: String = "https://api.github.com/"
        const val DATABASE_NAME: String = "user_database"
        const val PAGE_NO: String = "page_no"
        private val SHARED_PREF_NAME: String = "user_shared_pref"

        fun setString(context: Context,key: String, value: String) {
            // shared pref mode
            val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply {
                putString(key, value)
            }.apply()
        }
        fun setInteger(context: Context,key: String, value: Int) {
            val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply {
                putInt(key, value)
            }.apply()
        }


        fun getString(context: Context,key: String): String {
            val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(key, null).toString()
        }

        fun getInteger(context: Context,key: String): Int {
            val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt(key, 0)
        }
    }



    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val service = RetrofitClient.getRetrofitInstance().create(RetrofitService::class.java)
        val database = UserDatabase.getDatabase(applicationContext)
        userRepository = UserRepository(database, service, applicationContext)
    }




}