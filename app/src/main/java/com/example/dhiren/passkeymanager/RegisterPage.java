package com.example.dhiren.passkeymanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPage extends AppCompatActivity {

    Button register,viewb,resetpass;
    EditText uname,pass;
    ProgressBar progressBar;
    FirebaseAuth auth;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        auth = FirebaseAuth.getInstance();
        register = (Button)findViewById(R.id.button);
        viewb = (Button)findViewById(R.id.button2);
        uname = (EditText)findViewById(R.id.editText);
        pass = (EditText)findViewById(R.id.editText2);
        tv = (TextView)findViewById(R.id.textView4);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        resetpass = (Button) findViewById(R.id.btn_reset_password);

       /* register.setOnClickListener(this);
        viewb.setOnClickListener(this);
        tv.setOnClickListener(this);
        */

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterPage.this, ResetpasswordActivity.class));
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = uname.getText().toString().trim();
                String password = pass.getText().toString().trim();

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(RegisterPage.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterPage.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(RegisterPage.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    /*public void onBackPressed() {
        Intent intent1 = new Intent(Intent.ACTION_MAIN);
        intent1.addCategory(Intent.CATEGORY_HOME);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
    }
    */

   /* @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button:

                boolean didItWork = true;

                try {
                    String name = uname.getText().toString();
                    String pass1 = pass.getText().toString();

                    Info entry = new Info(RegisterPage.this);

                    entry.open();
                    entry.createEntry(name, pass1);
                    entry.close();

                } catch (Exception e) {
                    didItWork = false;
                    Toast.makeText(RegisterPage.this, "Please enter details in correct format.", Toast.LENGTH_SHORT).show();
                } finally {
                    if (didItWork) {
                        Toast.makeText(RegisterPage.this, "You are successfully registered.", Toast.LENGTH_SHORT).show();

                    }
                }
                break;

            case  R.id.button2:
                Intent i = new Intent(RegisterPage.this,SqlView.class);
                startActivity(i);

                break;

            case  R.id.textView4:
                Intent i1 = new Intent(RegisterPage.this,Login_Page.class);
                startActivity(i1);

                break;
        }

    } */
}
