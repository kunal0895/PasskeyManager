package com.example.dhiren.passkeymanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.os.Handler;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

public class SiteDetails extends AppCompatActivity {

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
        setContentView(R.layout.activity_site_details);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SiteUrl.put("Amazon","https://www.amazon.in");
        SiteUrl.put("Airtel","https://www.airtel.in");
        SiteUrl.put("Abof","https://www.abof.com");
        SiteUrl.put("Bewakoof","https://www.bewakoof.com");
        SiteUrl.put("Bigbasket","https://www.bigbasket.com");
        SiteUrl.put("Cleartrip","https://www.cleartrip.com");
        SiteUrl.put("Dominos","https://www.dominos.co.in");
        SiteUrl.put("Ebay","https://www.ebay.in");
        SiteUrl.put("EzoneOnline","https://www.ezoneonline.in");
        SiteUrl.put("Faasos","https://www.faasos.com");
        SiteUrl.put("Flipkart","https://www.flipkart.com");
        SiteUrl.put("Goibibo","https://www.goibibo.com");
        SiteUrl.put("Grofers","https://www.grofers.com");
        SiteUrl.put("HomeShop18","https://www.homeshop18.com");
        SiteUrl.put("Infibeam","https://www.infibeam.com");
        SiteUrl.put("Jabong","https://www.jabong.com");
        SiteUrl.put("Koovs","https://www.koovs.com");
        SiteUrl.put("Kfc","https://www.kfc.co.in");
        SiteUrl.put("Limeroad","https://www.limeroad.com");
        SiteUrl.put("Lenskart","https://www.lenskart.com");
        SiteUrl.put("Mobikwik","https://www.mobikwik.com");
        SiteUrl.put("MakeMyTrip","https://www.makemytrip.com");
        SiteUrl.put("Myntra","https://www.myntra.com");
        SiteUrl.put("Netmeds","https://www.netmeds.com");
        SiteUrl.put("OyoRooms","https://www.oyorooms.com");
        SiteUrl.put("Paytm","https://www.paytm.com");
        SiteUrl.put("Pepperfry","https://www.pepperfry.com");
        SiteUrl.put("PizzaHut","https://www.pizzahut.co.in");
        SiteUrl.put("Redbus","https://www.redbus.in");
        SiteUrl.put("Rediff","https://www.rediff.com");
        SiteUrl.put("Snapdeal","https://www.snapdeal.com");
        SiteUrl.put("Shopclues","https://www.shopclues.com");
        SiteUrl.put("TataCliq","https://www.tatacliq.com");
        SiteUrl.put("TataSky","https://www.tatasky.com");
        SiteUrl.put("Voonik","https://www.voonik.com");
        SiteUrl.put("Xiaomi","https://www.mi.com/in/");
        SiteUrl.put("Yatra","https://www.yatra.com");
        SiteUrl.put("Yepme","https://www.yepme.com");


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
        FIREBASE_URL += "/SiteDetails/";

        try {
            Intent iin= getIntent();
            Bundle b = iin.getExtras();

            String j =(String) b.get("val");
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
                        break;
                    }
                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                        String uname = (String) messageSnapshot.getValue();
                        siteuname.setText(uname);
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
                FIREBASE_URL += "/SiteDetails";
                FireBaseRef1 = new Firebase(FIREBASE_URL);


                new AlertDialog.Builder(SiteDetails.this)
                        .setTitle("Delete Details")
                        .setMessage("Are you sure you want to delete the details?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String e = sitename.getText().toString();
                                FireBaseRef1.child(e).removeValue();
                                String success = "Details successfully deleted!";
                                Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
                                Intent intent1 = new Intent(SiteDetails.this,Main_Page.class);
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
                siteuname.setEnabled(true);
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

                    new AlertDialog.Builder(SiteDetails.this)
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

                                    Details.put("Username",suname);
                                    Details.put("Password",newspassword);
                                    String success = "Details successfully updated!";
                                    FireBaseRef1.child(userid).child("SiteDetails").child(e).setValue(Details);

                                    imdone.setVisibility(View.GONE);
                                    imedit.setVisibility(View.VISIBLE);
                                    siteuname.setEnabled(false);
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
