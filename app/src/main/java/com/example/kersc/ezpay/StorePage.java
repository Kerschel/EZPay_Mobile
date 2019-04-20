package com.example.kersc.ezpay;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.kersc.ezpay.Classes.item;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StorePage extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> merchIDS = new ArrayList<String>();
    Toolbar toolbar;
    ListView listView;
    Adapter adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_page);
        listView = findViewById(R.id.listId);
//        toolbar = findViewById(R.id.storename);
//        toolbar.setTitle(getResources().getString(R.string.app_name));

//        getMerchants();



        adapt = new Adapter();
        listView.setAdapter(adapt);
        listView.setOnItemClickListener(this );
        adapt.notifyDataSetChanged();
        getMerchants();


    }


    public void getMerchants(){

        OkHttpClient client = new OkHttpClient();
        String url = "https://us-central1-ezpay-c9127.cloudfunctions.net/getMerchants";

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
                    StorePage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("HEREq", myResponse);

                            try {
                                JSONObject Jobject = new JSONObject(myResponse);
                                Iterator<String> keys = Jobject.keys();
                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    if (Jobject.get(key) instanceof JSONObject) {
                                        // do something with jsonObject here
                                        JSONObject merch = new JSONObject(Jobject.get(key).toString());
                                        String merchant = merch.get("id").toString();
                                        String merchantname = merch.get("name").toString();
                                        String email = merch.get("email").toString();
                                        name.add(merchantname);
                                        merchIDS.add(merchant);
                                        Log.i("hereq", merchantname);
                                        adapt.notifyDataSetChanged();
//                     itemList.add(new item(image,itemPurchased.get("category").toString(), itemPurchased.get("price").toString(), itemPurchased.get("title").toString(),itemPurchased.get("id").toString(),itemPurchased.get("merchantId").toString(),itemPurchased.get("location").toString()));
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }});

                }
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,BuyFromStore.class);
        intent.putExtra("merchantId",merchIDS.get(position));

        startActivity(intent);
        Toast.makeText(this,name.get(position),Toast.LENGTH_LONG).show();
    }


    class Adapter extends BaseAdapter{


        @Override
        public int getCount() {
            return name.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.storelayoutlist,null);
            ImageView image = convertView.findViewById(R.id.storeImg);
            TextView storename = convertView.findViewById(R.id.storeTxt);

            storename.setText(name.get(position));
            return convertView;
        }
    }
}
