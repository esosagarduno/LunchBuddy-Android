package com.mmrnd.lunchbuddy;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;

import java.util.ArrayList;

/**
 * Created by Esteban Sosa
 */

public class AppActivity extends AppCompatActivity {

    // GUI elements
    Toolbar toolbar;
    SearchView searchView;
    RecyclerView recyclerView;
    TextView findNowButton;

    // Variables
    ArrayList<String> interests;
    InterestsAdapter interestsAdapter;

    // Firebase
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        // Get GUI elements
        /*toolbar = findViewById(R.id.main_app_toolbar);
        searchView = findViewById(R.id.main_app_searchview);
        recyclerView = findViewById(R.id.main_app_recyclerview);
        findNowButton = findViewById(R.id.main_app_findsomeone_button);*/

        // Initialize Firebase
        ref = FirebaseDatabase.getInstance().getReference();
        Log.d("TAG", ref.toString());

        // Initialize variables
        interests = new ArrayList<>();
        interestsAdapter = new InterestsAdapter(this, interests, new InterestsAdapter.OnInterestInterface() {
            @Override
            public void interestClicked(int index) {
                //TODO IMPLEMENT
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(interestsAdapter);
        interestsAdapter.notifyDataSetChanged();

        findNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AppActivity.this, "CLICKED", Toast.LENGTH_LONG).show();
                ref.child("ANDROID").setValue(true);
                FirebaseDatabase.getInstance().getReference().child("HELLO THERE").setValue("STUFF");
            }
        });

        // Fetch interests
        fetchInterests();
    }

    // Fetch interests
    private void fetchInterests() {
        interests.clear();
        interestsAdapter.notifyDataSetChanged();
        DatabaseReference interestsRef = FirebaseDatabase.getInstance().getReference().child(DatabaseManager.INTERESTS);
        interestsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG", "STARTING");
                if(dataSnapshot.exists()) {
                    Log.d("TAG", dataSnapshot.child("Gaming").getKey());
                    Log.d("TAG", "EXISTS");
                    for(DataSnapshot childSnap : dataSnapshot.getChildren()) {
                        interests.add(childSnap.getKey());
                        interestsAdapter.notifyDataSetChanged();
                        Log.d("TAG", childSnap.getKey());
                    }
                }
                else {
                    Log.d("TAG", "Doesnt exist");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", databaseError.getDetails());
            }
        });
    }
}
