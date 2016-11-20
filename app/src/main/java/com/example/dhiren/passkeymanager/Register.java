package com.example.dhiren.passkeymanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Register extends AppCompatActivity {

    //Button register,login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Thread timerThread = new Thread()
        {
            public void run()
            {
                try {
                    sleep(5000);
                }

                catch (InterruptedException e) {
                    e.printStackTrace();
                }

                finally
                {
                    Intent intent = new Intent(Register.this, AfterSplash.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();

      /*  register = (Button)findViewById(R.id.button);
        login = (Button)findViewById(R.id.button2);

        register.setOnClickListener(this);
        login.setOnClickListener(this); */
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

   /* public void onClick(View v)
    {
        switch (v.getId()) {

            case  R.id.button:
                Intent i = new Intent(Register.this,Login_Page.class);
                startActivity(i);

                break;

            case  R.id.button2:
                Intent i1 = new Intent(Register.this,RegisterPage.class);
                startActivity(i1);

                break;
        }
    }
    public void onBackPressed() {
        Intent intent1 = new Intent(Intent.ACTION_MAIN);
        intent1.addCategory(Intent.CATEGORY_HOME);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
    } */
}
