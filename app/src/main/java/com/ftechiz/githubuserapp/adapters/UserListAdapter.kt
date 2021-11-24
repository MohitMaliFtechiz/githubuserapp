package com.ftechiz.githubuserapp.adapters

import android.app.Activity
import android.content.Intent
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ftechiz.githubuserapp.ProfileActivity
import com.ftechiz.githubuserapp.R
import com.ftechiz.githubuserapp.data.UserModel
import com.ftechiz.githubuserapp.utils.NetworkUtils
import com.squareup.picasso.Picasso

class UserListAdapter(private val activity: Activity, list: ArrayList<UserModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_DATA = 0;
        const val VIEW_TYPE_PROGRESS = 1;
    }


    private var NEGATIVE = floatArrayOf(
        -1f, 0f, 0f, 0f, 255f,
        0f, -1f, 0f, 0f, 255f,
        0f, 0f, -1f, 0f, 255f,
        0f, 0f, 0f, 1f, 0f
    )
    private var userList: ArrayList<UserModel>? = list
    fun setList(list: ArrayList<UserModel>) {
        this.userList = list
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_DATA) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
            ViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.progressbar, parent, false)
            ProgressViewHolder(view)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (userList!![position].viewType == "progress") {
            VIEW_TYPE_PROGRESS
        } else {
            VIEW_TYPE_DATA
        }
    }


    override fun getItemCount(): Int {
        return if (userList == null) 0
        else
            userList?.size!!
    }

    inner class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val name = view.findViewById<TextView>(R.id.userItemUsername)
        val image = view.findViewById<ImageView>(R.id.ivUserImage)
        val noteImage = view.findViewById<ImageView>(R.id.createNote)
        private val detail = view.findViewById<TextView>(R.id.userItemDetail)

        fun bind(data: UserModel, activity: Activity) {
            noteImage.visibility = View.GONE
            name.text = data.login
            detail.text = data.url
            if (data.note != null && data.note != "null") {
                noteImage.visibility = View.VISIBLE
            } else {
                noteImage.visibility = View.GONE
            }
            Picasso.get().load(data.avatar_url).into(image)

            itemView.setOnClickListener {
                if ((NetworkUtils.isInternetAvailable(activity))
                    || (data.note != null && data.note != "null")
                ) {
                    val intent = Intent(activity, ProfileActivity::class.java)
                    intent.putExtra("user", data.login)
                    activity.startActivity(intent)
                } else {
                    Toast.makeText(
                        activity,
                        "Enable internet connection to view profile!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(userList!![position], activity)
            //To Invert Image Color of every 4th item
            val count: Int = position + 1
            if (count % 4 == 0) {
                holder.image.colorFilter = ColorMatrixColorFilter(NEGATIVE)
            }
        }
    }
}