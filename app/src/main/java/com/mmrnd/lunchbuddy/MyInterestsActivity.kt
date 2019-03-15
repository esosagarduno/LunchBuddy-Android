package com.mmrnd.lunchbuddy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

/**
 * Created by Esteban Sosa
 */

class MyInterestsActivity : AppCompatActivity() {

    // GUI elements
    var toolbar: Toolbar? = null
    var recyclerView: RecyclerView? = null

    // Variables
    var interestsAdapter: MyInterestsAdapter? = null
    var interests = ArrayList<MyInterest>()

    // Firebase
    var currentUser: FirebaseUser? = null
    var ref: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_interests)

        // Get GUI elements
        toolbar = findViewById(R.id.my_interests_toolbar)
        recyclerView = findViewById(R.id.my_interests_recyclerview)

        // Initialize toolbar
        toolbar!!.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Initialize recycler view
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        interests = ArrayList()
        interestsAdapter = MyInterestsAdapter(this, interests, object: MyInterestsAdapter.OnMyInterestInterface {
            override fun interestClicked(index: Int) {
                goToEditInterest(interests.get(index))
            }
        })
        recyclerView!!.adapter = interestsAdapter
        interestsAdapter!!.notifyDataSetChanged()

        // Firebase
        currentUser = FirebaseAuth.getInstance().currentUser
    }

    // Go to edit interest
    private fun goToEditInterest(interest: MyInterest) {
        val intent = Intent(this, EditInterestActivity::class.java)
        val newActivity = EditInterestActivity()
        intent.putExtra(newActivity.IS_EDIT_INTEREST, true)
        intent.putExtra(newActivity.EDIT_INTEREST_TITLE, interest.title)
        intent.putExtra(newActivity.EDIT_INTEREST_SPINNER_VALUE, interest.level)
        intent.putExtra(newActivity.EDIT_INTEREST_DETAILS, interest.details)
        startActivity(intent)
    }

    // Fetch interests
    private fun fetchInterests() {
        // Clear list
        interests.clear()
        interestsAdapter!!.notifyDataSetChanged()
        // Fetch info
        if(currentUser != null) {
            ref = FirebaseDatabase.getInstance().reference.child(DatabaseManager.USERS).child(currentUser!!.uid).child(DatabaseManager.USER_INTERESTS)
            ref!!.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(childSnap in snapshot.children) {
                        val title = childSnap.key
                        val level = childSnap.child(DatabaseManager.USER_INTEREST_LEVEL).value.toString().toInt()
                        val details = childSnap.child(DatabaseManager.USER_INTEREST_DETAILS).value.toString()
                        val newInterest = MyInterest(title, level, details)
                        interests.add(newInterest)
                        interestsAdapter!!.notifyDataSetChanged()
                    }
                }
                override fun onCancelled(error: DatabaseError) { }
            })
        }
    }

    // Go to add interest
    private fun goToAddInterest() {
        startActivity(Intent(this, AddInterestActivity::class.java))
    }

    // Menu methods
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_interests_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if(id == R.id.my_interests_plus) {
            goToAddInterest()
            return true
        }
        if(id == android.R.id.home) {
            this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        this.finish()
    }

    override fun onResume() {
        super.onResume()
        fetchInterests()
    }
}
