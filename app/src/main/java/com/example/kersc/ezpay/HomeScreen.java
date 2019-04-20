package com.example.kersc.ezpay;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class HomeScreen extends AppCompatActivity {
    ViewFlipper imgBanner;
    Button shop,history,window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        shop = (Button) findViewById(R.id.shop);
        history = (Button) findViewById(R.id.history);
        window = findViewById(R.id.window);
        final Activity activity = this;

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, Products.class);
                startActivity(intent);
            }
        });

        window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, StorePage.class);
                startActivity(intent);
            }
        });


        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, History.class);
                startActivity(intent);
            }
        });

        imgBanner = findViewById(R.id.imgBanner);

        int sliders[] = {
                R.drawable.menu1,R.drawable.menu2,R.drawable.menu3,R.drawable.menu4,R.drawable.menu5
        };

        for (int slide:sliders){
            bannerFlipper(slide);
        }


    }

    public void bannerFlipper(int image){
        ImageView imageview = new ImageView(this);
        imageview.setImageResource(image);
        imgBanner.addView(imageview);
        imgBanner.setFlipInterval(2000);
        imgBanner.setAutoStart(true);
        imgBanner.setInAnimation(this,android.R.anim.fade_in);
        imgBanner.setOutAnimation(this,android.R.anim.fade_out);
    }


}
