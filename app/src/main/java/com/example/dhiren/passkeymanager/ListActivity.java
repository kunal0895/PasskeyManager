package com.example.dhiren.passkeymanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListActivity extends AppCompatActivity {

    private ListView listView;

    /*ListView list;
    String[] web = {
            "Generic",
            "Bank Account",
            "Credit Card",
            "Database",
            "Driver's License",
            "Email Account",
            "ABC"
    } ;
    Integer[] imageId = {
        R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
            R.drawable.image7,
    };
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Secure Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Secure Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true); */

        /*  CustomList adapter = new
                CustomList(ListActivity.this, web, imageId);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(ListActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
            }
        });
    }
} */
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        // Defined Array values to show in ListView
        String[] values = new String[] {
                "Bank Account",
                "Credit Card/Debit Card",
                "Email Account",
                "Wi-Fi Password"
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);


                if (itemValue.equals("Bank Account"))
                {
                    Intent i = new Intent(ListActivity.this,BankAccount.class);
                    startActivity(i);
                }
                else if (itemValue.equals("Credit Card/Debit Card"))
                {
                    Intent i = new Intent(ListActivity.this,CreditCard.class);
                    startActivity(i);
                }
                else if (itemValue.equals("Email Account"))
                {
                    Intent i = new Intent(ListActivity.this,EmailAccount.class);
                    startActivity(i);
                }
                else if (itemValue.equals("Wi-Fi Password"))
                {
                    Intent i = new Intent(ListActivity.this,WiFiDetails.class);
                    startActivity(i);
                }
                // Show Alert
                //Toast.makeText(getApplicationContext(),
                //        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                //        .show();

            }

        });

    }
}
