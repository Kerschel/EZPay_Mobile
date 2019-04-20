package com.example.kersc.ezpay;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kersc.ezpay.Classes.User;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    AutoCompleteTextView email,password,fullname;
    Button register;
    public static FirebaseAuth mAuth;
    ImageView ivLogLogo;
    Animation rotater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        fullname = findViewById(R.id.atvUsernameReg);
        email = findViewById(R.id.atvEmailReg);
        password =findViewById(R.id.atvPasswordReg);
        register = (Button) findViewById(R.id.btnSignUp);
        ivLogLogo = findViewById(R.id.ivLogLogo);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String address = email.getText().toString().trim();
                final String pass = password.getText().toString().trim();
                final String name = fullname.getText().toString().trim();
// Write a message to the database

                rotater = AnimationUtils.loadAnimation(Register.this, R.anim.rotate);
                rotater.setRepeatCount(Animation.INFINITE);

                rotater.setFillAfter(true);
                ivLogLogo.startAnimation(rotater);

                Register(address,pass,name);
            }
        });

    }



    public void Register(final String email, final String password, String name){
        OkHttpClient client = new OkHttpClient();
        String url = "https://us-central1-ezpay-c9127.cloudfunctions.net/createNewCustomer?email="+email+"&password="+password+"&name="+name;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
//                    final String myResponse = response.body().string();
                    Register.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Register.this, "Please wait while we sign you in", Toast.LENGTH_LONG).show();

                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(Register.this, HomeScreen.class);
                                        startActivity(intent);
                                    } else {
                                        ivLogLogo.setImageDrawable(null);

                                        Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }
                    });
                }
            }
        });
    }
}
