package com.mmrnd.lunchbuddy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem

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
        // TODO implement
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
}
