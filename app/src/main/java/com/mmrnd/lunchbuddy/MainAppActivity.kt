package com.mmrnd.lunchbuddy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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

        // Initialize Firebase
        ref = FirebaseDatabase.getInstance().reference
    }
}
