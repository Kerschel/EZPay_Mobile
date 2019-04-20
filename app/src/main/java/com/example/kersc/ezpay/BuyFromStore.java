package com.example.kersc.ezpay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kersc.ezpay.Classes.item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BuyFromStore extends AppCompatActivity {
    List<item> itemList;
    StoreAdapter myAdapter;
    private  String userID;
    public item i;
    public int removeitem=0;
    String merchantId;
    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)

            .clientId("AZEqnKfo2CTPnlclHsFR2byFAtra_h6rTJmcXr7HgdLhhdiNj0DKJiquRx8HUgu08n16BaUj9DuLGZXz");
    ImageView ivLogLogo;
    Animation rotater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_from_store);
        Intent intent = getIntent();
        merchantId = intent.getExtras().getString("merchantId");
        itemList = new ArrayList<>();
        rotater = AnimationUtils.loadAnimation(BuyFromStore.this, R.anim.rotate);
//        rotater.setRepeatCount(Animation.INFINITE);
        rotater.setFillAfter(true);
//        rotater.setRepeatCount(Animation.INFINITE);

        ivLogLogo = findViewById(R.id.ivLogLogo);
        ivLogLogo.startAnimation(rotater);
        RecyclerView displayview = (RecyclerView) findViewById(R.id.recycler);
        myAdapter = new StoreAdapter(this, itemList);
        final Activity activity = this;

        getProducts();

        displayview.setLayoutManager(new GridLayoutManager(this, 2));

        displayview.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                i= itemList.get(position);
                config = new PayPalConfiguration()

                        // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                        // or live (ENVIRONMENT_PRODUCTION)
                        .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)

                        .clientId("AZEqnKfo2CTPnlclHsFR2byFAtra_h6rTJmcXr7HgdLhhdiNj0DKJiquRx8HUgu08n16BaUj9DuLGZXz");
                paypalPayment(i.getDescription(),i.getPrice(),position);

            }

            @Override
            public void onItemClickRemove(int position) {
//                i= itemList.get(position);
//                itemList.remove(position);
//                myAdapter.notifyDataSetChanged();
            }
        });


    }



    public void getProducts(){
        OkHttpClient client = new OkHttpClient();
        String url = "https://us-central1-ezpay-c9127.cloudfunctions.net/getMerchantInvoices?id="+merchantId;

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

                    BuyFromStore.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.i("yyy",myResponse);
                                JSONObject Jobject = new JSONObject(myResponse);
                                Iterator<String> keys = Jobject.keys();
                                while(keys.hasNext()) {
                                    String key = keys.next();
                                    if (Jobject.get(key) instanceof JSONObject) {
                                        JSONObject store = new JSONObject(Jobject.get(key).toString());
                                        String merchant =  store.get("merchantId").toString();
                                        String code =  store.get("id").toString();
                                        String title =  store.get("title").toString();
                                        String description =  store.get("description").toString();
                                        String price =  store.get("price").toString();
                                        String url = store.get("imgUrl").toString();
                                        Log.i("URL",url);
                                        Products.RetrieveFeedTask r = new Products.RetrieveFeedTask();
                                        Drawable image =r.execute(url).get();
                                        itemList.add(new item(description,code,"",price,title,image));

                                    }
                                    }


//                                Drawable drawable = LoadImageFromWebOperations(url);

                                myAdapter.notifyDataSetChanged();
                                ivLogLogo.setImageDrawable(null);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("error",e.toString());

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
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

                }
            }
        });
    }

    public void paypalPayment(String description, String price,int remove){
        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.

        removeitem = remove;
        PayPalPayment payment = new PayPalPayment(new BigDecimal(price.replace("$","")), "USD", description,
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 30);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
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
//                    itemList.remove(removeitem);
                    myAdapter.notifyDataSetChanged();
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



    static class RetrieveFeedTask extends AsyncTask<String, Void, Drawable> {
        public Drawable image;
        private Exception exception;

        protected Drawable doInBackground(String... urls) {
            try {
                InputStream is = (InputStream) new URL(urls[0]).getContent();
                Drawable d = Drawable.createFromStream(is, "src name");

                return d;

            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }
        @Override
        protected void onPostExecute(Drawable result) {
            //do stuff
            image  = result;
//                myMethod(myValue);
        }

    }


}