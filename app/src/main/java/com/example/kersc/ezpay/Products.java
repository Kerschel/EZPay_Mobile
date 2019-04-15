package com.example.kersc.ezpay;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.kersc.ezpay.Classes.item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.paypal.android.sdk.payments.PayPalConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Products extends AppCompatActivity {
    List<item> itemList;
    FloatingActionButton add;
    RecyclerViewAdapter myAdapter;

//    private static PayPalConfiguration config = new PayPalConfiguration()
//            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
//            .clientId("YOUR CLIENT ID");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        add = findViewById(R.id.fab);
        itemList = new ArrayList<>();
        RecyclerView displayview = (RecyclerView) findViewById(R.id.recycler);
        myAdapter = new RecyclerViewAdapter(this, itemList);
        final Activity activity = this;

        add.setOnClickListener(new View.OnClickListener() {
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


        itemList.add(new item("Pizza", "food", "50.10", "Tast great"));
        itemList.add(new item("Donut", "food", "21.10", "Tasted great"));


        displayview.setLayoutManager(new GridLayoutManager(this, 2));

        displayview.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                System.out.println(itemList.get(position).getDescription());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            } else {
                getFirebaseData(result.getContents());
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    public void getFirebaseData(final String barcode) {
        // Read from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference barcodeRef = database.getReference("Barcodes");
        barcodeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds  : dataSnapshot.getChildren())
                {
                    //TODO get the data here
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
//                    String value = dataSnapshot.getValue(String.class);
//                    Log.d("value", "Value is: " + value);
                    System.out.println(barcode);
                    System.out.println(ds.getKey());
                    System.out.println(barcode.trim() == ds.getKey().trim());

                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Log.d("Map", "Kerssc is: " + map.get("Category"));
                        itemList.add(new item("Pizza", map.get("Category").toString().toLowerCase(), map.get("Price").toString(), map.get("Description").toString()));
                        myAdapter.notifyDataSetChanged();

                }

            }
        });

    }

}