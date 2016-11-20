package com.example.dhiren.passkeymanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login_Page extends AppCompatActivity implements View.OnClickListener {

    Button login;
    EditText uname,pass;
    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__page);

        login = (Button)findViewById(R.id.button);
        tv1 = (TextView)findViewById(R.id.textView4);
        uname = (EditText)findViewById(R.id.editText);
        pass = (EditText)findViewById(R.id.editText2);

        login.setOnClickListener(this);
        tv1.setOnClickListener(this);
    }

    /*public void onBackPressed() {
        Intent intent1 = new Intent(Intent.ACTION_MAIN);
        intent1.addCategory(Intent.CATEGORY_HOME);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
    }
    */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button:

                //boolean didItWork = true;

                try {
                    String name = uname.getText().toString();
                    String pass1 = pass.getText().toString();

                    Info db = new Info(getApplicationContext());

                    if (db.loginCheck(name, pass1)) {
                        Intent in = new Intent(getApplicationContext(), Main_Page.class);
                        startActivity(in);
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Username and Password not match", Toast.LENGTH_SHORT).show();
                    }

                }
                catch(Exception e)
                {
                    //Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(getApplicationContext(), Main_Page.class);
                    startActivity(in);
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                }
                /*finally {
                    if (didItWork) {


                    }
                }*/
                break;

            case  R.id.textView4:
                Intent i = new Intent(Login_Page.this,RegisterPage.class);
                startActivity(i);

                break;
        }
    }
}
