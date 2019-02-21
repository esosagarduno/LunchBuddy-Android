package com.mmrnd.lunchbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Esteban Sosa
 */

public class MyProfileActivity extends AppCompatActivity {

    // GUI elements
    Toolbar toolbar;
    CircleImageView imageView;
    TextView emailTextView;
    EditText nameEditText;
    TextView interestsTextView;
    SwitchCompat locationSwitch;
    Button signOutButton;

    // Firebase
    DatabaseReference ref;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // Find GUI elements
        toolbar = findViewById(R.id.my_profile_toolbar);
        imageView = findViewById(R.id.my_profile_imageview);
        emailTextView = findViewById(R.id.my_profile_email_textview);
        nameEditText = findViewById(R.id.my_profile_name_edittext);
        interestsTextView = findViewById(R.id.my_profile_interests_textview);
        locationSwitch = findViewById(R.id.my_profile_location_switch);
        signOutButton = findViewById(R.id.my_profile_signout_button);

        // Toolbar
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set click listeners
        interestsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyInterests();
            }
        });

        // Initialize Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child(DatabaseManager.USERS);

        // Fetch user info
        fetchUserInfo();
    }

    // Fetch user info
    private void fetchUserInfo() {
        if(user != null) {
            ref = ref.child(user.getUid());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        // Fetch info
                        String email = dataSnapshot.child(DatabaseManager.EMAIL).getValue().toString();
                        String name = dataSnapshot.child(DatabaseManager.NAME).getValue().toString();
                        int location = Integer.parseInt(dataSnapshot.child(DatabaseManager.LOCATION).getValue().toString());
                        String photoUri = "";
                        if(dataSnapshot.child(DatabaseManager.USER_PHOTO).exists()) {
                            photoUri = dataSnapshot.child(DatabaseManager.USER_PHOTO).getValue().toString();
                        }
                        // Display info
                        displayUserInfo(email, name, location, photoUri);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });
        }
    }

    // Display user info
    private void displayUserInfo(String email, String name, int location, String photoUri) {
        emailTextView.setText(email);
        nameEditText.setText(name);
        // TODO change switch
        if(location == DatabaseManager.LOCATION_OFF) {

        }
        else {

        }
        if(photoUri.isEmpty()) {
            // TODO display photo
        }
    }

    // Go to my interests
    private void goToMyInterests() {
        Intent intent = new Intent(this, MyInterestsActivity.class);
        startActivity(intent);
    }

    // Save changes
    private void saveChanges() {

    }

    // Menu methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_profile_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_profile_save:
                saveChanges();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
