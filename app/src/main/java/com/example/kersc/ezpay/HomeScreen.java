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
    Button scan_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        scan_btn = (Button) findViewById(R.id.scanner);
        final Activity activity = this;

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan Product");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.initiateScan();
            }
        });

        imgBanner = findViewById(R.id.imgBanner);

        int sliders[] = {
                R.drawable.female,R.drawable.male
        };

//        for (int slide:sliders){
//            bannerFlipper(slide);
//        }


    }

//    public void bannerFlipper(int image){
//        ImageView imageview = new ImageView(this);
//        imageview.setImageResource(image);
//        imgBanner.addView(imageview);
//        imgBanner.setFlipInterval(5000);
//        imgBanner.setAutoStart(true);
//        imgBanner.setInAnimation(this,android.R.anim.fade_in);
//        imgBanner.setOutAnimation(this,android.R.anim.fade_out);
//    }


}
