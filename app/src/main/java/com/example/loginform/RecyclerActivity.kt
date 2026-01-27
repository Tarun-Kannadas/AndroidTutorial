package com.example.loginform

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginform.adapters.RecyclerAdapter
import com.example.loginform.data.User
import com.example.loginform.database.LoginDatabase
import com.example.loginform.repository.UserRepository
import com.example.loginform.utils.DialogModifier // Import your custom dialog class
import kotlinx.coroutines.launch

class RecyclerActivity : AppCompatActivity() {

    private lateinit var adapter: RecyclerAdapter
    private lateinit var repository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        val recyclerView = findViewById<RecyclerView>(R.id.recycle_view_id)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val db = LoginDatabase.getDatabase(this)
        repository = UserRepository(db.UserDao())

        loadUsers(recyclerView)
    }

    private fun loadUsers(recyclerView: RecyclerView) {
        lifecycleScope.launch {
            val userList = repository.getAllUsers()

            // Initialize Adapter with the Dialog Logic
            adapter = RecyclerAdapter(userList) { userClicked ->

                // 1. Create instance of your custom DialogModifier
                val dialog = DialogModifier(this@RecyclerActivity)

                // 2. Show the dialog with your custom message
                dialog.showDialog(
                    title = "Delete User?",
                    message = "Are you sure you want to remove ${userClicked.username}?"
                ) { responseType ->

                    // 3. Check the response type
                    if (responseType == DialogModifier.ResponseType.YES) {
                        // Only delete if they clicked YES
                        performDelete(userClicked)
                    } else {
                        // Optional: Handle NO or CANCEL
                        Toast.makeText(this@RecyclerActivity, "Action Cancelled", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            recyclerView.adapter = adapter
        }
    }

    // Renamed to 'performDelete' to be clear this is the actual action
    private fun performDelete(user: User) {
        lifecycleScope.launch {
            repository.deleteUser(user)
            Toast.makeText(this@RecyclerActivity, "Deleted ${user.username}", Toast.LENGTH_SHORT).show()

            // Refresh list
            val updatedList = repository.getAllUsers()
            adapter.UpdateList(updatedList) // Make sure to use correct casing (UpdateList vs updateList)
        }
    }
}