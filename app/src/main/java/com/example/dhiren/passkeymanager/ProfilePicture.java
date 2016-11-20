package com.example.dhiren.passkeymanager;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfilePicture extends AppCompatActivity {

    ImageView viewImage;
    Bitmap nextImg;
    String picturePath="";
    Button b,uploadBtn;
    String uid;
    private ProgressBar progressBar;

    // creating an instance of Firebase Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    //creating a storage reference. Replace the below URL with your Firebase storage URL.
    StorageReference storageRef = storage.getReferenceFromUrl("gs://passkey-manager.appspot.com");

    static boolean errored = false;
    String id,type,URL,subtype,img_str,path,loc,uname,email,name;

    private static final String TAG = "ProfilePicture";
    private  static String ss="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            uname = user.getDisplayName();
            email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            uid = user.getUid();
        }
        else
        {
            uname = Login_Page_New.getTheName();
            email = Login_Page_New.getTheMail();
            uid = Login_Page_New.getTheId();
        }

        Firebase.setAndroidContext(this);

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile Picture");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true); */

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            subtype=extras.getString("SUBTYPE");
            type = extras.getString("TYPE");
            name = extras.getString("NAME");
            id = extras.getString("ID");
            URL = extras.getString("IP");
            path = id+"//"+type+"//"+subtype;
        }

        b=(Button)findViewById(R.id.btnSelectPhoto);
        uploadBtn = (Button)findViewById(R.id.btnNext);
        viewImage=(ImageView)findViewById(R.id.viewImage);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        onUploadButtonClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds options to the action bar if it is present.
        //getMenuInflater().inflate(R., menu);
        //getMenuInflater().inflate(R.menu.varna_lab_geo_locations, menu);

        return true;
    }

    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilePicture.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Button bd = (Button)findViewById(R.id.btnNext);
        bd.setVisibility(View.VISIBLE);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    //viewImage.setImageBitmap(bitmap);
                    nextImg=bitmap;

//                    String path = Environment
//                            .getExternalStorageDirectory()
//                            + File.separator
//                            + "Phoenix" + File.separator + "default";
                    f.delete();
//                    OutputStream outFile = null;
//                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
//                    try {
//                        outFile = new FileOutputStream(file);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
//                        outFile.flush();
//                        outFile.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    picturePath = saveImage(nextImg);
                    if(picturePath!=null)
                    {
                        Toast.makeText(ProfilePicture.this,"Picture Saved.", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath1 = c.getString(columnIndex);
                c.close();
                nextImg = (BitmapFactory.decodeFile(picturePath1));
                Log.w("image from gallery", picturePath1+"");
                //nextImg = resizeImg(nextImg);
                //resizeImg(thumbnail);
                //nextImg=thumbnail;
                //viewImage.setImageBitmap(thumbnail);
            }

            //nextImg = resizeImg(nextImg);
            //nextImg = increaseBrightness(nextImg,Integer.parseInt("70"));
            //nextImg = enhanceImage(nextImg);
            //nextImg = increaseContrast(nextImg);
            viewImage.setImageBitmap(nextImg);
        }
    }

    public String saveImage(Bitmap bitmap)
    {
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CDD");
//
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Log.e(TAG, "failed to create directory");
//                return null;
//            }
//        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        ContentResolver cr = getContentResolver();
        String title = "CDD_"+timeStamp+".jpg";
        String description = "Profile Image";
        String savedURL = MediaStore.Images.Media.insertImage(cr, bitmap, title, description);
        return savedURL+File.separator+title;
    }

    protected void onUploadButtonClick() {

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //progressBar = (ProgressBar) findViewById(R.id.progressBar);
                //progressBar.setVisibility(View.VISIBLE);
                StorageReference myfileRef = storageRef.child(email).child("ProfPic.jpg");
                //StorageReference myfileRef = storageRef.child("ProfPic.jpg");
                viewImage.setDrawingCacheEnabled(true);
                viewImage.buildDrawingCache();
                Bitmap bitmap = viewImage.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = myfileRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ProfilePicture.this, "TASK FAILED", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ProfilePicture.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                        //progressBar.setVisibility(View.GONE);

                        Uri downloadUrl =taskSnapshot.getDownloadUrl();
                        String DOWNLOAD_URL = downloadUrl.getPath();
                        Log.v("Download URL", DOWNLOAD_URL);
                        Toast.makeText(ProfilePicture.this, DOWNLOAD_URL, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}