package com.chandana.smartmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
//import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class list extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Item> itemlist;
    public static int amount;
    String myResult;

    public TextView tvamount,barcode;
    BackgroundWorker backgroundWorker= new BackgroundWorker(this);
    public static final String SHARED_PREF= "sharedPreferences";
    public static final String TAKE_LIST="takeList";
    public static final String AMOUNT_NUM="totalAmount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        tvamount=(TextView)findViewById(R.id.amount);
        barcode=(TextView)findViewById(R.id.bar);

        Intent intent = getIntent();
        String text = intent.getStringExtra(BackgroundWorker.EXTRA_NAME);
        String ba = intent.getStringExtra(BackgroundWorker.EXTRA_BAR);
        int co = intent.getIntExtra(BackgroundWorker.EXTRA_COST, 0);
        int qu = 1;

        loadData();

        buildRecyclerView();

       if(text!=null) {
           int found=0;
            Item item = new Item(text, ba, co, qu);
            for(Item i:itemlist){
                if(i.getName().equals(text)){
                    found=1;
                    break;
                }
            }
            if(found==1)
           {
               Toast.makeText(getApplicationContext(), "You have already ordered the Item, Please check!!..", Toast.LENGTH_SHORT).show();
           }
            else if (found==0) {
                itemlist.add(item);
                mAdapter.notifyItemInserted(0);
                amount=amount+item.getCost()*item.getQuantity();
                tvamount.setText(amount+"");
            }

        }

        saveData();
    }
    public void buildRecyclerView()
    {
        mRecyclerView = findViewById(R.id.recylerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ItemAdapter(itemlist);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
       /*    public void onItemClick(int position) {
               itemlist.get(position);
           }
*/
            @Override
            public void onDeleteClick(int position) {

                itemlist.remove(position);
          /*      int index=0;
                for(Item i:itemlist){
                    if(i.getName().equals(Name)){
                        int intcos=Integer.parseInt(cos);
                        int intqu=Integer.parseInt(qu);
                        Item itemdel = new Item(Name,barco,intcos,intqu);
                        itemlist.remove(itemdel);
                        break;
                    }
                    index=index+1;
                }*/
                mAdapter.notifyItemRemoved(position);

            }
            public void onChangeAmountInc(int am)
            {
                int a=Integer.parseInt(tvamount.getText().toString());
                amount=amount+am;
                tvamount.setText(amount+"");
            }
            public void onChangeAmountDec(int am)
            {
                int a=Integer.parseInt(tvamount.getText().toString());
                amount=amount-am;
                tvamount.setText(amount+"");
            }
        });

    }

    public void saveData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(itemlist);
        editor.putString(TAKE_LIST,json);
        editor.putString(AMOUNT_NUM,tvamount.getText().toString());
        editor.apply();
    }

    private void loadData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(TAKE_LIST,null);
        String storeAmount=sharedPreferences.getString(AMOUNT_NUM,"");
        Type type = new TypeToken<ArrayList<Item>>() {}.getType();
        itemlist = gson.fromJson(json,type);

        if(itemlist == null){
            itemlist = new ArrayList<>();

        }
        if(storeAmount.equals(""))
        {
            amount=0;
        }
        else
        {
            amount=Integer.parseInt(storeAmount);
        }
        tvamount.setText(storeAmount+"");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.barscan,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.scan)
        {
            Intent intent = new Intent(this,Shop.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


 /*   public void removeData(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        Intent intent =new Intent(this,PayLogout.class);
        startActivity(intent);

        //finish();
    }
*/



    public void bartesting(View view) {
        myResult=barcode.getText().toString();
        String type = "scan";
        backgroundWorker.execute(type, myResult);
    }

    public void Payment(View view) {

        if(amount==0)
        {
           Toast.makeText(getBaseContext(),"Buy something",Toast.LENGTH_SHORT);
        }
        else {
            generateCheckSum();
        }
    }

    private void generateCheckSum() {

        //getting the tax amount first.
        String txnAmount = tvamount.getText().toString().trim();

        //creating a retrofit object.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //creating the retrofit api service
        Api apiService = retrofit.create(Api.class);

        //creating paytm object
        //containing all the values required
        final Paytm paytm = new Paytm(
                Constants.M_ID,
                Constants.CHANNEL_ID,
                txnAmount,
                Constants.WEBSITE,
                Constants.CALLBACK_URL,
                Constants.INDUSTRY_TYPE_ID
        );

        //creating a call object from the apiService
        Call<Checksum> call = apiService.getChecksum(
                paytm.getmId(),
                paytm.getOrderId(),
                paytm.getCustId(),
                paytm.getChannelId(),
                paytm.getTxnAmount(),
                paytm.getWebsite(),
                paytm.getCallBackUrl(),
                paytm.getIndustryTypeId()
        );

        //making the call to generate checksum
        call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, Response<Checksum> response) {

                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                initializePaytmPayment(response.body().getChecksumHash(), paytm);
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {

            }
        });
    }


    private void initializePaytmPayment(String checksumHash, Paytm paytm) {

        //getting paytm service
        PaytmPGService Service = PaytmPGService.getStagingService();

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constants.M_ID);
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder(paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(this, true, true, this);

    }

    //all these overriden method is to detect the payment result accordingly
    @Override
    public void onTransactionResponse(Bundle bundle) {

        Toast.makeText(this, bundle.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(this, "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(this, s + bundle.toString(), Toast.LENGTH_LONG).show();
    }

}
