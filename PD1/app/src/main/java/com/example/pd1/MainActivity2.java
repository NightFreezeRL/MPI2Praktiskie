package com.example.pd1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.core.app.ActivityCompat;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String outputFile;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    ListView listView;
    ArrayList recordingList;
    ArrayAdapter<String> adapter;
    Button recordButton;



    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {

        String pattern = "MM-dd-yyyy-hh-mm-ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        outputFile = "Recording-" + date + ".3gp";
        String file_path=getExternalFilesDir("Audio/").getAbsolutePath();


        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(file_path + "/" + outputFile);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        recordingList.add(outputFile);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recordingList);
        Log.d("Main:" ," Audio names : " + recordingList);
        listView.setAdapter(adapter);
        Bundle params=new Bundle();
        params.putString("Audio_recs", "S??ka recording");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

        boolean mStartRecording = true;

        View.OnClickListener clicker = new View.OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    recordButton.setText("Stop recording");
                } else {
                    recordButton.setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        recordButton = findViewById(R.id.recordButton);
        recordButton.setOnClickListener(clicker);




        listView = findViewById(R.id.listView);
        recordingList = new ArrayList();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recordingList);
        File audiofile = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.example.pd1/files/Audio/");
        File[] listOfAudios = audiofile.listFiles();
        Bundle params= new Bundle();
        for (File file : listOfAudios) {
           recordingList.add(file.getName());
           params.putString("Audio_recs", file.getName());
           mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);
           adapter.notifyDataSetChanged();
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recordingList);
        Log.d("Main:" ," Audio names : " + recordingList);
        listView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main2, menu);
        getMenuInflater();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.photo_thing:
                Bundle params= new Bundle();
                params.putString("Image_tab", "Image_button");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);
                finish();
                return true;
            case R.id.delete_images:
                Toast.makeText(this, "Delete photos yes", Toast.LENGTH_SHORT).show();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}