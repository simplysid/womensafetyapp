package com.example.registration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class EditProfile extends AppCompatActivity {
    private EditText editName, editAge, editEmail, editPhone;
    private Button Save;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private ImageView editProfilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        editAge = (EditText) findViewById(R.id.etAgeupdate);
        editName = (EditText)findViewById(R.id.etNameupdate);
        editEmail = (EditText)findViewById(R.id.etEmailupdate);
        editPhone = (EditText)findViewById(R.id.etPhoneupdate);
        Save = (Button)findViewById(R.id.btnSave);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snap1: snapshot.getChildren())
                {
                    editName.setText(snapshot.child("displayname").getValue().toString());
                    editAge.setText(snapshot.child("age").getValue().toString());
                    editEmail.setText(snapshot.child("email").getValue().toString());
                    editPhone.setText(snapshot.child("mobile").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(EditProfile.this, error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = editName.getText().toString().trim();
                String age = editAge.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String mobile = editPhone.getText().toString().trim();

                User userProfile = new User(Username, email, mobile, age);

                databaseReference.setValue(userProfile);


                finish();
            }
        });

    }

}