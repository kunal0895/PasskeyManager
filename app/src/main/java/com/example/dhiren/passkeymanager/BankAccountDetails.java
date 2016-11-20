package com.example.dhiren.passkeymanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BankAccountDetails extends AppCompatActivity {

    private Button button;
    private EditText bn,etcn,etci,etan,etap,etic,etbb,etbt;
    private ImageView imdelete,imedit,imdone,iimage;
    private FirebaseAuth firebaseAuth;
    private String FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
    private Firebase FireBaseRef,FireBaseRef1;
    private ArrayList<String> mSiteName = new ArrayList<String>();
    String j, userid;
    Map<String,String> SiteUrl = new HashMap<String, String>();
    Map<String,String> Detail=new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account_details);
        Firebase.setAndroidContext(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bank Account Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SiteUrl.put("AXIS Bank","https://www.axisbank.com/");
        SiteUrl.put("Bank Of Baroda","http://www.bankofbaroda.co.in");
        SiteUrl.put("Canara Bank","https://netbanking.canarabank.in");
        SiteUrl.put("Central Bank Of India","https://www.centralbank.net.in");
        SiteUrl.put("Dena Bank","https://www.denabank.co.in");
        SiteUrl.put("HDFC Bank","https://netbanking.hdfcbank.com/netbanking");
        SiteUrl.put("ICICI Bank","https://www.icicibank.com/Personal-Banking/insta-banking/internet-banking");
        SiteUrl.put("IndusInd Bank","https://www.indusind.com");
        SiteUrl.put("Oriental Bank Of Commerce","https://www.obcindia.co.in/obcnew/site/internet_banking.aspx");
        SiteUrl.put("Punjab National Bank","https://www.netpnb.com");
        SiteUrl.put("State Bank Of India","https://retail.onlinesbi.com/retail/login.htm");
        SiteUrl.put("Union Bank","https://www.unionbankonline.co.in");
        SiteUrl.put("Yes Bank","https://www.yesbank.in/digital-banking/online-banking/netbanking-services");

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        j =(String) b.get("val2");

        bn = (EditText)findViewById(R.id.etbaname);
        etcn = (EditText)findViewById(R.id.ecustname);
        etci = (EditText)findViewById(R.id.ecustid);
        etan = (EditText)findViewById(R.id.eaccnum);
        etap = (EditText)findViewById(R.id.eaccpassword);
        etbb = (EditText)findViewById(R.id.ebankbranch);
        etbt = (EditText)findViewById(R.id.ebranchtel);
        etic = (EditText)findViewById(R.id.ebranchifsc);
        imdelete = (ImageView) findViewById(R.id.toolbar_delete);
        imedit = (ImageView) findViewById(R.id.toolbar_edit);
        imdone = (ImageView) findViewById(R.id.toolbar_done);
        iimage = (ImageView)findViewById(R.id.imageView3);
        button = (Button)findViewById(R.id.launch_button);

        imedit.setVisibility(View.VISIBLE);

        String lower = j.toLowerCase();
        String lower1 = lower.replaceAll("\\s+","");
        iimage.setImageResource(getResources().getIdentifier(lower1,"drawable",getPackageName()));

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null)
        {
            userid = Login_Page_New.getTheId();
        }
        else
        {
            userid = user.getUid().toString();
        }

        FIREBASE_URL += userid;
        FIREBASE_URL += "/Secure Notes/Bank Account/";
        FIREBASE_URL += j;
        FireBaseRef = new Firebase(FIREBASE_URL);

        FireBaseRef.addChildEventListener(new ChildEventListener() {
            int i=0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String str) {
                String value = dataSnapshot.getValue(String.class);
                mSiteName.add(value);
                if (i==6)
                {
                    String an = mSiteName.get(0);
                    String ap = mSiteName.get(1);
                    String bb = mSiteName.get(2);
                    String bt = mSiteName.get(3);
                    String ci = mSiteName.get(4);
                    String cn = mSiteName.get(5);
                    String ic = mSiteName.get(6);

                    int shift = -3;
                    String s = "";
                    int len = ap.length();
                    for (int x = 0; x < len; x++) {
                        char c = (char) (ap.charAt(x) + shift);
                        s += c;
                    }

                    String newpass = s;

                    bn.setText(j);
                    etcn.setText(cn);
                    etci.setText(ci);
                    etan.setText(an);
                    etap.setText(newpass);
                    etbb.setText(bb);
                    etbt.setText(bt);
                    etic.setText(ic);
                }
                i++;
                //arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        imdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
                FIREBASE_URL += userid;
                FIREBASE_URL += "/Secure Notes/Bank Account";
                FireBaseRef1 = new Firebase(FIREBASE_URL);


                new AlertDialog.Builder(BankAccountDetails.this)
                        .setTitle("Delete Details")
                        .setMessage("Are you sure you want to delete the details?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String e = bn.getText().toString();
                                FireBaseRef1.child(e).removeValue();
                                String success = "Details successfully deleted!";
                                Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
                                Intent intent1 = new Intent(BankAccountDetails.this,Main_Page.class);
                                startActivity(intent1);
                                finish();

                            }
                        })
                        .setNegativeButton("No",null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        imedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imedit.setVisibility(View.GONE);
                imdone.setVisibility(View.VISIBLE);
                etap.setEnabled(true);
            }
        });

        imdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
                FireBaseRef1 = new Firebase(FIREBASE_URL);

                if(etap.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Please make sure that no field is blank!",Toast.LENGTH_LONG).show();
                }
                else {

                    new AlertDialog.Builder(BankAccountDetails.this)
                            .setTitle("Update Password")
                            .setMessage("Are you sure you want to update the password?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    //Implement encryption
                                    String spassword = etap.getText().toString();
                                    //String suname = wname.getText().toString();
                                    String e = bn.getText().toString();
                                    int shift = 3;
                                    String s = "";
                                    int len = spassword.length();
                                    for (int x = 0; x < len; x++) {

                                        char c = (char) (spassword.charAt(x) + shift);
                                        /*if (c > 'z' || (c > 'Z' && c < 'd'))
                                        {
                                            c -= 26;
                                        }
                                        */
                                        s += c;
                                    }
                                    String newspassword = s;

                                    Detail.put("Customer Name",etcn.getText().toString());
                                    Detail.put("Customer Id",etci.getText().toString());
                                    Detail.put("Account Number",etan.getText().toString());
                                    Detail.put("Account Password",newspassword);
                                    Detail.put("Bank Branch",etbb.getText().toString());
                                    Detail.put("Branch Tel",etbt.getText().toString());
                                    Detail.put("IFSC Code",etic.getText().toString());
                                    String success = "Password successfully updated!";

                                    FireBaseRef1.child(userid).child("Secure Notes").child("Bank Account").child(e).setValue(Detail);

                                    imdone.setVisibility(View.GONE);
                                    imedit.setVisibility(View.VISIBLE);
                                    etap.setEnabled(false);
                                    Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();

                                }
                            })
                            .setNegativeButton("No",null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rdintent = new Intent("android.intent.action.VIEW", Uri.parse(SiteUrl.get(j).toString()));
                startActivity(rdintent);
            }
        });
    }
}