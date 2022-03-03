package com.example.pd1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
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
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
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