package com.kreimben.android_sns_app

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class FriendListAdapter(val itemList: ArrayList<UserListLayout>): RecyclerView.Adapter<FriendListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_list_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.displayname.text = " name : " + itemList[position].displayname
        holder.email.text = " email : " + itemList[position].email
        Glide.with(holder.userImg.context).load(itemList[position].photo_url.toString()).into(holder.userImg)


        FirestoreHelper().checkFollowing(itemList[position].uid, holder.follow_button)
        holder.follow_button.setOnClickListener {
            //친구추가
            FirestoreHelper().updateFollower(itemList[position].uid, holder.follow_button)
        }

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val displayname: TextView = itemView.findViewById(R.id.list_name)
        val email: TextView = itemView.findViewById(R.id.list_email)
        val follow_button: Button = itemView.findViewById(R.id.follow_button)
        val userImg: ImageView = itemView.findViewById(R.id.User_list_img)
    }
}