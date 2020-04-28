package com.example.yoklamasistemi1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class vpstudentslistPage extends AppCompatActivity {
    ListView studentslistview;
    String lname;
    ProgressBar progressBar;
    TextView studentlistlesson,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentslist_page);
        studentslistview=findViewById(R.id.studentslistview);
        studentlistlesson=findViewById(R.id.studentslistlesson);
        progressBar=findViewById(R.id.progressBar2);
        user=findViewById(R.id.title4);
        Intent intent=getIntent();
        lname=intent.getStringExtra("lname");
        user.setText(intent.getStringExtra("user"));
        studentlistlesson.setText(lname+" dersini alan öğrenciler");
        vpstudentslist();
    }
    public void vpstudentslist(){
        String url="http://192.168.2.56//bitirme/vp_studentslist.php";


        StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);

                try {
                    final ArrayList<HashMap<String,String>> arrayList=new ArrayList<HashMap<String,String>>();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray= jsonObject.getJSONArray("students");

                    for (int i=0;i<jsonArray.length();i++)
                    {

                        HashMap<String,String> hashMap=new HashMap<String, String>();
                        JSONObject ac=jsonArray.getJSONObject(i);
                        hashMap.put("sname", ac.getString("sname"));
                        hashMap.put("discontinuityday", ac.getString("discontinuityday"));
                        hashMap.put("discontinuitycount", ac.getString("discontinuitycount"));
                        hashMap.put("totalinspection", ac.getString("totalinspection"));
                        arrayList.add(hashMap);

                    }
                    vpstudentslistAdapter adapter=new vpstudentslistAdapter(vpstudentslistPage.this,R.layout.discontinuity,arrayList);
                    studentslistview.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();

                params.put("lname",lname);
                progressBar.setVisibility(View.VISIBLE);

                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(request);

    }
}
