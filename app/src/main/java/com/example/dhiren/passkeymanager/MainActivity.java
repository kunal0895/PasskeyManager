package com.example.dhiren.passkeymanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Button btnChangePassword, btnSendResetEmail, btnRemoveUser, btnSetProfilePicture, btnRemoveProfilePicture, signOut, resetsimplepassword;

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private static final String FIREBASE_URL="https://passkey-manager.firebaseio.com/";
    private Firebase reference = new Firebase(FIREBASE_URL);

    private Firebase reference1 = new Firebase("https://passkey-manager.firebaseio.com/Users/");

    private GoogleApiClient mGoogleApiClient;

    private EditText currentEmail, oldPassword, newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    String uname, mail, uid;
    String Image = "ProfPic.jpg";
    int c = 0;

    // creating an instance of Firebase Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    //creating a storage reference. Replace the below URL with your Firebase storage URL.
    StorageReference ref1 = storage.getReferenceFromUrl("gs://passkey-manager.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = auth.getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    //startActivity(new Intent(MainActivity.this, Login_Page_New.class));
                    //finish();
                    mail = Login_Page_New.getTheMail();
                    uid = Login_Page_New.getTheId();
                }
                else
                {
                    // Name, email address, and profile photo Url
                    uname = user.getDisplayName();
                    mail = user.getEmail();
                    Uri photoUrl = user.getPhotoUrl();

                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                    // authenticate with your backend server, if you have one. Use
                    // FirebaseUser.getToken() instead.
                    uid = user.getUid();
                }
            }
        };

        //btnChangeEmail = (Button) findViewById(R.id.change_email_button);
        btnChangePassword = (Button) findViewById(R.id.change_password_button);
        btnSendResetEmail = (Button) findViewById(R.id.sending_pass_reset_button);
        resetsimplepassword = (Button) findViewById(R.id.reset_password_button);
        btnRemoveUser = (Button) findViewById(R.id.remove_user_button);
        btnSetProfilePicture = (Button) findViewById(R.id.set_profile_picture);
        btnRemoveProfilePicture = (Button) findViewById(R.id.remove_profile_picture);
        //changeEmail = (Button) findViewById(R.id.changeEmail);
        //changePassword = (Button) findViewById(R.id.changePass);
        //sendEmail = (Button) findViewById(R.id.send);
        //remove = (Button) findViewById(R.id.remove);
        signOut = (Button) findViewById(R.id.sign_out);

        //changePassword.setVisibility(View.GONE);
        //sendEmail.setVisibility(View.GONE);
        //remove.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        btnSetProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), ProfilePicture.class));
            }
        });

        btnRemoveProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteImageFromFirebase(mail);
                deleteImageFromLocalStorage();

            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), ChangePassword.class));
            }
        });


        btnSendResetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), ResetpasswordActivity.class));
            }
        });

        resetsimplepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), ResetPasswordSimpleActivity.class));
            }
        });

        btnRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null) {
                    reference1.child(uid).removeValue();
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Your profile is deleted :( Create a account now!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, RegisterPageNew.class));
                                        finish();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
                else
                {
                    reference1.child(uid).removeValue();
                    revokeAccess();
                }
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    //sign out method
    public void signOut() {

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
            reference.unauth();
            System.out.println("Should reach here yes");
            auth.signOut();
            System.out.println("Reaches here yes.");
            startActivity(new Intent(MainActivity.this, Login_Page_New.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    private void deleteImageFromFirebase(String mail)
    {
        String m = mail;
        StorageReference ref = ref1.child(m).child(Image);

        // Delete the file
        ref.delete().addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(MainActivity.this,"Profile Picture deleted successfully",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(MainActivity.this,"Uh-oh, an error occurred!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void deleteImageFromLocalStorage()
    {

        //String pathExt= Environment.getDataDirectory().toString();
        String m=mail;
        //StorageReference mainref = ref1.child(m).child(Image);
        String filename="Image.jpg";

        File localFile= null;
        //File localFile1 = null;

        localFile = new File("/sdcard/PasskeyManager/ProfileImages/"+m+"/"+filename);
        //localFile1 = new File("/sdcard/PasskeyManager/ProfileImages/"+m+"/");
        localFile.mkdirs();
        if(localFile.exists()) {
            localFile.delete();
        }
        else
        {
            Toast.makeText(MainActivity.this, "No Profile Pic Exists!", Toast.LENGTH_SHORT).show();
        }

    }

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        //updateUI(false);
                        Intent intent1 = new Intent(getApplicationContext(), Login_Page_New.class);
                        startActivity(intent1);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

    }
}