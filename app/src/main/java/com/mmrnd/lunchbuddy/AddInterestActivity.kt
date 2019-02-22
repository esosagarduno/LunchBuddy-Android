package com.mmrnd.lunchbuddy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import com.google.firebase.database.*

/**
 * Created by Esteban Sosa
 */

class AddInterestActivity : AppCompatActivity() {

    // GUI elements
    var toolbar: Toolbar? = null
    var searchView: SearchView? = null
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
        recyclerView = findViewById(R.id.add_interest_recyclerview)

        // Initialize toolbar
        toolbar!!.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Initialize Firebase
        ref = FirebaseDatabase.getInstance().reference.child(DatabaseManager.INTERESTS)

        // Initialize variables
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        interests = ArrayList()
        interestsAdapter = InterestsAdapter(this, interests, object: InterestsAdapter.OnInterestInterface {
            override fun interestClicked(index: Int) {
                TODO("not implemented")
            }
        })
        recyclerView!!.adapter = interestsAdapter
        interestsAdapter!!.notifyDataSetChanged()

        // Fetch interests
        fetchInterests()
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
                        Log.d("INTEREST", childSnap.key.toString())
                        interests.add(childSnap.key.toString())
                        interestsAdapter!!.notifyDataSetChanged()
                    }
                }
                else {
                    Log.d("TAG", "NOT EXISTS")
                }
            }
            override fun onCancelled(snapshot: DatabaseError) {
                Log.d("TAG", "ERROR")
            }
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
