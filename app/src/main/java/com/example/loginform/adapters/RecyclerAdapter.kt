package com.example.loginform.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.loginform.R
import com.example.loginform.data.User

class RecyclerAdapter(
    private var userList: List<User>,
    private var onEditClick: (User) -> Unit,
    private val onDeleteClick: (User) -> Unit) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_user_details, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.tvUserName.text = "User: ${currentUser.username}"

        holder.ivEditUser.setOnClickListener {
            onEditClick(currentUser)
        }

        // Handle clicks on the Image (e.g., delete or view profile)
        holder.ivUserImage.setOnClickListener {
            onDeleteClick(currentUser)
            Toast.makeText(holder.itemView.context, "${currentUser.username} user Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = userList.size

    fun UpdateList(newList: List<User>)
    {
        userList = newList
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Ensure these IDs match exactly what is in recycler_user_details.xml
        val tvUserName: TextView = itemView.findViewById(R.id.recycler_user_id)
        val ivUserImage: ImageView = itemView.findViewById(R.id.recycler_iv_id)

        val ivEditUser: ImageView = itemView.findViewById(R.id.iv_edit_user)
    }
}