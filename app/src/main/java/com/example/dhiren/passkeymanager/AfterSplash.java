package com.example.dhiren.passkeymanager;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AfterSplash extends AppCompatActivity implements View.OnClickListener {

    Button register,login,loginGoogle;
    String siteURL = "https://accounts.google.com/SignUp?hl=en-GB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_splash);

        register = (Button)findViewById(R.id.button);
        login = (Button)findViewById(R.id.button1);
        loginGoogle = (Button)findViewById(R.id.button2);

        register.setOnClickListener(this);
        login.setOnClickListener(this);
        loginGoogle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case  R.id.button:
                Intent i = new Intent(AfterSplash.this,RegisterPageNew.class);
                startActivity(i);

                break;

            case  R.id.button1:
                Intent i1 = new Intent(AfterSplash.this,Login_Page_New.class);
                startActivity(i1);

                break;

            case R.id.button2:
                Intent rdintent = new Intent("android.intent.action.VIEW", Uri.parse(siteURL));
                startActivity(rdintent);

                break;
        }
    }
    public void onBackPressed() {
        Intent intent1 = new Intent(Intent.ACTION_MAIN);
        intent1.addCategory(Intent.CATEGORY_HOME);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
    }
}
