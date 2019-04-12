package com.mmrnd.lunchbuddy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * Created by Esteban Sosa
 */

class AddInterestActivity : AppCompatActivity() {

    // Validate new interest return codes
    val INTEREST_OK = 0
    val INTEREST_ALREADY_EXISTS = -2
    val INTEREST_EMPTY = -1
    val INTEREST_INVALID = -3

    // GUI elements
    var toolbar: Toolbar? = null
    var searchView: SearchView? = null
    var createNewTextView: TextView? = null
    var recyclerView: RecyclerView? = null

    // Firebase
    var ref: DatabaseReference? = null

    // Variables
    var interests = ArrayList<String>()
    var interestsAdapter: InterestsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_interest)

        // Get GUI elements
        toolbar = findViewById(R.id.add_interest_toolbar)
        searchView = findViewById(R.id.add_interest_searchview)
        createNewTextView = findViewById(R.id.add_interest_createnew_textview)
        recyclerView = findViewById(R.id.add_interest_recyclerview)

        // Set on click listeners
        createNewTextView!!.setOnClickListener ({ view ->
            showCreateInterestDialog()
        })

        // Initialize toolbar
        toolbar!!.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Initialize Firebase
        ref = FirebaseDatabase.getInstance().reference.child(DatabaseManager.INTERESTS)

        // Set search view query listener
        searchView!!.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(text: String?): Boolean {
                var userInput = text!!.toLowerCase()
                var newList = ArrayList<String>()
                for(interest in interests) {
                    if(interest.toLowerCase().startsWith(userInput)) {
                        newList.add(interest)
                    }
                }
                interestsAdapter!!.updateList(newList)
                return true
            }
            override fun onQueryTextSubmit(text: String?): Boolean {
                return false
            }
        })

        // Initialize variables
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        interests = ArrayList()
        interestsAdapter = InterestsAdapter(this, interests, object: InterestsAdapter.OnInterestInterface {
            override fun interestClicked(index: Int) {
                goToEditInterest(interestsAdapter!!.interests.get(index))
            }
        })
        recyclerView!!.adapter = interestsAdapter
        interestsAdapter!!.notifyDataSetChanged()

        // Fetch interests
        fetchInterests()
    }

    // Show create interest dialog
    private fun showCreateInterestDialog() {
        //Create alert dialog builder
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Create new interest")
        builder.setMessage("Please enter the new interest")
        builder.setCancelable(true)
        // Set edit text
        val createEditText = EditText(this)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(64, 2, 64, 2)
        createEditText.layoutParams = layoutParams
        createEditText.hint = "ex. Hiking"
        builder.setView(createEditText)
        //Set buttons
        builder.setPositiveButton("Create") { dialog, which ->
            createInterest(createEditText.text.toString())
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }
        //Create alert dialog
        val alertDialog = builder.create()
        alertDialog.show()
    }

    // Create interest
    private fun createInterest(input: String) {
        val finalString = formatInterest(input)
        val isInterestValid = validateInterest(finalString)
        if(isInterestValid == INTEREST_OK) {
            // Update database with new interest
            updateDatabaseWithNewInterest(finalString)
            // Fetch interests again
            fetchInterests()
        }
        else if(isInterestValid == INTEREST_INVALID) {
            showErrorToast("Invalid input")

        }
        else if(isInterestValid == INTEREST_ALREADY_EXISTS) {
            showErrorToast("Interest already exists")
        }
    }

    // Update database with new interest
    private fun updateDatabaseWithNewInterest(input: String) {
        // Add to interests database
        ref!!.child(input).child(DatabaseManager.ADDED_BY).setValue(0)
        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null) {
            ref!!.child(input).child(DatabaseManager.ADDED_BY).setValue(currentUser!!.uid)
        }
        showErrorToast("Interest successfully added")
    }

    // Format interest
    fun formatInterest(input: String): String {
        var trimmedString = input.trim()
        val re = Regex("[^A-Za-z0-9 ]")
        var finalString = re.replace(trimmedString, "")
        return finalString
    }

    // Validate interest
    fun validateInterest(input: String): Int {
        // Check if string is empty
        if(input.isEmpty()) {
            return INTEREST_INVALID
        }
        // Check if it contains non-alphanumeric characters
        for(i in 0 until input.length) {
            if(!input[i].isLetterOrDigit()) {
                return INTEREST_INVALID
            }
        }
        // Check if already in interests
        for(interest in interests) {
            if (interest == input) {
                return INTEREST_ALREADY_EXISTS
            }
        }
        return INTEREST_OK
    }

    // Show error toast
    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    // Go to edit interest
    private fun goToEditInterest(interest: String) {
        val intent = Intent(this, EditInterestActivity::class.java)
        val newActivity = EditInterestActivity()
        intent.putExtra(newActivity.IS_EDIT_INTEREST, false)
        intent.putExtra(newActivity.EDIT_INTEREST_TITLE, interest)
        intent.putExtra(newActivity.EDIT_INTEREST_SPINNER_VALUE, 0)
        intent.putExtra(newActivity.EDIT_INTEREST_DETAILS, "")
        startActivity(intent)
    }

    // Fetch interests
    private fun fetchInterests() {
        // Clear list and recycler view
        interests.clear()
        interestsAdapter!!.notifyDataSetChanged()
        // Retrieve info
        ref!!.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(childSnap in snapshot.children) {
                        interests.add(childSnap.key.toString())
                        interestsAdapter!!.notifyDataSetChanged()
                    }
                }
            }
            override fun onCancelled(snapshot: DatabaseError) { }
        })
    }

    // Menu methods
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
