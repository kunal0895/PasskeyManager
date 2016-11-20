package com.example.dhiren.passkeymanager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.DatePickerDialog;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SitePage extends AppCompatActivity {

    private EditText sitename,siteurl,siteusername,sitepassword;
    private Button button;
    ImageView imv;
    int i=0;
    private FirebaseAuth firebaseAuth;
    String uname, mail, uid;

    Map<String,String> Details=new HashMap<String,String>();
    private static final String FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
    private Firebase FireBaseRef = new Firebase(FIREBASE_URL);


    String[] sites = {"Amazon", " ", "Airtel", " ", "Abof", " ", "Bewakoof", " ", "Bigbasket", " ", "Cleartrip", " ", "Dominos", " ", "Ebay", " ", "EzoneOnline", " ", "Faasos", " ", "Flipkart", " ", "Goibibo", " ", "Grofers", " ", "HomeShop18", " ", "Infibeam", " ", "Jabong", " ", "Koovs", " ", "Kfc", " ", "Limeroad", " ", "Lenskart", " ", "Mobikwik", " ", "MakeMyTrip", " ", "Myntra", " ", "Netmeds", " ", "OyoRooms", " ", "Paytm", " ", "Pepperfry", " ", "PizzaHut", " ", "Redbus", " ", "Rediff", " ", "Snapdeal", " ", "Shopclues", " ", "TataCliq", " ", "TataSky", " ", "Voonik", " ", "Xiaomi", " ", "Yatra", " ", "Yepme"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_page);
        Firebase.setAndroidContext(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Site");
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

        //sitename = (EditText)findViewById(R.id.etsname);
        siteurl = (EditText)findViewById(R.id.eturlname);
        siteusername = (EditText)findViewById(R.id.etusername);
        sitepassword = (EditText)findViewById(R.id.etupass);
        button = (Button)findViewById(R.id.button4);
        imv = (ImageView)findViewById(R.id.imageView1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SitePage.this,android.R.layout.simple_dropdown_item_1line,sites);

        AutoCompleteTextView textViewsites = (AutoCompleteTextView)findViewById(R.id.etsname);
        textViewsites.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AutoCompleteTextView sname = (AutoCompleteTextView)findViewById(R.id.etsname);
                final String e = sname.getText().toString();
                final String suname = siteusername.getText().toString();
                final String spassword = sitepassword.getText().toString();

                //Details.put("SiteName",e);
                try {
                    if(siteusername.length()==0 || sitepassword.length()==0 || sname.length()==0)
                    {
                        Toast.makeText(getApplicationContext(),"Please make sure that no field is blank!",Toast.LENGTH_LONG).show();
                    }
                    else {

                        new AlertDialog.Builder(SitePage.this)
                                .setTitle("Add Site")
                                .setMessage("Are you sure with your username and password?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        //Implement encryption
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
                                        String success = "Details entered successfully!";
                                        FireBaseRef.child(uid).child("SiteDetails").child(e).setValue(Details);
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

        imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==0)
                {
                    sitepassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    sitepassword.setSelection(sitepassword.length());
                    i=1;
                }
                else
                {
                    sitepassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    sitepassword.setSelection(sitepassword.length());
                    i=0;
                }
            }
        });

    }
}
