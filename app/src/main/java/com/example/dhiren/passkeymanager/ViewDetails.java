package com.example.dhiren.passkeymanager;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ViewDetails extends AppCompatActivity {

    private EditText etuname,etbdate,etpname,etfname,etmname,etlname;
    private FirebaseAuth firebaseAuth;

    Map<String,String> Detail=new HashMap<String,String>();
    ImageView imdelete,imedit,imdone,iimage;
    private ArrayList<String> mSiteName = new ArrayList<String>();
    String j;
    private String FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
    private Firebase FireBaseRef,FireBaseRef1;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        Firebase.setAndroidContext(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        etuname=(EditText)findViewById(R.id.etuname);
        etpname=(EditText)findViewById(R.id.etpname);
        etbdate=(EditText)findViewById(R.id.etbdate);
        etfname=(EditText)findViewById(R.id.etfname);
        etmname=(EditText)findViewById(R.id.etmname);
        etlname=(EditText)findViewById(R.id.etlname);
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
        FIREBASE_URL += "/UserDetails";

        FireBaseRef = new Firebase(FIREBASE_URL);

        FireBaseRef.addChildEventListener(new ChildEventListener() {
            int i=0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                mSiteName.add(value);
                if (i==5)
                {
                    String bd = mSiteName.get(0);
                    String fn = mSiteName.get(1);
                    String ln = mSiteName.get(2);
                    String mn = mSiteName.get(3);
                    String pn = mSiteName.get(4);
                    String un = mSiteName.get(5);

                    etbdate.setText(bd);
                    etfname.setText(fn);
                    etlname.setText(ln);
                    etmname.setText(mn);
                    etpname.setText(pn);
                    etuname.setText(un);
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

                FIREBASE_URL="https://passkey-manager.firebaseio.com/Users";
                FireBaseRef1 = new Firebase(FIREBASE_URL);

                new AlertDialog.Builder(ViewDetails.this)
                        .setTitle("Delete Details")
                        .setMessage("Are you sure you want to delete the profile details?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String e = "UserDetails";
                                FireBaseRef1.child(userid).child(e).removeValue();
                                String success = "Details successfully deleted!";
                                Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
                                Intent intent1 = new Intent(ViewDetails.this,Main_Page.class);
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
                etfname.setEnabled(true);
                etlname.setEnabled(true);
                etmname.setEnabled(true);
                etpname.setEnabled(true);
                etuname.setEnabled(true);
            }
        });

        imdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
                FireBaseRef1 = new Firebase(FIREBASE_URL);

                if(etfname.length()==0 || etlname.length()==0 || etmname.length()==0 || etpname.length()==0 || etuname.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Please make sure that no field is blank!",Toast.LENGTH_LONG).show();
                }
                else {

                    new AlertDialog.Builder(ViewDetails.this)
                            .setTitle("Update Details")
                            .setMessage("Are you sure you want to update the profile details?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Detail.put("FirstName",etfname.getText().toString());
                                    Detail.put("MiddleName",etmname.getText().toString());
                                    Detail.put("LastName",etlname.getText().toString());
                                    Detail.put("ProfileName",etpname.getText().toString());
                                    Detail.put("Username",etuname.getText().toString());
                                    Detail.put("BirthDate",etbdate.getText().toString());
                                    String success = "Details successfully updated!";

                                    FireBaseRef1.child(userid).child("UserDetails").setValue(Detail);

                                    imdone.setVisibility(View.GONE);
                                    imedit.setVisibility(View.VISIBLE);
                                    etfname.setEnabled(false);
                                    etlname.setEnabled(false);
                                    etmname.setEnabled(false);
                                    etpname.setEnabled(false);
                                    etuname.setEnabled(false);
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
