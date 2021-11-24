package com.ftechiz.githubuserapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ftechiz.githubuserapp.utils.App
import com.ftechiz.githubuserapp.dao.UserDao
import com.ftechiz.githubuserapp.data.UserModel
import com.ftechiz.githubuserapp.data.UserProfileModel
import com.ftechiz.githubuserapp.utils.Converter

@Database(entities = [UserModel::class,UserProfileModel::class], version = 2)
@TypeConverters(Converter::class)
abstract class UserDatabase : RoomDatabase() {


    abstract fun getDao(): UserDao
    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null
        fun getDatabase(context: Context): UserDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        UserDatabase::class.java,
                        App.DATABASE_NAME
                    )
                      //  .addTypeConverter(Converter::class.java)
                        .build()
                }
            }
            return INSTANCE!!
        }
    }

}