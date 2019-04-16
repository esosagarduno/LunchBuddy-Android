package com.mmrnd.lunchbuddy

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Esteban Sosa
 */

class SignUpActivity : AppCompatActivity() {

    // GUI elements
    var toolbar: Toolbar? = null
    var nameEditText: EditText? = null
    var emailEditText: EditText? = null
    var confirmEmailEditText: EditText? = null
    var passwordEditText: EditText? = null
    var confirmPasswordEditText: EditText? = null
    var progressDialog: ProgressDialog? = null
    var signUpButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Get GUI elements
        toolbar = findViewById(R.id.signup_toolbar)
        nameEditText = findViewById(R.id.signup_name_edittext)
        emailEditText = findViewById(R.id.signup_email_edittext)
        confirmEmailEditText = findViewById(R.id.signup_confirmemail_edittext)
        passwordEditText = findViewById(R.id.signup_password_edittext)
        confirmPasswordEditText = findViewById(R.id.signup_confirmpassword_edittext)
        signUpButton = findViewById(R.id.signup_signup_button)

        // Initialize toolbar
        toolbar!!.title = "Create Account"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Initialize progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setIndeterminate(true)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)

        // Set on click listener
        signUpButton!!.setOnClickListener({ view->
            signUp()
        })
    }

    // Sign up
    private fun signUp() {
        progressDialog!!.show()
        // Get variables
        val name = nameEditText!!.text.toString()
        val email = emailEditText!!.text.toString().toLowerCase()
        val confirmEmail = confirmEmailEditText!!.text.toString().toLowerCase()
        val password = passwordEditText!!.text.toString()
        val confirmPassword = confirmPasswordEditText!!.text.toString()
        // Validate
        if(validateForm(name, email, confirmEmail, password, confirmPassword)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->
                progressDialog!!.dismiss()
                if(task.isSuccessful) {
                    // Update database
                    updateDatabase(name, email, password)
                    showToast("Account created successfully. Please sign in.")
                    // Go back to login screen
                    this.finish()
                }
                else {
                    showToast(task.exception!!.message.toString())
                }
            }
        }
        else {
            progressDialog!!.dismiss()
        }
    }

    // Update database
    private fun updateDatabase(name: String, email: String, password: String) {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().reference.child(DatabaseManager.USERS).child(uid)
        ref.child(DatabaseManager.NAME).setValue(name)
        ref.child(DatabaseManager.EMAIL).setValue(email)
        ref.child(DatabaseManager.LOCATION).setValue(0)
    }

    // Validate form
    private fun validateForm(name: String, email: String, confirmEmail: String, password: String, confirmPassword: String): Boolean {
        // Check if empty
        if(name.isEmpty() || email.isEmpty() || confirmEmail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showToast("All fields are required")
            return false
        }
        // Check if emails match
        if(email != confirmEmail) {
            showToast("Emails don't match")
            return false
        }
        // Check if valid email address
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Email address format is invalid")
            return false
        }
        // Check if password is less than 8 characters
        if(password.length < 8) {
            showToast("Password must be at least 8 characters")
            return false
        }
        // Check if passwords match
        if(password != confirmPassword) {
            showToast("Passwords don't match")
            return false
        }
        return true
    }

    // Show toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if(id == android.R.id.home) {
            this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        this.finish()
    }
}
