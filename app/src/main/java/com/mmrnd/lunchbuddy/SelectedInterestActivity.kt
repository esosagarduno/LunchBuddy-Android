package com.mmrnd.lunchbuddy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.firebase.database.*
import java.sql.DatabaseMetaData

/**
 * Created by Esteban Sosa
 */

class SelectedInterestActivity : AppCompatActivity() {

    // Intent bundle identifiers strings
    public val INTEREST_TITLE = "InterestTitle"

    // GUI elements
    var toolbar: Toolbar? = null
    var toolbarTitle: TextView? = null
    var recyclerView: RecyclerView? = null
    var noUsersTextView: TextView? = null

    // Variables
    var interestTitle: String = ""
    var adapter: SelectedInterestUsersAdapter? = null
    var users = ArrayList<User>()

    // Firebase
    var ref: DatabaseReference? = null
    var interestRef: DatabaseReference? = null
    var usersRef: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_interest)

        // Get GUI elements
        toolbar = findViewById(R.id.selected_interest_toolbar)
        toolbarTitle = findViewById(R.id.selected_interest_toolbar_title)
        recyclerView = findViewById(R.id.selected_interest_recyclerview)
        noUsersTextView = findViewById(R.id.selected_interest_nousers_textview)

        // Get intent variables
        interestTitle = intent.getStringExtra(INTEREST_TITLE)

        // Initialize toolbar
        toolbarTitle!!.text = interestTitle
        toolbar!!.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Initialize recycler view
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        users = ArrayList()
        adapter = SelectedInterestUsersAdapter(this, users, object: SelectedInterestUsersAdapter.OnSelectedInterestInterface {
            override fun userClicked(index: Int) {
                goToSelectedUser(users.get(index))
            }
        })
        recyclerView!!.adapter = adapter
        adapter!!.notifyDataSetChanged()

        // Initialize Firebase
        ref = FirebaseDatabase.getInstance().reference
        interestRef = ref!!.child(DatabaseManager.INTERESTS).child(interestTitle).child(DatabaseManager.INTEREST_USERS)
        usersRef = ref!!.child(DatabaseManager.USERS)

        // Fetch users
        fetchUsers()
    }

    // Go to selected user
    private fun goToSelectedUser(user: User) {
        val intent = Intent(this, SelectedUserActivity::class.java)
        val newActivity = SelectedUserActivity()
        intent.putExtra(newActivity.USER_ID, user.id)
        intent.putExtra(newActivity.USER_NAME, user.name)
        intent.putExtra(newActivity.USER_EMAIL, user.email)
        intent.putExtra(newActivity.USER_PHOTO, user.userPhoto)
        intent.putExtra(newActivity.INTEREST_TITLE, interestTitle)
        intent.putExtra(newActivity.INTEREST_EXPERIENCE, user.experience)
        intent.putExtra(newActivity.INTEREST_DETAILS, user.details)
        startActivity(intent)
    }

    // Fetch users
    private fun fetchUsers() {
        // Clear list
        users.clear()
        adapter!!.notifyDataSetChanged()
        noUsersTextView!!.visibility = View.GONE
        // Fetch users
        interestRef!!.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                    for(childSnap in dataSnapshot.children) {
                        val userId = childSnap.key.toString()
                        usersRef!!.child(userId).addListenerForSingleValueEvent(object: ValueEventListener {
                            override fun onDataChange(userSnap: DataSnapshot) {
                                if(userSnap.exists()) {
                                    // Fetch basic info
                                    val name = userSnap.child(DatabaseManager.NAME).value.toString()
                                    val email = userSnap.child(DatabaseManager.EMAIL).value.toString()
                                    var photoUri = ""
                                    if(userSnap.child(DatabaseManager.USER_PHOTO).exists()) {
                                        photoUri = userSnap.child(DatabaseManager.USER_PHOTO).value.toString()
                                    }
                                    // Fetch interest specific info
                                    if(userSnap.child(DatabaseManager.USER_INTERESTS).child(interestTitle).exists()) {
                                        val experience = userSnap.child(DatabaseManager.USER_INTERESTS).child(interestTitle).child(DatabaseManager.USER_INTEREST_LEVEL).value.toString().toInt()
                                        val details = userSnap.child(DatabaseManager.USER_INTERESTS).child(interestTitle).child(DatabaseManager.USER_INTEREST_DETAILS).value.toString()
                                        // Create new user
                                        val user = User(userId, name, email, photoUri, details, experience)
                                        users.add(user)
                                        adapter!!.notifyDataSetChanged()
                                    }
                                }
                            }
                            override fun onCancelled(userError: DatabaseError) { }
                        })
                    }
                }
                else {
                    noUsersTextView!!.visibility = View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) { }
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
