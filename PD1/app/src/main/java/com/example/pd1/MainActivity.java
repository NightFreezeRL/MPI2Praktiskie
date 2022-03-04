package com.example.pd1;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.Size;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button button;
    Uri imageUri;
    ViewPager mViewPager;
    int[] images;


    ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData()!=null) {
                        Toast.makeText(MainActivity.this, "Image Saved.", Toast.LENGTH_LONG).show();
                    }

                    // if we are here, everything processed successfully.

                        // if we are here, we are hearing back from the image gallery.

                        // the address of the image on the SD Card.
                        //Uri imageUri = result.getData();

                        // declare a stream to read the image data from the SD Card.
                        InputStream inputStream;

                        try {
                            inputStream = getContentResolver().openInputStream(imageUri);

                            // get a bitmap from the stream.
                            Bitmap image = BitmapFactory.decodeStream(inputStream);


                            // show the image to the user
                            imageView.setImageBitmap(image);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            // show a message to the user indicating that the image is unavailable.
                            Toast.makeText(MainActivity.this, "Unable to open image", Toast.LENGTH_LONG).show();
                        }



                }

        });

        imageView = findViewById(R.id.imageview);
        button = findViewById(R.id.button);
        button.setOnClickListener(aListener);
    }



    private View.OnClickListener aListener = new View.OnClickListener() {
            public void onClick(View v) {
                    Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri imagePath= createImage();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath);
                    activityResultLauncher.launch(intent);
                }
            };
    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intent);
    }
    private Uri createImage(){
        Uri uri = null;
        ContentResolver resolver= getContentResolver();
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q) {
            uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        }else{
            uri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        String imgName=String.valueOf(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,imgName+".jpg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/"+"My Images/");
        Uri finalUri = resolver.insert(uri, contentValues);
        imageUri=finalUri;

        return finalUri;
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        getMenuInflater();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.audio_thing:
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
                return true;
            case R.id.delete_images:
                Toast.makeText(this, "Delete photos yes", Toast.LENGTH_SHORT).show();
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }


    }


