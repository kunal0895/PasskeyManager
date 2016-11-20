package com.example.dhiren.passkeymanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class CardDetails extends AppCompatActivity {

    private EditText bn, cardname, cardnumber, cvv, cardpass, cardenddate;
    private ImageView imdelete,imedit,imdone,iimage;
    private FirebaseAuth firebaseAuth;
    private String FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
    private Firebase FireBaseRef,FireBaseRef1;
    private ArrayList<String> mSiteName = new ArrayList<String>();
    String j, userid;
    Map<String,String> Detail=new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        Firebase.setAndroidContext(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Card Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        j =(String) b.get("val2");

        bn = (EditText)findViewById(R.id.etbaname);
        cardname = (EditText)findViewById(R.id.ecreditcardnameoncard);
        cardnumber = (EditText)findViewById(R.id.enumber);
        cardpass = (EditText)findViewById(R.id.epassword);
        cvv=(EditText)findViewById(R.id.ecvv);
        cardenddate = (EditText) findViewById(R.id.eenddate);
        imdelete = (ImageView) findViewById(R.id.toolbar_delete);
        imedit = (ImageView) findViewById(R.id.toolbar_edit);
        imdone = (ImageView) findViewById(R.id.toolbar_done);
        iimage = (ImageView)findViewById(R.id.imageView3);

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
        //final String userid = user.getUid().toString();
        FIREBASE_URL += userid;
        FIREBASE_URL += "/Secure Notes/Credit Card/";
        FIREBASE_URL += j;
        FireBaseRef = new Firebase(FIREBASE_URL);

        FireBaseRef.addChildEventListener(new ChildEventListener() {
            int i=0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String str) {
                String value = dataSnapshot.getValue(String.class);
                mSiteName.add(value);
                if (i==4)
                {
                    String cv = mSiteName.get(0);
                    String cd = mSiteName.get(1);
                    String cn = mSiteName.get(2);
                    String nc = mSiteName.get(3);
                    String p = mSiteName.get(4);

                    int shift = -3;
                    String s = "";
                    int len = p.length();
                    for (int x = 0; x < len; x++) {
                        char c = (char) (p.charAt(x) + shift);
                        s += c;
                    }

                    String newpass = s;

                    bn.setText(j);
                    cvv.setText(cv);
                    cardenddate.setText(cd);
                    cardnumber.setText(cn);
                    cardname.setText(nc);
                    cardpass.setText(newpass);
                }
                i++;
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
                FIREBASE_URL += "/Secure Notes/Credit Card";
                FireBaseRef1 = new Firebase(FIREBASE_URL);


                new AlertDialog.Builder(CardDetails.this)
                        .setTitle("Delete Details")
                        .setMessage("Are you sure you want to delete the card?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String e = bn.getText().toString();
                                FireBaseRef1.child(e).removeValue();
                                String success = "Card successfully deleted!";
                                Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
                                Intent intent1 = new Intent(CardDetails.this,Main_Page.class);
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
                cardpass.setEnabled(true);
            }
        });

        imdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
                FireBaseRef1 = new Firebase(FIREBASE_URL);

                if(cardpass.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Please make sure that no field is blank!",Toast.LENGTH_LONG).show();
                }
                else {

                    new AlertDialog.Builder(CardDetails.this)
                            .setTitle("Update Password")
                            .setMessage("Are you sure you want to update the password?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    //Implement encryption
                                    String spassword = cardpass.getText().toString();
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

                                    Detail.put("Name on Card", cardname.getText().toString());
                                    Detail.put("Card Number", cardnumber.getText().toString());
                                    Detail.put("Password", newspassword);
                                    Detail.put("Card End Date", cardenddate.getText().toString());
                                    Detail.put("CVV", cvv.getText().toString());
                                    String success = "Password successfully updated!";

                                    FireBaseRef1.child(userid).child("Secure Notes").child("Credit Card").child(e).setValue(Detail);

                                    imdone.setVisibility(View.GONE);
                                    imedit.setVisibility(View.VISIBLE);
                                    cardpass.setEnabled(false);
                                    Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();

                                }
                            })
                            .setNegativeButton("No",null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
    }
}
