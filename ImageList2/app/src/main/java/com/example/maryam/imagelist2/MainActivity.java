package com.example.maryam.imagelist2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listview;
    EditText editText;
    Button button;
    String url;
    private static String TAG = MainActivity.class.getSimpleName();
    public Context mContext;
    View header;

    ListViewAdapter adapter;
    List<String> itemname=new ArrayList<>();
    List<String> imgid=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView) findViewById(R.id.listView);

//        LayoutInflater lf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        header = lf.inflate(R.layout.header, null);
//        listview.setAdapter(null);
//        listview.addHeaderView(header);

        editText = (EditText) findViewById(R.id.txtUrl);
        button = (Button) findViewById(R.id.btnLoadImg);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                url = editText.getText().toString();
                GetLastOfCatJsonArray(url);

            }
        });


    }

    public void GetLastOfCatJsonArray(String url) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        Log.d(TAG, response.toString());
                        if (response != null) {

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    String img = jsonObject.getString("photo");
                                    String author = jsonObject.getString("author");

                                    itemname.add(author);
                                    imgid.add(img);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }


                            adapter = new ListViewAdapter(MainActivity.this, itemname, imgid);
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
//
            }
        });
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonArrayRequest);
    }

}
