package com.example.dhiren.passkeymanager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class FormFill extends AppCompatActivity {

    static final int DATE_DIALOG_ID = 0;
    Spinner spinner,spinner1,spinner2;
    Button add;
    EditText etuname,etbdate,etpname,etfname,etmname,etlname;
    private DatePickerDialog  dpdialog;
    private SimpleDateFormat dateFormatter;
    private int mYear, mMonth, mDay;
    private FirebaseAuth firebaseAuth;
    String uname, mail, uid;


    Map<String,String> Details=new HashMap<String,String>();
    private static final String FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
    private Firebase FireBaseRef = new Firebase(FIREBASE_URL);

    String[] langs = {"English", " ", "English(United Kingdom)", " ", "French", " ", "French(Canada)", " ", "Swedish"};
    String[] titles = {"Please Select", " ", "Mr", " ", "Mrs", " ", "Ms", " ", "Dr"};
    String[] gender = {"Please Select", " ", "Male", " ", "Female"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_fill);

        Firebase.setAndroidContext(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Form Fill");
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

        Calendar c=Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth=c.get(Calendar.MONTH);
        mDay=c.get(Calendar.DAY_OF_MONTH);

        add = (Button)findViewById(R.id.bfinal);
        etuname=(EditText)findViewById(R.id.etuname);
        etpname=(EditText)findViewById(R.id.etpname);
        etbdate=(EditText)findViewById(R.id.etbdate);
        etfname=(EditText)findViewById(R.id.etfname);
        etmname=(EditText)findViewById(R.id.etmname);
        etlname=(EditText)findViewById(R.id.etlname);
        etbdate.setInputType(InputType.TYPE_NULL);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FormFill.this,android.R.layout.simple_dropdown_item_1line,langs);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(FormFill.this, android.R.layout.simple_dropdown_item_1line,titles);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(FormFill.this, android.R.layout.simple_dropdown_item_1line,gender);

        AutoCompleteTextView textViewlang = (AutoCompleteTextView)findViewById(R.id.atvlang);
        textViewlang.setAdapter(adapter);

        AutoCompleteTextView textViewtitle = (AutoCompleteTextView)findViewById(R.id.atvtitle);
        textViewtitle.setAdapter(adapter1);

        AutoCompleteTextView textViewgender = (AutoCompleteTextView)findViewById(R.id.atvgender);
        textViewgender.setAdapter(adapter2);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy",Locale.US);
        etbdate.setText(sdf.format(c.getTime()));

        etbdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Uname = etuname.getText().toString();
                final String Pname = etpname.getText().toString();
                final String Bdate = etbdate.getText().toString();
                final String Fname = etfname.getText().toString();
                final String Mname = etmname.getText().toString();
                final String Lname = etlname.getText().toString();

                if(etuname.length()==0 || etpname.length()==0 || etbdate.length()==0 || etfname.length()==0 || etmname.length()==0 || etlname.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"Please make sure that no field is blank!",Toast.LENGTH_LONG).show();
                }
                else {
                    new AlertDialog.Builder(FormFill.this)
                            .setTitle("Add Profile Details")
                            .setMessage("Are you sure with the entered details?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Details.put("Username", Uname);
                                    Details.put("ProfileName", Pname);
                                    Details.put("BirthDate", Bdate);
                                    Details.put("FirstName", Fname);
                                    Details.put("MiddleName", Mname);
                                    Details.put("LastName", Lname);


                                    String success = "Profile details entered successfully!";
                                    FireBaseRef.child(uid).child("UserDetails").setValue(Details);
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

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }

        return null;

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            etbdate.setText(new StringBuilder().append(mDay).append("/").append(mMonth+1).append("/").append(mYear));

        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}