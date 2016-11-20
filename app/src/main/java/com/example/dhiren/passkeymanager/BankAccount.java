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

public class BankAccount extends AppCompatActivity {

    private Button button;
    private EditText etcn,etci,etan,etap,etic,etbb,etbt;
    String uname, mail, uid;

    private FirebaseAuth firebaseAuth;

    Map<String,String> Detail=new HashMap<String,String>();
    private static final String FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
    private Firebase FireBaseRef = new Firebase(FIREBASE_URL);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account);String[] banks = {"AXIS Bank", " ", "Bank Of Baroda", " ", "Central Bank Of India", " ", "Canara Bank", " ", "Dena Bank", " ", "HDFC Bank", " ", "ICICI Bank", " ", "IndusInd Bank", " ", "Oriental Bank Of Commerce", " ", "Punjab National Bank", " ", "State Bank Of India", " ", "Union Bank", " ", "Yes Bank"};
        Firebase.setAndroidContext(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bank Account Details");
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

        AutoCompleteTextView bn = (AutoCompleteTextView)findViewById(R.id.etbaname);
        etcn = (EditText)findViewById(R.id.ecustname);
        etci = (EditText)findViewById(R.id.ecustid);
        etan = (EditText)findViewById(R.id.eaccnum);
        etap = (EditText)findViewById(R.id.eaccpassword);
        etbb = (EditText)findViewById(R.id.ebankbranch);
        etbt = (EditText)findViewById(R.id.ebranchtel);
        etic = (EditText)findViewById(R.id.ebranchifsc);
        button = (Button)findViewById(R.id.button4);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BankAccount.this,android.R.layout.simple_dropdown_item_1line,banks);

        //bn = (AutoCompleteTextView)findViewById(R.id.etbaname);
        bn.setAdapter(adapter);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AutoCompleteTextView bn = (AutoCompleteTextView)findViewById(R.id.etbaname);
                final String e = bn.getText().toString();
                final String cn = etcn.getText().toString();
                final String ci = etci.getText().toString();
                final String an = etan.getText().toString();
                final String ap = etap.getText().toString();
                final String bb = etbb.getText().toString();
                final String bt = etbt.getText().toString();
                final String ic = etic.getText().toString();

                try {
                    if(bn.length()==0 || etcn.length()==0 || etci.length()==0 || etan.length()==0 || etap.length()==0 || etbb.length()==0 || etbt.length()==0 || etic.length()==0)
                    {
                        Toast.makeText(getApplicationContext(),"Please make sure that no field is blank!",Toast.LENGTH_LONG).show();
                    }
                    else {

                        new AlertDialog.Builder(BankAccount.this)
                                .setTitle("Add Bank Information")
                                .setMessage("Do you want to proceed with the details entered?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        //Implement encryption
                                        int shift = 3;
                                        String s = "";
                                        int len = ap.length();
                                        for (int x = 0; x < len; x++) {
                                            char c = (char) (ap.charAt(x) + shift);
                                            s += c;
                                        }
                                        String newspassword = s;

                                        Detail.put("Customer Name",cn);
                                        Detail.put("Customer Id",ci);
                                        Detail.put("Account Number",an);
                                        Detail.put("Account Password",newspassword);
                                        Detail.put("Bank Branch",bb);
                                        Detail.put("Branch Tel",bt);
                                        Detail.put("IFSC Code",ic);
                                        String success = "Details entered successfully!";
                                        FireBaseRef.child(uid).child("Secure Notes").child("Bank Account").child(e).setValue(Detail);
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
