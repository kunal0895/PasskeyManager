package com.example.dhiren.passkeymanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreditCard extends AppCompatActivity {

    static final int DATE_DIALOG_ID1 = 1;
    private DatePickerDialog dpdialog;
    private SimpleDateFormat dateFormatter;
    private int endYear, endMonth, endDay;
    EditText cardname, cardnumber, cvv, cardpass, cardenddate;
    Button add;
    String uname, mail, uid;

    private FirebaseAuth firebaseAuth;

    Map<String,String> Details=new HashMap<String,String>();
    private static final String FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
    private Firebase FireBaseRef = new Firebase(FIREBASE_URL);

    String[] banks = {"AXIS Bank", " ", "Bank Of Baroda", " ", "Central Bank Of India", " ", "Canara Bank", " ", "Dena Bank", " ", "HDFC Bank", " ", "ICICI Bank", " ", "Indusind Bank", " ", "Oriental Bank Of Commerce", " ", "Punjab National Bank", " ", "State Bank Of India", " ", "Union Bank", " ", "Yes Bank"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        Firebase.setAndroidContext(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Credit Card Details");
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

        Calendar c1=Calendar.getInstance();
        endYear = c1.get(Calendar.YEAR);
        endMonth=c1.get(Calendar.MONTH);
        endDay=c1.get(Calendar.DAY_OF_MONTH);

        AutoCompleteTextView bn = (AutoCompleteTextView)findViewById(R.id.etbaname);
        add = (Button)findViewById(R.id.bfinal);
        cardname = (EditText)findViewById(R.id.ecreditcardnameoncard);
        cardnumber = (EditText)findViewById(R.id.enumber);
        cardpass = (EditText)findViewById(R.id.epassword);
        cvv=(EditText)findViewById(R.id.ecvv);
        cardenddate = (EditText) findViewById(R.id.eenddate);
        cardenddate.setInputType(InputType.TYPE_NULL);

        SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy", Locale.US);
        cardenddate.setText(sdf.format(c1.getTime()));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreditCard.this,android.R.layout.simple_dropdown_item_1line,banks);

        //bn = (AutoCompleteTextView)findViewById(R.id.etbaname);
        bn.setAdapter(adapter);

        cardenddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID1);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AutoCompleteTextView bn = (AutoCompleteTextView) findViewById(R.id.etbaname);
                final String e = bn.getText().toString();
                final String cardname1 = cardname.getText().toString();
                final String cardnumber1 = cardnumber.getText().toString();
                final String cardcode1 = cardpass.getText().toString();
                final String cardenddate1 = cardenddate.getText().toString();
                final String cardcvv = cvv.getText().toString();

                try
                {
                if (bn.length() == 0 || cardname.length() == 0 || cardnumber.length() == 0 || cardpass.length() == 0 || cardenddate.length() == 0 || cvv.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please make sure that no field is blank!", Toast.LENGTH_LONG).show();
                } else {

                    new AlertDialog.Builder(CreditCard.this)
                            .setTitle("Credit Card Information")
                            .setMessage("Do you want to proceed with the details entered?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    //Implement encryption
                                    int shift = 3;
                                    String s = "";
                                    int len = cardcode1.length();
                                    for (int x = 0; x < len; x++) {
                                        char c = (char) (cardcode1.charAt(x) + shift);
                                        s += c;
                                    }
                                    String newspassword = s;

                                    Details.put("Name on Card", cardname1);
                                    Details.put("Card Number", cardnumber1);
                                    Details.put("Password", newspassword);
                                    Details.put("Card End Date", cardenddate1);
                                    Details.put("CVV", cardcvv);

                                    //Toast.makeText(getApplicationContext(),Username,Toast.LENGTH_LONG).show();
                                    //System.out.println(Username);

                                    String success = "Submitted Successfully!";
                                    //FireBaseRef.child("CreditCardDetails").child("user").push().setValue(Details);
                                    FireBaseRef.child(uid).child("Secure Notes").child("Credit Card").child(e).setValue(Details);
                                    Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();

                                }
                            })
                            .setNegativeButton("No",null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            }
                catch (Exception ex)
                {
                    System.out.println(ex);
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID1:
                return new DatePickerDialog(this,
                        mDateSetListener1,
                        endYear, endMonth, endDay);
        }

        return null;

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            endYear = year;
            endMonth = monthOfYear;
            //endDay = dayOfMonth;
            //cardenddate.setText(new StringBuilder().append(endDay).append("/").append(endMonth + 1).append("/").append(endYear));
            cardenddate.setText(new StringBuilder().append(endMonth + 1).append("/").append(endYear));
        }

    };
}