package com.example.kersc.ezpay;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.kersc.ezpay.Classes.item;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class History extends AppCompatActivity {
    List<item> itemList;
    public item i;
    RecyclerViewHistory myAdapter;
    private FirebaseAuth mAuth;
    JSONObject myResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        itemList = new ArrayList<>();
        RecyclerView displayview = (RecyclerView) findViewById(R.id.recycler);
        itemList.add(new item("Pizza", "icecream", "50.10", "Tast great"));

        myAdapter = new RecyclerViewHistory(this, itemList);
        displayview.setLayoutManager(new GridLayoutManager(this, 1));
        displayview.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
        getAllTransactions("1");

    }


    public void getAllTransactions(String customerID){
            OkHttpClient client = new OkHttpClient();
            String url = "https://us-central1-ezpay-c9127.cloudfunctions.net/getTransactionsCustomerId?customerId="+Register.mAuth.getCurrentUser().getUid();
            Log.i("Loggedyser",Register.mAuth.getCurrentUser().getUid());
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
                        final String myResponse = response.body().string();
                        History.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject Jobject = new JSONObject(myResponse);
                                    Iterator<String> keys = Jobject.keys();
                                    while(keys.hasNext()) {
                                        String key = keys.next();
                                        if (Jobject.get(key) instanceof JSONObject) {
                                            // do something with jsonObject here
                                            JSONObject trans = new JSONObject(Jobject.get(key).toString());
                                            String customerId =  trans.get("customerId").toString();
                                            Log.i("Loggedyser",trans.get("invoiceNumber").toString());

                                            String invoiceNumber =  trans.get("invoiceNumber").toString();
                                            JSONObject itemPurchased  =  new JSONObject(trans.get("itemPurchased").toString());
                                            String whenbought =  trans.get("timestamp").toString();
                                            String url = itemPurchased.get("url").toString();

                                            Products.RetrieveFeedTask r = new Products.RetrieveFeedTask();
                                            Drawable image =r.execute(url).get();

                                            itemList.add(new item(image,"Food", itemPurchased.get("Price").toString(), itemPurchased.get("Description").toString(),"","",""));
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }

//

                                myAdapter.notifyDataSetChanged();


                            }
                        });
                    }
                }
            });
        }
    }




