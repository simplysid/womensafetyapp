package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class Profile extends AppCompatActivity {
        private TextView profileName, profileAge, profileEmail, profilePhone;
        private Button Edit;
        private FirebaseAuth firebaseAuth;
        private FirebaseDatabase firebaseDatabase;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);
            profileName = (TextView)findViewById(R.id.tvProfilename);
            profileAge = (TextView)findViewById(R.id.tvProfileage);
            profileEmail = (TextView)findViewById(R.id.tvprofileemail);
            profilePhone = (TextView)findViewById(R.id.tvProfilephone);
            Edit = (Button)findViewById(R.id.btnProfileedit);

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance();

            DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot snap1: snapshot.getChildren())
                    {
                        profileName.setText("Name: "+snapshot.child("displayname").getValue());
                        profileAge.setText("Age: "+snapshot.child("age").getValue());
                        profileEmail.setText("Email: "+snapshot.child("email").getValue());
                        profilePhone.setText("Phone: "+snapshot.child("mobile").getValue());
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    Toast.makeText(Profile.this, error.getCode(), Toast.LENGTH_SHORT).show();
                }
            });

            Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Profile.this, EditProfile.class));
                }
            });

        }
}