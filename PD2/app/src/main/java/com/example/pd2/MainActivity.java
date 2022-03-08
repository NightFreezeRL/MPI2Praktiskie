package com.example.pd2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    JSONArray jArray;
    JSONObject jData;
    private ListView listView;

    ArrayList<Fish> arrayOfWebData = new ArrayList<Fish>();
    class Fish{
        public String Species_Name;
        public String Location;
        public String Color;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        searchNameStringRequest();
    }
    private void searchNameStringRequest() {

        String url = "https://www.fishwatch.gov/api/species/";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url,null,
                new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<String> items = new ArrayList<String>();
                jArray = null;
                try {
                    jArray = response;
                    for (int i=0;i< jArray.length();i++){
                        JSONObject jData = jArray.getJSONObject(i);
                        Fish resultRow = new Fish();
                        resultRow.Species_Name = jData.getString("Species Name");
                        resultRow.Location = jData.getString("Location");
                        resultRow.Color = jData.getString("Color");
                        arrayOfWebData.add(resultRow);
                        Log.d("Main",resultRow.Species_Name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListView listView = (ListView) findViewById(R.id.listView);
                ArrayList<String> fishes = new ArrayList<String>();
                for(int i=0; i < arrayOfWebData.size(); i++) {
                    String name = arrayOfWebData.get(i).Species_Name;
                    fishes.add(name);

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,fishes);
                listView.setAdapter(adapter);
            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"FishWatch didn't respond", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
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
            case R.id.map_thing:
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

}