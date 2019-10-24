package com.chandana.smartmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PayLogout extends AppCompatActivity {
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_logout);


    }

    public void logout(View view) {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Login Status");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setMessage("Are You Sure?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Ok",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(PayLogout.this,MainActivity.class);
                startActivity(intent);
            }
        });
        alertDialog.show();

    }
}
