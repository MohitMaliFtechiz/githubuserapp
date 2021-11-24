package com.ftechiz.githubuserapp.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ftechiz.githubuserapp.dao.UserDao
import com.ftechiz.githubuserapp.data.UserModel
import com.ftechiz.githubuserapp.data.UserProfileModel
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UserDatabaseTest : TestCase() {

    private lateinit var db: UserDatabase
    private lateinit var dao: UserDao
    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java).build()
        dao = db.getDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun addUserProfile(): Unit = runBlocking {
        val user = UserProfileModel(
            0, "s", "s", "s", "s", "s", "s", "s", 2,
            "s", 2, "s", "s", "s", "s", "s", "s",
            "s", "s", "s", "s", 5, 10, "s", "s",
            false, "s", "s", "s", "s", "s", "s", "s"
        )
        dao.insertUserProfile(user)

        val u = dao.getUserProfile(user.login!!)
        assertThat(u).isEqualTo(user)
    }

    @Test
    fun updateUserProfile(): Unit = runBlocking {
        val user = UserProfileModel(
            0, "s", "s", "s", "s", "s", "s", "s", 2,
            "s", 2, "s", "s", "s", "s", "s", "s",
            "s", "s", "s", "s", 5, 10, "s", "s",
            false, "s", "s", "s", "s", "s", "s", "s"
        )
        dao.insertUserProfile(user)
        dao.updateUserProfile("user Note", "s")
        val u = dao.getUserProfile("s")
        assertThat(u.note).isEqualTo("user Note")
    }

    @Test
    fun addUserList(): Unit = runBlocking {
        val user1 = UserModel(
            0, "s", "s", "s", "s", "s", "s",
            "s", "s_user1", "s", "s", "s", "s",
            false, "s", "s", "s", "s", "s", "progress"
        )
        val user2 = UserModel(
            1, "s", "s", "s", "s", "s", "s",
            "s", "s_login", "s", "s", "s", "s",
            false, "s", "s", "s", "s", "s", "progress"
        )

        val list: ArrayList<UserModel> = arrayListOf()
        list.add(user1)
        list.add(user2)

        dao.addUserList(list as List<UserModel>)

        val u = dao.getAllUser()
        assertThat(u.contains(user1)).isTrue()
    }
}