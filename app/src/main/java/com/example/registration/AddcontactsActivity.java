package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddcontactsActivity extends AppCompatActivity {

    EditText name,phone;
    Button add;
    FirebaseUser uid;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontacts);

        name=findViewById(R.id.contactname);
        phone=findViewById(R.id.contact);
        add=findViewById(R.id.addbutton);
        uid= FirebaseAuth.getInstance().getCurrentUser();
        String Uid=uid.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(Uid).child("contacts");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String conname=name.getText().toString().trim();
                String conno=phone.getText().toString().trim();
                databaseReference.child(conname).setValue(conno);
            }
        });
    }
}