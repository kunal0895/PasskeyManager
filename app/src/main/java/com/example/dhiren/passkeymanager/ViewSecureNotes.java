package com.example.dhiren.passkeymanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ViewSecureNotes extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private String FIREBASE_URL="https://passkey-manager.firebaseio.com/Users/";
    private Firebase FireBaseRef;
    private ListView listView;
    private ArrayList<String> mSiteName = new ArrayList<String>();
    private HashMap keymap;
    String j, userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_secure_notes);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        j =(String) b.get("val1");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(j);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
        // String userid = user.getUid().toString();
        FIREBASE_URL += userid;
        FIREBASE_URL += "/Secure Notes/"+j;

        try
        {
            FireBaseRef = new Firebase(FIREBASE_URL);
            listView = (ListView)findViewById(R.id.listview);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mSiteName);
            listView.setAdapter(arrayAdapter);

            FireBaseRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    keymap = new HashMap();
                    keymap.put("key", dataSnapshot.getKey());
                    System.out.println(keymap);

                    Set keySet = keymap.keySet();
                    Iterator it = keySet.iterator();
                    while(it.hasNext())
                    {
                        String key = (String) it.next();
                        String value = (String) keymap.get(key);
                        mSiteName.add(value);
                    }

                    arrayAdapter.notifyDataSetChanged();
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

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String value = ((TextView) view).getText().toString();
                    if (j.equals("Bank Account"))
                    {
                        Intent intent = new Intent(ViewSecureNotes.this, BankAccountDetails.class);
                        intent.putExtra("val2",value);
                        startActivity(intent);
                    }
                    else if (j.equals("Credit Card"))
                    {
                        Intent intent = new Intent(ViewSecureNotes.this, CardDetails.class);
                        intent.putExtra("val2",value);
                        startActivity(intent);
                    }
                    else if (j.equals("Email Account"))
                    {
                        Intent intent = new Intent(ViewSecureNotes.this, EmailAccountDetails.class);
                        intent.putExtra("val2",value);
                        startActivity(intent);
                    }
                    else if (j.equals("Wifi Password"))
                    {
                        Intent intent = new Intent(ViewSecureNotes.this, WifiPasswordDetails.class);
                        intent.putExtra("val2",value);
                        startActivity(intent);
                    }
                }
            });
        }
        catch (Exception es)
        {
            Toast.makeText(getApplicationContext(), es.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
