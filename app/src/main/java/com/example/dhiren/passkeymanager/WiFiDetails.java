package com.example.dhiren.passkeymanager;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class WiFiDetails extends AppCompatActivity {

    private EditText wname,wpassword;
    private Button button;
    private FirebaseAuth firebaseAuth;
    String uname, mail, uid;

    Map<String,String> Details=new HashMap<String,String>();
    private static final String FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
    private Firebase FireBaseRef = new Firebase(FIREBASE_URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_details);
        Firebase.setAndroidContext(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Wi-Fi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            uname = user.getDisplayName();
            mail = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            uid = user.getUid();
        }
        else
        {
            uname = Login_Page_New.getTheName();
            mail = Login_Page_New.getTheMail();
            uid = Login_Page_New.getTheId();
        }

        wname = (EditText)findViewById(R.id.ename);
        wpassword = (EditText)findViewById(R.id.epass);
        button = (Button)findViewById(R.id.buttonw);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String suname = wname.getText().toString();
                final String spassword = wpassword.getText().toString();

                try {
                    if(wname.length()==0 || wpassword.length()==0)
                    {
                        Toast.makeText(getApplicationContext(),"Please make sure that no field is blank!",Toast.LENGTH_LONG).show();
                    }
                    else {

                        new AlertDialog.Builder(WiFiDetails.this)
                                .setTitle("Add Wi-Fi Password")
                                .setMessage("Are you sure with the wi-fi name and password?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        //Implement encryption
                                        int shift = 3;
                                        String s = "";
                                        int len = spassword.length();
                                        for (int x = 0; x < len; x++) {
                                            char c = (char) (spassword.charAt(x) + shift);
                                            s += c;
                                        }
                                        String newspassword = s;

                                        Details.put("Password",newspassword);
                                        String success = "Details entered successfully!";
                                        FireBaseRef.child(uid).child("Secure Notes").child("Wifi Password").child(suname).setValue(Details);
                                        Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();

                                    }
                                })
                                .setNegativeButton("No",null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                        //public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                        //}


                    }
                }

                catch(Exception ex)
                {
                    System.out.println(ex);
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
