package com.mmrnd.lunchbuddy

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Esteban Sosa
 */

class EditInterestActivity : AppCompatActivity() {

    // Strings to get intent values
    val IS_EDIT_INTEREST = "IsEditInterest"
    val EDIT_INTEREST_TITLE = "EditInterestTitle"
    val EDIT_INTEREST_SPINNER_VALUE = "EditInterestSpinnerValue"
    val EDIT_INTEREST_DETAILS = "EditInterestDetails"

    // GUI elements
    var toolbar: Toolbar? = null
    var toolbarTitleTextView: TextView? = null
    var titleTextView: TextView? = null
    var spinner: Spinner? = null
    var detailsTextView: TextView? = null
    var removeButton: Button? = null

    // Variables
    var isEditInterest = false
    var title = ""
    var level = -1
    var details = ""

    // Firebase
    var currentUser: FirebaseUser? = null
    var ref: DatabaseReference? = null
    var userRef: DatabaseReference? = null
    var interestRef: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_interest)

        // Get GUI elements
        toolbar = findViewById(R.id.edit_interest_toolbar)
        toolbarTitleTextView = findViewById(R.id.edit_interest_toolbar_title_textview)
        titleTextView = findViewById(R.id.edit_interest_title_textview)
        spinner = findViewById(R.id.edit_interest_spinner)
        detailsTextView = findViewById(R.id.edit_interest_details_edittext)
        removeButton = findViewById(R.id.edit_interest_remove_button)

        // Initialize toolbar
        toolbar!!.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Get intent variables
        isEditInterest = intent.getBooleanExtra(IS_EDIT_INTEREST, false)
        title = intent.getStringExtra(EDIT_INTEREST_TITLE)
        level = intent.getIntExtra(EDIT_INTEREST_SPINNER_VALUE, 0)
        details = intent.getStringExtra(EDIT_INTEREST_DETAILS)

        // Display info
        if(isEditInterest) {
            toolbarTitleTextView!!.text = "Edit Interest"
        }
        else {
            toolbarTitleTextView!!.text = "Add Interest"
        }
        titleTextView!!.text = title
        spinner!!.setSelection(level)
        detailsTextView!!.text = details
        if(!isEditInterest) {
            removeButton!!.visibility = View.GONE
        }

        // Set click listener
        removeButton!!.setOnClickListener({ view ->
            showRemoveInterestDialog()
        })

        // Firebase
        currentUser = FirebaseAuth.getInstance().currentUser
        ref = FirebaseDatabase.getInstance().reference
    }

    // Save changes
    private fun saveChanges() {
        // Get variables
        val newDetails = detailsTextView!!.text.toString()
        // Check if empty
        if(newDetails.isEmpty()) {
            Toast.makeText(this, "Please provide details for what you are looking for", Toast.LENGTH_LONG).show()
            return
        }
        // If not empty, process
        else {
            if(currentUser != null) {
                // Update details
                ref!!.child(DatabaseManager.USERS).child(currentUser!!.uid).child(DatabaseManager.USER_INTERESTS).child(title).child(DatabaseManager.USER_INTEREST_DETAILS).setValue(newDetails)
                // Update level
                ref!!.child(DatabaseManager.USERS).child(currentUser!!.uid).child(DatabaseManager.USER_INTERESTS).child(title).child(DatabaseManager.USER_INTEREST_LEVEL).setValue(spinner!!.selectedItemPosition)
                // If adding the interest from scratch, update the interest database as well
                if(!isEditInterest) {
                    ref!!.child(DatabaseManager.INTERESTS).child(title).child(DatabaseManager.INTEREST_USERS).child(currentUser!!.uid).setValue(true)
                    Toast.makeText(this, "Interest successfully added", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(this, "Changes saved", Toast.LENGTH_LONG).show()
                }
            }
            // Go to previous activity
            this.finish()
        }
    }

    // Remove interest
    private fun removeInterest() {
        if(currentUser != null) {
            // Remove interest from user's list
            ref!!.child(DatabaseManager.USERS).child(currentUser!!.uid).child(DatabaseManager.USER_INTERESTS).child(title).removeValue()
            // Remove user from interest list of users
            ref!!.child(DatabaseManager.INTERESTS).child(title).child(DatabaseManager.INTEREST_USERS).child(currentUser!!.uid).removeValue()
        }
        Toast.makeText(this, "Changes saved", Toast.LENGTH_LONG).show()
        this.finish()
    }

    // Sign out alert dialog
    private fun showRemoveInterestDialog() {
        //Create alert dialog builder
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to remove this interest?")
        builder.setCancelable(true)
        //Set buttons
        builder.setPositiveButton("Yes") { dialog, which ->
            removeInterest()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }
        //Create alert dialog
        val alertDialog = builder.create()
        alertDialog.show()
    }


    // Menu methods
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_profile_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if(id == android.R.id.home) {
            this.finish()
        }
        else if(id == R.id.my_profile_save) {
            saveChanges()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        this.finish()
    }
}
