package com.example.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText name, email, password, moblie_no, confirmPassword, age;
    Button mRegisterbtn;
    TextView mLoginPageBack;
    TextView admin;
    FirebaseAuth mAuth;
    DatabaseReference mdatabase;
    String Name, Email, Password, Mobile,Age, ConfirmPassword;
    ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );

        name = (EditText) findViewById( R.id.inputUsername);
        moblie_no = (EditText) findViewById( R.id.inputPhone);
        email = (EditText) findViewById( R.id.inputEmail);
        password = (EditText) findViewById( R.id.inputPassword);
        confirmPassword = (EditText)findViewById(R.id.inputConformPassword);
        age = (EditText)findViewById(R.id.inputAge);
        mRegisterbtn = (Button) findViewById( R.id.btnRegister);
        mLoginPageBack = (TextView) findViewById( R.id.alreadyHaveAccount);
        // for authentication using FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();
        mRegisterbtn.setOnClickListener( this );
        mLoginPageBack.setOnClickListener( this );
        mDialog = new ProgressDialog( this );
        mdatabase = FirebaseDatabase.getInstance().getReference().child( "Users" );
    }

    public void onClick(View v) {
        if (v == mRegisterbtn) {
            UserRegister();
        } else if (v == mLoginPageBack) {
            Intent intent = new Intent( RegisterActivity.this, LoginActivity.class );
            startActivity( intent );
        }
    }

    private void UserRegister() {
        Name = name.getText().toString().trim();
        Email = email.getText().toString().trim();
        Password = password.getText().toString().trim();
        Mobile = moblie_no.getText().toString().trim();
        Age = age.getText().toString();
        ConfirmPassword = confirmPassword.getText().toString();

        if (TextUtils.isEmpty( Name )) {
            Toast.makeText( RegisterActivity.this, "Enter Name", Toast.LENGTH_SHORT ).show();
            return;
        } else if (TextUtils.isEmpty( Email )) {
            Toast.makeText( RegisterActivity.this, "Enter Email", Toast.LENGTH_SHORT ).show();
            return;
        } else if (TextUtils.isEmpty( Password )) {
            Toast.makeText( RegisterActivity.this, "Enter Password", Toast.LENGTH_SHORT ).show();
            return;
        } else if (Password.length() < 6) {
            Toast.makeText( RegisterActivity.this, "Password must be greater then 6 digit", Toast.LENGTH_SHORT ).show();
            return;
        } else if(ConfirmPassword.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Confirm Password", Toast.LENGTH_SHORT).show();
        } else if (Mobile.length() < 10) {
            Toast.makeText( RegisterActivity.this, "Mobile No. must be of 10 digit", Toast.LENGTH_SHORT ).show();
            return;
        } else if (TextUtils.isEmpty( Mobile )) {
            Toast.makeText( RegisterActivity.this, "Enter Mobile No. ", Toast.LENGTH_SHORT ).show();
            return;
        } else if(Age.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Enter Age", Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.setMessage( "Creating User please wait..." );
        mDialog.setCanceledOnTouchOutside( false );
        mDialog.show();
        mAuth.createUserWithEmailAndPassword( Email, Password ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendEmailVerification();
                    mDialog.dismiss();
                    OnAuth( task.getResult().getUser() );
                    mAuth.signOut();
                } else {
                    Toast.makeText( RegisterActivity.this, "error on creating user", Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }

    //Email verification code using FirebaseUser object and using isSucccessful()function.
    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener( new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText( RegisterActivity.this, "Check your Email for verification", Toast.LENGTH_SHORT ).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            } );
        }
    }
    private void OnAuth(FirebaseUser user) {
        createAnewUser( user.getUid() );
    }

    private void createAnewUser(String uid) {
        User user = BuildNewuser();
        mdatabase.child( uid ).setValue( user );
    }


    private User BuildNewuser() {
        return new User(
                getDisplayName(),
                getUserEmail(),
                getMobile(),
                getAge()
        );
    }

    public String getDisplayName() {
        return Name;
    }

    public String getUserEmail() {
        return Email;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getAge(){return Age;}
}