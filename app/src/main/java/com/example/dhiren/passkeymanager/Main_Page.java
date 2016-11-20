package com.example.dhiren.passkeymanager;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

//import com.getbase.floatingactionbutton.FloatingActionsMenu;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.firebase.ui.storage.*;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main_Page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignInActivity";
    private TextView email,loginstatus;
    private FirebaseAuth firebaseAuth;
    private GoogleApiClient mGoogleApiClient;
    String uname, mail;
    String Image = "ProfPic.jpg";
    String img1 = "Image.jpg";
    String path1,path2, uid;
    Button signOut;
    ImageView imgView;
    int c = 0;
    // final NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);
    //final View headerLayout = mNavigationView.inflateHeaderView(R.layout.nav_header_main__page);
    //final ImageView imgView = (ImageView) headerLayout.findViewById(R.id.imageView);

    // creating an instance of Firebase Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    //creating a storage reference. Replace the below URL with your Firebase storage URL.
    StorageReference ref1 = storage.getReferenceFromUrl("gs://passkey-manager.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__page);
        Firebase.setAndroidContext(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        signOut = (Button) findViewById(R.id.action_settings);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fuser != null) {
            // Name, email address, and profile photo Url
            uname = fuser.getDisplayName();
            mail = fuser.getEmail();
            Uri photoUrl = fuser.getPhotoUrl();

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

        loadImage(mail);

        final View actionA = findViewById(R.id.action_a);
        final View actionB = findViewById(R.id.action_b);
        final View actionC = findViewById(R.id.action_c);

        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), FormFill.class);
                startActivity(i);
            }
        });

        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
            }
        });

        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i1 = new Intent(getApplicationContext(), SitePage.class);
                startActivity(i1);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        email = (TextView)header.findViewById(R.id.textView2);
        if(user == null)
        {
            //Intent in = getIntent();
            //String str = in.getStringExtra(LoginGoogle.EXTRA_MESSAGE);
            String str = Login_Page_New.getTheMail();
            email.setText(str);
        }
        else
        {
            String str = user.getEmail().toString();
            email.setText(str);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            firebaseAuth = FirebaseAuth.getInstance();
                            //final FirebaseUser user = firebaseAuth.getCurrentUser();
                            firebaseAuth.signOut();
                            Intent intent=new Intent(Main_Page.this,AfterSplash.class);
                            startActivity(intent);
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main__page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //return true;
            signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.snba || id == R.id.sncc || id == R.id.snea || id == R.id.snwp)
        {
            Intent int0 = new Intent(Main_Page.this, ViewSecureNotes.class);
            int0.putExtra("val1",item.toString());
            startActivity(int0);
        }

        else if (id == R.id.nav_view_details)
        {
            Intent int3 = new Intent(Main_Page.this, ViewDetails.class);
            startActivity(int3);
        }

        else if (id == R.id.nav_view_sites)
        {
            Intent int5 = new Intent(Main_Page.this, ViewSites.class);
            startActivity(int5);
        }

        else if (id == R.id.nav_manage)
        {
            Intent int6 = new Intent(Main_Page.this, MainActivity.class);
            startActivity(int6);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Code to download image

   /* private void loadImage(String mail)
    {
        String m = mail;
        StorageReference mainref = ref1.child(m).child(Image);

        Glide.with(this).using(new FirebaseImageLoader()).load(mainref).into(imgView);
    } */

    public void signOut()
    {
        c = Login_Page_New.getCount();
        System.out.println("Value of C: " + c);
        if(c == 1)
        {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // [START_EXCLUDE]
                            //updateUI(false);
                            Intent intent = new Intent(getApplicationContext(), Login_Page_New.class);
                            startActivity(intent);
                            // [END_EXCLUDE]
                        }
                    });
        }
        else
        {
            System.out.println("Should reach here yes");
            firebaseAuth.signOut();
            System.out.println("Reaches here yes.");
            startActivity(new Intent(Main_Page.this, Login_Page_New.class));
        }
    }

    private void loadImage(String mail)
    {   String pathExt= Environment.getDataDirectory().toString();
        String m=mail;
        StorageReference mainref = ref1.child(m).child(Image);
        String filename="Image.jpg";

        File localFile= null;
        File localFile1 = null;

        localFile = new File("/sdcard/PasskeyManager/ProfileImages/"+m+"/"+filename);
        localFile1 = new File("/sdcard/PasskeyManager/ProfileImages/"+m+"/");
        localFile.mkdirs();
        if(localFile.exists()) {
            localFile.delete();
        }

        String path=localFile.getAbsolutePath();
        path2 = localFile1.getAbsolutePath();
        path1 = path;
        System.out.println(pathExt);
        System.out.println(path);
        System.out.println(path2);


        mainref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                //Toast.makeText(Main_Page.this,"Image downloaded successfully",Toast.LENGTH_SHORT).show();
                loadImageFromStorage(path2);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    private void loadImageFromStorage(String path2)
    {

        try {
            File f=new File(path2, img1);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imgView = (ImageView) findViewById(R.id.imageView);
            imgView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
