package com.example.kersc.ezpay;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "";
    Button loginAccount,register;
    AutoCompleteTextView userEmail, userPW;
ProgressBar progress ;
ImageView ivLogLogo;
    Animation rotater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginAccount = (Button)findViewById(R.id.btnSignIn);
        register =(Button)findViewById(R.id.btnRegister);
        userEmail = findViewById(R.id.atvEmailLog);
        userPW = findViewById(R.id.atvPasswordLog);
        Register.mAuth = FirebaseAuth.getInstance();
        progress = findViewById(R.id.spinkit);
        Wave wave = new Wave();
        progress.setIndeterminateDrawable(wave);
        progress.setVisibility(View.INVISIBLE);
        ivLogLogo = findViewById(R.id.ivLogLogo);
//Login Account
        loginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = userEmail.getText().toString();
                String pass = userPW.getText().toString();
                progress.setVisibility(View.VISIBLE);

                rotater = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.rotate);
//                rotater.setRepeatCount(Animation.INFINITE);

                rotater.setFillAfter(true);
                ivLogLogo.startAnimation(rotater);


                Register.mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = Register.mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, HomeScreen.class);
                            startActivity(intent);
                        } else {
                            progress.setVisibility(View.INVISIBLE);

                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });// the click



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);
            }
        });// the click

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        Toast.makeText(LoginActivity.this, "You are logged in.", Toast.LENGTH_SHORT).show();
//        System.out.print(currentUser);
//        Intent intent = new Intent(LoginActivity.this, Products.class);
//        startActivity(intent);
    }


    public void findUserID(){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//
//        myRef.getDatabase().getReference().child("Users/Customers").orderByChild("email")
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
    }

}
