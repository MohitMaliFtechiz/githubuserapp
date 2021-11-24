package com.ftechiz.githubuserapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.ftechiz.githubuserapp.database.UserDatabase
import com.ftechiz.githubuserapp.utils.App
import com.ftechiz.githubuserapp.viewmodel.ProfileViewModel
import com.ftechiz.githubuserapp.viewmodel.ProfileViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private lateinit var tvFollowers: TextView
    private lateinit var tvName: TextView
    private lateinit var tvBlog: TextView
    private lateinit var tvCompany: TextView
    private lateinit var tvEmail: TextView
    private lateinit var profileImage: ImageView
    private lateinit var tvFollowing: TextView
    private lateinit var bSave: AppCompatButton
    private lateinit var tvTwitter: TextView
    private lateinit var edNotes: TextInputEditText
    private lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val s: String = intent.getStringExtra("user").toString()
        supportActionBar?.title = s
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        context = this
        initViews()
        initViewModel(s)

        bSave.setOnClickListener {
            val note = edNotes.text.toString()
            if (!TextUtils.isEmpty(note)) {
                val database = UserDatabase.getDatabase(context)
                GlobalScope.launch {
                    database.getDao().updateUser(note, s)
                    database.getDao().updateUserProfile(note, s)
                }
                Toast.makeText(context, "Note added successfully!", Toast.LENGTH_SHORT).show()
            } else {
                edNotes.error = "Enter a valid value!"
            }
        }
    }

    private fun initViews() {
        tvFollowers = findViewById(R.id.tvFollowers)
        tvFollowing = findViewById(R.id.tvFollowing)
        profileImage = findViewById(R.id.imageView)
        tvEmail = findViewById(R.id.tvEmail)
        tvCompany = findViewById(R.id.tvCompany)
        tvBlog = findViewById(R.id.tvBlog)
        tvName = findViewById(R.id.tvName)
        edNotes = findViewById(R.id.edNotes)
        bSave = findViewById(R.id.bSave)
        tvTwitter = findViewById(R.id.tvTwitterUsername)
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel(username: String) {
        val repo = (application as App).userRepository
        val viewModel = ViewModelProvider(
            this,
            ProfileViewModelFactory(repo, username)
        )[ProfileViewModel::class.java]

        viewModel.user.observe(this, {
            Picasso.get().load(it.avatar_url).into(profileImage)
            tvFollowing.text = "Following : " + it.following.toString()
            tvFollowers.text = "Followers : " + it.followers.toString()
            tvName.text = "Name : " + it.name.toString()
            tvCompany.text = "Company : " + it.company.toString()
            tvEmail.text = "Email : " + it.email.toString()
            tvBlog.text = "Blog : " + it.blog.toString()
            tvTwitter.text = "Twitter Username : " + it.twitter_username.toString()
            val note = it.note.toString()
            Log.e("TAG", "initViewModel: $note")
            if (note != "null") {
                edNotes.setText(note)
            }
        })
    }
}