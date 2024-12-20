package com.example.a20practicheskaya

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var birthDateEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var loadButton: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        database = Firebase.database.reference

        saveButton.setOnClickListener { saveUser() }
        loadButton.setOnClickListener { loadUser() }
    }

    private fun initUI() {
        firstNameEditText = EditText(this).apply { hint = "First Name"; id = View.generateViewId() }
        lastNameEditText = EditText(this).apply { hint = "Last Name"; id = View.generateViewId() }
        addressEditText = EditText(this).apply { hint = "Address"; id = View.generateViewId() }
        phoneEditText = EditText(this).apply { hint = "Phone"; id = View.generateViewId() }
        emailEditText = EditText(this).apply { hint = "Email"; id = View.generateViewId() }
        birthDateEditText = EditText(this).apply { hint = "Birth Date"; id = View.generateViewId() }
        saveButton = Button(this).apply { text = "Save"; id = View.generateViewId() }
        loadButton = Button(this).apply { text = "Load"; id = View.generateViewId() }

        val rootLayout = ConstraintLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        listOf(firstNameEditText, lastNameEditText, addressEditText, phoneEditText, emailEditText, birthDateEditText, saveButton, loadButton)
            .forEach { rootLayout.addView(it) }

        val constraintSet = ConstraintSet()
        constraintSet.clone(rootLayout)

        constraintSet.connect(firstNameEditText.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 16)
        constraintSet.connect(firstNameEditText.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 16)
        constraintSet.connect(firstNameEditText.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 16)

        constraintSet.connect(lastNameEditText.id, ConstraintSet.TOP, firstNameEditText.id, ConstraintSet.BOTTOM, 16)
        constraintSet.connect(lastNameEditText.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 16)
        constraintSet.connect(lastNameEditText.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 16)

        constraintSet.connect(addressEditText.id, ConstraintSet.TOP, lastNameEditText.id, ConstraintSet.BOTTOM, 16)
        constraintSet.connect(addressEditText.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 16)
        constraintSet.connect(addressEditText.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 16)

        constraintSet.connect(phoneEditText.id, ConstraintSet.TOP, addressEditText.id, ConstraintSet.BOTTOM, 16)
        constraintSet.connect(phoneEditText.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 16)
        constraintSet.connect(phoneEditText.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 16)

        constraintSet.connect(emailEditText.id, ConstraintSet.TOP, phoneEditText.id, ConstraintSet.BOTTOM, 16)
        constraintSet.connect(emailEditText.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 16)
        constraintSet.connect(emailEditText.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 16)

        constraintSet.connect(birthDateEditText.id, ConstraintSet.TOP, emailEditText.id, ConstraintSet.BOTTOM, 16)
        constraintSet.connect(birthDateEditText.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 16)
        constraintSet.connect(birthDateEditText.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 16)

        constraintSet.connect(saveButton.id, ConstraintSet.TOP, birthDateEditText.id, ConstraintSet.BOTTOM, 16)
        constraintSet.connect(saveButton.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 16)
        constraintSet.connect(loadButton.id, ConstraintSet.TOP, birthDateEditText.id, ConstraintSet.BOTTOM, 16)
        constraintSet.connect(loadButton.id, ConstraintSet.START, saveButton.id, ConstraintSet.END, 16)
        constraintSet.connect(loadButton.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 16)

        constraintSet.applyTo(rootLayout)
        setContentView(rootLayout)
    }

    private fun saveUser() {
        val user = User(
            firstNameEditText.text.toString(),
            lastNameEditText.text.toString(),
            addressEditText.text.toString(),
            phoneEditText.text.toString(),
            emailEditText.text.toString(),
            birthDateEditText.text.toString()
        )
        database.child("users").push().setValue(user)
    }

    private fun loadUser() {
        database.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    user?.let {
                        Log.d("Firebase", "Name: ${it.firstName} ${it.lastName}, Address: ${it.address}, Phone: ${it.phone}, Email: ${it.email}, Birth Date: ${it.birthDate}")
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Error loading users", databaseError.toException())
            }
        })
    }
}