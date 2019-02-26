package com.mmrnd.lunchbuddy

//import android.app.ProgressDialog
import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by Esteban Sosa
 */

class MainActivity : AppCompatActivity() {

    // GUI elements
    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null
    var loginButton: Button? = null
    var signUpButton: Button? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get GUI elements
        emailEditText = findViewById(R.id.login_email_edittext)
        passwordEditText = findViewById(R.id.login_password_edittext)
        loginButton = findViewById(R.id.login_login_button)
        signUpButton = findViewById(R.id.login_signup_button)

        // Initialize progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setIndeterminate(true)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)

        // Set on click listeners
        loginButton!!.setOnClickListener({ view ->
            login()
        })
        signUpButton!!.setOnClickListener({ view->
            goToSignUp()
        })

        // Check if user is logged in
        checkIsUserLoggedIn()
    }

    // Check if user is logged in
    private fun checkIsUserLoggedIn() {
        val user = FirebaseAuth.getInstance().currentUser
        if(user != null) {
            val intent = Intent(this, MainAppActivity::class.java)
            startActivity(intent)
        }
    }

    // Login
    private fun login() {
        progressDialog!!.show()
        // Get variables
        val email = emailEditText!!.text.toString()
        val password = passwordEditText!!.text.toString()
        // If valid, login
        if(validateForm(email, password)) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task->
                progressDialog!!.dismiss()
                if(task.isSuccessful) {
                    val intent = Intent(this, MainAppActivity::class.java)
                    startActivity(intent)
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

    // Validate form
    private fun validateForm(email: String, password: String): Boolean {
        // Check if empty
        if(email.isEmpty() || password.isEmpty()) {
            showToast("Invalid email/password")
            return false
        }
        return true
    }

    // Go to sign up
    private fun goToSignUp() {

    }

    // Show toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
