package com.example.loginform

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginform.adapters.RecyclerAdapter
import com.example.loginform.data.User
import com.example.loginform.database.LoginDatabase
import com.example.loginform.repository.UserRepository
import com.example.loginform.utils.DialogModifier
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

            // Initialize Adapter with TWO listeners: Delete and Edit
            adapter = RecyclerAdapter(
                userList,
                onDeleteClick = { userClicked ->
                    showDeleteConfirmation(userClicked)
                },
                onEditClick = { userClicked ->
                    showEditDialog(userClicked)
                }
            )

            recyclerView.adapter = adapter
        }
    }

    // --- DELETE LOGIC ---
    private fun showDeleteConfirmation(user: User) {
        val dialog = DialogModifier(this)
        dialog.showDialog(
            title = "Delete User?",
            message = "Are you sure you want to remove ${user.username}?"
        ) { responseType ->
            if (responseType == DialogModifier.ResponseType.YES) {
                performDelete(user)
            } else {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performDelete(user: User) {
        lifecycleScope.launch {
            repository.deleteUser(user)
            Toast.makeText(this@RecyclerActivity, "Deleted ${user.username}", Toast.LENGTH_SHORT).show()
            refreshList()
        }
    }

    // --- EDIT LOGIC ---
    private fun showEditDialog(user: User) {
        // 1. Inflate the custom layout (dialog_edit_user.xml)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_user, null)

        val etName = dialogView.findViewById<EditText>(R.id.et_edit_username)
        val etEmail = dialogView.findViewById<EditText>(R.id.et_edit_email)
        val etPhone = dialogView.findViewById<EditText>(R.id.et_edit_phone)
        val etPass = dialogView.findViewById<EditText>(R.id.et_edit_password)

        // 2. Pre-fill data
        etName.setText(user.username)
        etEmail.setText(user.email)
        etPhone.setText(user.number)
        etPass.setText(user.password)

        // 3. Build and Show Dialog
        AlertDialog.Builder(this)
            .setTitle("Edit User")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val newName = etName.text.toString()
                val newEmail = etEmail.text.toString()
                val newPhone = etPhone.text.toString()
                val newPass = etPass.text.toString()

                if (newName.isNotBlank() && newEmail.isNotBlank()) {
                    // Create updated user object (keeping the same ID is crucial!)
                    val updatedUser = user.copy(
                        username = newName,
                        email = newEmail,
                        number = newPhone,
                        password = newPass
                    )
                    performUpdate(updatedUser)
                } else {
                    Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun performUpdate(user: User) {
        lifecycleScope.launch {
            repository.updateUser(user)
            Toast.makeText(this@RecyclerActivity, "User Updated", Toast.LENGTH_SHORT).show()
            refreshList()
        }
    }

    // --- HELPER ---
    private fun refreshList() {
        lifecycleScope.launch {
            val updatedList = repository.getAllUsers()
            adapter.UpdateList(updatedList) // Matches the function name in RecyclerAdapter
        }
    }
}