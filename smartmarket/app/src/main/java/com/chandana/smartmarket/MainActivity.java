package com.chandana.smartmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    EditText UsernameEt, PasswordEt;
    Button login,register;
    Intent intent1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UsernameEt = (EditText)findViewById(R.id.uname);
        PasswordEt = (EditText)findViewById(R.id.upassword);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.regist);

    }

    public void OnLogin(View view) {
        String username = UsernameEt.getText().toString();
        String password = PasswordEt.getText().toString();
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, username, password);
    }

    public void openreg(View view) {
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
    }

}
