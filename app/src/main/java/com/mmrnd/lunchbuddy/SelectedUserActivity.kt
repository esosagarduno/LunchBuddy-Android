package com.mmrnd.lunchbuddy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Esteban Sosa
 */

class SelectedUserActivity : AppCompatActivity() {

    // Intent string variables
    public val USER_ID = "UserId"
    public val USER_NAME = "UserName"
    public val USER_EMAIL = "UserEmail"
    public val USER_PHOTO = "UserPhoto"
    public val INTEREST_TITLE = "InterestTitle"
    public val INTEREST_EXPERIENCE = "InterestExperience"
    public val INTEREST_DETAILS = "InterestDetails"

    // GUI elements
    var toolbar: Toolbar? = null
    var imageView: CircleImageView? = null
    var nameTextView: TextView? = null
    var emailTextView: TextView? = null
    var interestTitleTextView: TextView? = null
    var expertiseTextView: TextView? = null
    var detailsTextView: TextView? = null
    var recyclerView: RecyclerView? = null

    // Variables
    var userId = ""
    var userName = ""
    var userEmail = ""
    var userPhoto = ""
    var interestTitle = ""
    var interestExperience = -1
    var interestDetails = ""
    var adapter: UsersInterestsAdapter? = null
    var interests = ArrayList<MyInterest>()

    // Firebase
    var ref: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_user)

        // Get GUI elements
        toolbar = findViewById(R.id.selected_user_toolbar)
        imageView = findViewById(R.id.selected_user_imageview)
        nameTextView = findViewById(R.id.selected_user_name_textview)
        emailTextView = findViewById(R.id.selected_user_email_textview)
        interestTitleTextView = findViewById(R.id.selected_user_interesttitle_textview)
        expertiseTextView = findViewById(R.id.selected_user_expertiselevel_textview)
        detailsTextView = findViewById(R.id.selected_user_details_textview)
        recyclerView = findViewById(R.id.selected_user_recyclerview)

        // Initialize toolbar
        toolbar!!.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Get intent variables
        userId = intent.getStringExtra(USER_ID)
        userName = intent.getStringExtra(USER_NAME)
        userEmail = intent.getStringExtra(USER_EMAIL)
        userPhoto = intent.getStringExtra(USER_PHOTO)
        interestTitle = intent.getStringExtra(INTEREST_TITLE)
        interestExperience = intent.getIntExtra(INTEREST_EXPERIENCE, 0)
        interestDetails = intent.getStringExtra(INTEREST_DETAILS)

        // Display info
        nameTextView!!.text = userName
        emailTextView!!.text = userEmail
        interestTitleTextView!!.text = interestTitle
        if(interestExperience == DatabaseManager.NOVICE) {
            expertiseTextView!!.text = "- Novice"
        }
        else if(interestExperience == DatabaseManager.OK) {
            expertiseTextView!!.text = "- OK"
        }
        else if(interestExperience == DatabaseManager.EXPERT) {
            expertiseTextView!!.text = "- Expert"
        }
        detailsTextView!!.text = interestDetails

        // Initialize firebase
        ref = FirebaseDatabase.getInstance().reference.child(DatabaseManager.USERS).child(userId).child(DatabaseManager.USER_INTERESTS)

        // Initialize variables
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        interests = ArrayList()
        adapter = UsersInterestsAdapter(this, interests, object: UsersInterestsAdapter.OnUsersInterestInterface {
            override fun interestClicked(index: Int) {
                displayInterest(interests.get(index))
            }
        })
        recyclerView!!.adapter = adapter
        adapter!!.notifyDataSetChanged()

        // Fetch interests
        fetchInterests()
    }

    // Fetch interests
    private fun fetchInterests() {
        // Clear list
        interests.clear()
        adapter!!.notifyDataSetChanged()
        // Fetch info
        ref!!.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(childSnap in snapshot.children) {
                        val title = childSnap.key
                        val level = childSnap.child(DatabaseManager.USER_INTEREST_LEVEL).value.toString().toInt()
                        val details = childSnap.child(DatabaseManager.USER_INTEREST_DETAILS).value.toString()
                        val newInterest = MyInterest(title, level, details)
                        interests.add(newInterest)
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) { }
        })
    }

    // Display interest
    private fun displayInterest(interest: MyInterest) {
        interestTitleTextView!!.text = interest.title
        if(interest.level == DatabaseManager.NOVICE) {
            expertiseTextView!!.text = "- Novice"
        }
        else if(interest.level == DatabaseManager.OK) {
            expertiseTextView!!.text = "- OK"
        }
        else if(interest.level == DatabaseManager.EXPERT) {
            expertiseTextView!!.text = "- Expert"
        }
        detailsTextView!!.text = interest.details
    }

    // Menu items
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
