package com.example.dhiren.passkeymanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class WifiPasswordDetails extends AppCompatActivity {

    private ImageView imdelete,imedit,imdone,iimage;
    private FirebaseAuth firebaseAuth;
    private String FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
    private Firebase FireBaseRef,FireBaseRef1;
    private EditText wname,wpass;
    String userid;
    Map<String,String> Details=new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_password_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wifi Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        wname = (EditText)findViewById(R.id.ewifiname);
        wpass = (EditText)findViewById(R.id.ewifipass);
        imdelete = (ImageView) findViewById(R.id.toolbar_delete);
        imedit = (ImageView) findViewById(R.id.toolbar_edit);
        imdone = (ImageView) findViewById(R.id.toolbar_done);
        iimage = (ImageView)findViewById(R.id.imageView3);

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
        FIREBASE_URL += "/Secure Notes/Wifi Password/";

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        String j =(String) b.get("val2");
        wname.setText(j);
        iimage.setImageResource(getResources().getIdentifier("wifipassword","drawable",getPackageName()));

        FIREBASE_URL +=j;
        FIREBASE_URL += "/";
        //Toast.makeText(getApplicationContext(),FIREBASE_URL,Toast.LENGTH_LONG).show();

        FireBaseRef = new Firebase(FIREBASE_URL);
        FireBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                    wpass.setText(newpass);
                }
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
                FIREBASE_URL += "/Secure Notes/Wifi Password";
                FireBaseRef1 = new Firebase(FIREBASE_URL);


                new AlertDialog.Builder(WifiPasswordDetails.this)
                        .setTitle("Delete Details")
                        .setMessage("Are you sure you want to delete the details?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String e = wname.getText().toString();
                                FireBaseRef1.child(e).removeValue();
                                String success = "Details successfully deleted!";
                                Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
                                Intent intent1 = new Intent(WifiPasswordDetails.this,Main_Page.class);
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
                wpass.setEnabled(true);
            }
        });

        imdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
                FireBaseRef1 = new Firebase(FIREBASE_URL);

                if(wpass.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Please make sure that no field is blank!",Toast.LENGTH_LONG).show();
                }
                else {

                    new AlertDialog.Builder(WifiPasswordDetails.this)
                            .setTitle("Update Details")
                            .setMessage("Are you sure you want to update the password?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    //Implement encryption
                                    String spassword = wpass.getText().toString();
                                    //String suname = wname.getText().toString();
                                    String e = wname.getText().toString();
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

                                    //Details.put("Username",suname);
                                    Details.put("Password",newspassword);
                                    String success = "Details successfully updated!";
                                    FireBaseRef1.child(userid).child("Secure Notes").child("Wifi Password").child(e).setValue(Details);

                                    imdone.setVisibility(View.GONE);
                                    imedit.setVisibility(View.VISIBLE);
                                    wname.setEnabled(false);
                                    wpass.setEnabled(false);

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
