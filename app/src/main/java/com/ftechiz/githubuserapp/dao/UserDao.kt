package com.ftechiz.githubuserapp.dao

import androidx.room.*
import com.ftechiz.githubuserapp.data.UserModel
import com.ftechiz.githubuserapp.data.UserProfileModel
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(user: UserProfileModel)

    @Query("UPDATE users_profiles SET note = :note WHERE login = :username")
    suspend fun updateUserProfile(note: String, username: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUserList(users: List<UserModel>)

    @Query("UPDATE users SET note = :note WHERE login = :username")
    suspend fun updateUser(note: String, username: String)

    @Delete
    suspend fun deleteUser(user: UserModel)

    @Query("SELECT * FROM users_profiles WHERE login = :username ")
    fun getUserProfile(username: String): UserProfileModel

    @Query("SELECT * FROM users")
    fun getAllUser(): List<UserModel>

    @Query("SELECT * FROM users WHERE login LIKE :query OR note LIKE :query")
    fun searchDatabase(query:String):Flow<List<UserModel>>

}