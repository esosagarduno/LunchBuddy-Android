package com.mmrnd.lunchbuddy;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import de.hdodenhof.circleimageview.CircleImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Esteban Sosa
 */

public class MyProfileActivity extends AppCompatActivity {

    static final String IMAGE_INTENT_TYPE = "image/*";
    static final int IMAGE_REQUEST_CODE = 2;

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

    // Variables
    String userName;
    Uri newImageUri;
    boolean isImageChanged = false;

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
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignOutDialog();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
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
            ref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        // Fetch info
                        String email = dataSnapshot.child(DatabaseManager.EMAIL).getValue().toString();
                        String name = dataSnapshot.child(DatabaseManager.NAME).getValue().toString();
                        userName = name;
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
        imageView.setImageResource(R.mipmap.user_icon);
        if(!photoUri.isEmpty()) {
            Glide.with(MyProfileActivity.this).load(photoUri).into(imageView);
        }
    }

    // Go to my interests
    private void goToMyInterests() {
        Intent intent = new Intent(this, MyInterestsActivity.class);
        startActivity(intent);
    }

    // Save changes
    private void saveChanges() {
        // Get new name
        String newName = nameEditText.getText().toString();
        if(newName.isEmpty()) {
            Toast.makeText(this, "Name field can't be empty.", Toast.LENGTH_LONG).show();
            return;
        }
        if(user != null) {
            if(!userName.equals(newName)) {
                ref.child(user.getUid()).child(DatabaseManager.NAME).setValue(newName);
            }
            if(newImageUri != null && isImageChanged) {
                isImageChanged = false;
                saveNewImage();
            }
            Toast.makeText(this, "Changes saved.", Toast.LENGTH_LONG).show();
        }
    }

    // Save new image
    private void saveNewImage() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(DatabaseManager.USER_PHOTO).child(user.getUid());
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), newImageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        final UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyProfileActivity.this, "Error uploading image. Please try again later.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUri = uri.toString();
                        ref.child(user.getUid()).child(DatabaseManager.USER_PHOTO).setValue(downloadUri);
                        Toast.makeText(MyProfileActivity.this, "Changes saved.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // Choose photo
    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(IMAGE_INTENT_TYPE);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    // Sign out alert dialog
    private void showSignOutDialog() {
        //Create alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to sign out?");
        builder.setCancelable(true);
        //Set buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                signOut();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //Create alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Sign out
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MyProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            newImageUri = data.getData();
            imageView.setImageURI(newImageUri);
            isImageChanged = true;
        }
        else if(resultCode != Activity.RESULT_OK && resultCode != Activity.RESULT_CANCELED) {
            Toast.makeText(MyProfileActivity.this, "Error fetching image. Please try again later.", Toast.LENGTH_LONG).show();
        }
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
