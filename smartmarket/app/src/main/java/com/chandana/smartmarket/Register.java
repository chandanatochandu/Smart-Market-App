package com.chandana.smartmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Register extends AppCompatActivity {

    public EditText ename,eemail,ephno,eusername,epassword;
    String name,email,phno,username,password;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ename = (EditText) findViewById(R.id.name);
        eemail = (EditText) findViewById(R.id.email);
        ephno = (EditText) findViewById(R.id.phnum);
        eusername = (EditText) findViewById(R.id.username);
        epassword = (EditText) findViewById(R.id.password);
    }

    public void OnReg(View view) {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Register Status");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                alertDialog.dismiss();
            }
        });
        name = ename.getText().toString();
        email = eemail.getText().toString();
        phno= ephno.getText().toString();
        username = eusername.getText().toString();
        password = epassword.getText().toString();


        if(name.length()==0)
        {
            alertDialog.setMessage("Please Enter your name");
            alertDialog.show();
        }
        else if(email.length()==0)
        {
            alertDialog.setMessage("Please Enter your email");
            alertDialog.show();
        }
        else if(phno.length()==0)
        {
            alertDialog.setMessage("Please Enter your phone number");
            alertDialog.show();
        }
        else if(phno.length()<10 || phno.charAt(0)==1 || phno.charAt(0)==2 || phno.charAt(0)==3 ||
                phno.charAt(0)==4 || phno.charAt(0)==5 || phno.charAt(0)==6)
        {
            alertDialog.setMessage("Invalid Phonenumber");
            alertDialog.show();
        }
        else if(username.length()==0)
        {
            alertDialog.setMessage("Please Enter your Username name");
            alertDialog.show();
        }
        else if(password.length()==0)
        {
            alertDialog.setMessage("Please set a password");
            alertDialog.show();
        }
        else{

            String type = "register";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type, name, email, phno, username, password);
        }

    }
}
