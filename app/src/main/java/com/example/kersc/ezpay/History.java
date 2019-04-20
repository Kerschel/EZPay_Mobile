package com.example.kersc.ezpay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kersc.ezpay.Classes.item;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
    List<item> itemList=new ArrayList<>();
    public item i;
    RecyclerViewHistory myAdapter;
    private FirebaseAuth mAuth;
    JSONObject myResponse;
    TextView total;
    ProgressBar progress ;

    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)

            .clientId("AZEqnKfo2CTPnlclHsFR2byFAtra_h6rTJmcXr7HgdLhhdiNj0DKJiquRx8HUgu08n16BaUj9DuLGZXz");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        itemList = new ArrayList<>();
        progress = findViewById(R.id.spinkit);
        Wave wave = new Wave();
        progress.setIndeterminateDrawable(wave);
        progress.setVisibility(View.VISIBLE);

        RecyclerView displayview = (RecyclerView) findViewById(R.id.recycler);
//        itemList.add(new item("Pizza", "icecream", "50.10", "Tast great"));

        myAdapter = new RecyclerViewHistory(this, itemList);
        displayview.setLayoutManager(new GridLayoutManager(this, 1));
        displayview.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
        getAllTransactions(Register.mAuth.getUid());

        myAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                i= itemList.get(position);
                config = new PayPalConfiguration()

                        // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                        // or live (ENVIRONMENT_PRODUCTION)
                        .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)

                        .clientId("AZEqnKfo2CTPnlclHsFR2byFAtra_h6rTJmcXr7HgdLhhdiNj0DKJiquRx8HUgu08n16BaUj9DuLGZXz");
                paypalPayment(i.getDescription(),i.getPrice());

            }

            @Override
            public void onItemClickRemove(int position) {
                i= itemList.get(position);
                itemList.remove(position);
                myAdapter.notifyDataSetChanged();
            }
        });

    }

    public void getTotal(){
        total = findViewById(R.id.total);
        double sum = 0;
        for(int i=0;i<itemList.size();i++){
            sum += Double.parseDouble(itemList.get(i).getPrice().replace("$",""));
        }
        total.setText("Spent: $"+sum);
    }


    public void getAllTransactions(String customerID){
            OkHttpClient client = new OkHttpClient();
            String url = "https://us-central1-ezpay-c9127.cloudfunctions.net/getTransactionsCustomerId?customerId="+customerID;
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

                                            String invoiceNumber =  trans.get("invoiceNumber").toString();
                                            JSONObject itemPurchased  =  new JSONObject(trans.get("invoice").toString());
//                                            String whenbought =  trans.get("transactionDate").toString();
                                            String url = itemPurchased.get("imgUrl").toString();

                                            Products.RetrieveFeedTask r = new Products.RetrieveFeedTask();
                                            Drawable image =r.execute(url).get();

                                            itemList.add(new item(image,itemPurchased.get("category").toString(), itemPurchased.get("price").toString(), itemPurchased.get("title").toString(),itemPurchased.get("id").toString(),itemPurchased.get("merchantId").toString(),itemPurchased.get("location").toString()));
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
                                getTotal();
                                myAdapter.notifyDataSetChanged();
                                    progress.setVisibility(View.INVISIBLE);

                            }
                        });
                    }
                }
            });
        }


    public void paypalPayment(String description, String price){
        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.

        PayPalPayment payment = new PayPalPayment(new BigDecimal(price.replace("$","")), "USD", description,
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 30);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("CODE", String.valueOf(requestCode));
        if(requestCode ==30){ // paypal
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    Log.i("paymentExample", confirm.toJSONObject().toString());
                    Log.d("paypal payment",confirm.toJSONObject().toString());
                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.
                    String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();

//                    Transaction t = new Transaction(user,i.getCategory(),i.getPrice(),i.getBarcode(),i.getSeller_paypalID());
                    logTransaction(i.getBarcode(),Register.mAuth.getCurrentUser().getUid(),"1");
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            }
            else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }

        }


    }

    public void logTransaction(String barcode,String customer,String status){

        OkHttpClient client = new OkHttpClient();
        String url = "https://us-central1-ezpay-c9127.cloudfunctions.net/addTransaction?invoiceId="+barcode+"&customerId="+customer+"&status="+status+"&customerName="+Register.mAuth.getCurrentUser().getDisplayName();

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
                    getTotal();
                }
            }
        });
    }


    }




