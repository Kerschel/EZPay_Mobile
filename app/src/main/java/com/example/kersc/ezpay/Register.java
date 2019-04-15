package com.example.kersc.ezpay;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.kersc.ezpay.Classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    AutoCompleteTextView email,password,fullname;
    Button register;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        fullname = findViewById(R.id.atvUsernameReg);
        email = findViewById(R.id.atvEmailReg);
        password =findViewById(R.id.atvPasswordReg);
        register = (Button) findViewById(R.id.btnSignUp);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String address = email.getText().toString().trim();
                final String pass = password.getText().toString().trim();
                final String name = fullname.getText().toString().trim();
// Write a message to the database

                System.out.println("Data is here" + address);
System.out.println(pass);
System.out.print(address + "address");
                mAuth.createUserWithEmailAndPassword(address, pass)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("User", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    User u= new User(name,address);
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Register.this, "User Successfully created",Toast.LENGTH_SHORT).show();
                                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                                Intent intent = new Intent(Register.this, HomeScreen.class);
                                                startActivity(intent);
                                            }
                                            else{
                                                Toast.makeText(Register.this, "Registration Failed",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    // If sign in fails, display a message to the user.
//                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            }
        });

    }


}
