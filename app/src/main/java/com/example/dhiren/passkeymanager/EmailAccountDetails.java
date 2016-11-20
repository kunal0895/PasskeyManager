package com.example.dhiren.passkeymanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class EmailAccountDetails extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private String FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
    private Firebase FireBaseRef,FireBaseRef1;
    private EditText sitename,siteurl,siteuname,sitepass;
    private ImageView imdelete,imedit,imdone,iimage;
    private Button lbutton;
    String userid;
    Map<String,String> Details=new HashMap<String,String>();
    Map<String,String> SiteUrl = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_account_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Email Account Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SiteUrl.put("Gmail","https://mail.google.com/mail");
        SiteUrl.put("Yahoo","https://login.yahoo.com");
        SiteUrl.put("Outlook","https://login.live.com");
        SiteUrl.put("Zoho","https://www.zoho.com/mail");

        sitename = (EditText) findViewById(R.id.etsname);
        siteurl = (EditText)findViewById(R.id.eturlname);
        siteuname = (EditText)findViewById(R.id.etusername);
        sitepass = (EditText)findViewById(R.id.etupass);
        imdelete = (ImageView) findViewById(R.id.toolbar_delete);
        imedit = (ImageView) findViewById(R.id.toolbar_edit);
        imdone = (ImageView) findViewById(R.id.toolbar_done);
        iimage = (ImageView)findViewById(R.id.imageView3);
        lbutton = (Button)findViewById(R.id.launch_button);

        imedit.setVisibility(View.VISIBLE);

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
        FIREBASE_URL += "/Secure Notes/Email Account/";

        try {
            Intent iin= getIntent();
            Bundle b = iin.getExtras();

            String j =(String) b.get("val2");
            sitename.setText(j);

            siteurl.setText(SiteUrl.get(j));
            String lower = j.toLowerCase();
            iimage.setImageResource(getResources().getIdentifier(lower,"drawable",getPackageName()));
            FIREBASE_URL +=j;
            FIREBASE_URL += "/";
            //Toast.makeText(getApplicationContext(),FIREBASE_URL,Toast.LENGTH_LONG).show();

            FireBaseRef = new Firebase(FIREBASE_URL);
            FireBaseRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    /*
                    Map map = dataSnapshot.getValue(Map.class);
                    String pass = (String) map.get("Password");
                    String uname = (String) map.get("Username");

                    siteuname.setText(uname);
                    sitepass.setText(pass);*/

                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                        String uname = (String) messageSnapshot.getValue();
                        siteuname.setText(uname);

                        break;

                    }
                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                        String pass = (String) messageSnapshot.getValue();

                        //Implement decryption

                        int shift = -3;
                        String s = "";
                        int len = pass.length();
                        for (int x = 0; x < len; x++) {
                            char c = (char) (pass.charAt(x) + shift);
                            /*if (c < 'A' || (c < 'a' && c > 'W'))
                            {
                                c += 26;
                            }*/
                            s += c;
                        }

                        String newpass = s;
                        sitepass.setText(newpass);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }

        imdelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
                FIREBASE_URL += userid;
                FIREBASE_URL += "/Secure Notes/Email Account";
                FireBaseRef1 = new Firebase(FIREBASE_URL);


                new AlertDialog.Builder(EmailAccountDetails.this)
                        .setTitle("Delete Details")
                        .setMessage("Are you sure you want to delete the details?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String e = sitename.getText().toString();
                                FireBaseRef1.child(e).removeValue();
                                String success = "Details successfully deleted!";
                                Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
                                Intent intent1 = new Intent(EmailAccountDetails.this,Main_Page.class);
                                startActivity(intent1);
                                finish();

                            }
                        })
                        .setNegativeButton("No",null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });

        imedit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                imedit.setVisibility(View.GONE);
                imdone.setVisibility(View.VISIBLE);
                sitepass.setEnabled(true);
            }
        });

        imdone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
                FireBaseRef1 = new Firebase(FIREBASE_URL);

                if(siteuname.length()==0 || sitepass.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Please make sure that no field is blank!",Toast.LENGTH_LONG).show();
                }
                else {

                    new AlertDialog.Builder(EmailAccountDetails.this)
                            .setTitle("Update Details")
                            .setMessage("Are you sure you want to update the details?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    //Implement encryption
                                    String spassword = sitepass.getText().toString();
                                    String suname = siteuname.getText().toString();
                                    String e = sitename.getText().toString();
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

                                    Details.put("Email id",suname);
                                    Details.put("Password",newspassword);
                                    String success = "Details successfully updated!";
                                    FireBaseRef1.child(userid).child("Secure Notes").child("Email Account").child(e).setValue(Details);

                                    imdone.setVisibility(View.GONE);
                                    imedit.setVisibility(View.VISIBLE);
                                    sitepass.setEnabled(false);

                                    Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();

                                }
                            })
                            .setNegativeButton("No",null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        lbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rdintent = new Intent("android.intent.action.VIEW", Uri.parse(siteurl.getText().toString()));
                startActivity(rdintent);
            }
        });
    }
}
