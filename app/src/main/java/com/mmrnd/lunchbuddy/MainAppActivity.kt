package com.mmrnd.lunchbuddy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.renderscript.Sampler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * Created by Esteban Sosa
 */

class MainAppActivity : AppCompatActivity() {

    // GUI elements
    var toolbar: Toolbar? = null
    var searchView: SearchView? = null
    var recyclerView: RecyclerView? = null
    var findNowButton: TextView? = null

    // Variables
    var interests = ArrayList<String>()
    var interestsAdapter: InterestsAdapter? = null

    // Firebase
    var ref: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_app)

        // Find GUI elements
        toolbar = findViewById(R.id.main_app_toolbar)
        searchView = findViewById(R.id.main_app_searchview)
        recyclerView = findViewById(R.id.main_app_recyclerview)
        findNowButton = findViewById(R.id.main_app_findsomeone_button)

        // Set on click listeners
        findNowButton!!.setOnClickListener({ view->
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        })

        // Set search view query text listener
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

        // Initialize toolbar
        toolbar!!.title = "Lunch Buddy"
        setSupportActionBar(toolbar)

        // Initialize Firebase
        ref = FirebaseDatabase.getInstance().reference.child(DatabaseManager.INTERESTS)

        // Initialize variables
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        interests = ArrayList()
        interestsAdapter = InterestsAdapter(this, interests, object: InterestsAdapter.OnInterestInterface {
            override fun interestClicked(index: Int) {
                goToSelectedInterest(interestsAdapter!!.interests.get(index))
            }
        })
        recyclerView!!.adapter = interestsAdapter
        interestsAdapter!!.notifyDataSetChanged()

        // Check if user is logged in
        checkifUserLoggedIn()

        // Fetch interests
        fetchInterests()
    }

    // Check if user is logged in
    private fun checkifUserLoggedIn() {
        val user = FirebaseAuth.getInstance().currentUser
        if(user == null) {
            this.finish()
        }
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
                else { }
            }
            override fun onCancelled(snapshot: DatabaseError) { }
        })
    }

    // Go to selected interest
    private fun goToSelectedInterest(interestTitle: String) {
        val intent = Intent(this, SelectedInterestActivity::class.java)
        intent.putExtra(SelectedInterestActivity().INTEREST_TITLE, interestTitle)
        startActivity(intent)
    }

    // Go to my profile
    private fun goToMyProfile() {
        val intent = Intent(this, MyProfileActivity::class.java)
        startActivity(intent)
    }

    // Menu methods
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if(id == R.id.main_menu_myprofile) {
            goToMyProfile()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}
