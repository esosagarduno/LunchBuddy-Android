package com.mmrnd.lunchbuddy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.MenuItem

/**
 * Created by Esteban Sosa
 */

class AddInterestActivity : AppCompatActivity() {

    // GUI elements
    var toolbar: Toolbar? = null
    var searchView: SearchView? = null
    var recyclerView: RecyclerView? = null

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
