package com.example.yoklamasistemi1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

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

public class vpinspectionsStudentsPage extends AppCompatActivity {
    ListView inspectionsstudentslistview;
    String lname,lweek;
    Button yoklamaplus,yoklamaminus,againinspection;
    int studentcount=0;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpinspectionsstudents_page);
        inspectionsstudentslistview=findViewById(R.id.inspectionsstudentslistview);
        yoklamaplus=findViewById(R.id.yoklamaplus);
        yoklamaminus=findViewById(R.id.yoklamaminus);
        againinspection=findViewById(R.id.againinspection);
        progressBar=findViewById(R.id.progressBar);
        Intent intent=getIntent();
        lname=intent.getStringExtra("lname");
        lweek=intent.getStringExtra("lweek");
        vpstudentslistplus();
        yoklamaplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpstudentslistplus();
                progressBar.setVisibility(View.VISIBLE);
                yoklamaplus.setBackgroundResource(android.R.color.holo_green_light);
                yoklamaplus.setTextColor(getColorStateList(android.R.color.background_light));
                yoklamaplus.setEnabled(false);
                yoklamaminus.setBackgroundResource(android.R.color.background_light);
                yoklamaminus.setTextColor(getColorStateList(android.R.color.background_dark));
                yoklamaminus.setEnabled(true);
            }
        });
        yoklamaminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                vpstudentslistminus();
                yoklamaplus.setBackgroundResource(android.R.color.background_light);
                yoklamaplus.setTextColor(getColorStateList(android.R.color.background_dark));
                yoklamaplus.setEnabled(true);
                yoklamaminus.setBackgroundResource(android.R.color.holo_green_light);
                yoklamaminus.setTextColor(getColorStateList(android.R.color.background_light));
                yoklamaminus.setEnabled(false);
            }
        });
        againinspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inttt=new Intent(vpinspectionsStudentsPage.this, vpinspectionStartStopPage.class);
                inttt.putExtra("lname",lname);
                inttt.putExtra("lweek",lweek);
                inttt.putExtra("lonline","0");
                inttt.putExtra("again","1");
                startActivity(inttt);
                finish();
            }
        });
    }
    public void vpstudentslistplus(){
        String url="http://192.168.2.56//bitirme/vp_inspections_students.php";
        final ArrayList<HashMap<String,String>> arrayList=new ArrayList<HashMap<String,String>>();

        StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);
                System.out.println(response);
                if(response.equals("{\"success\":\"0\",\"message\":\"Not successfully\"}"))
                {
                    vpinspectionstudentsAdapter adapter=new vpinspectionstudentsAdapter(vpinspectionsStudentsPage.this,R.layout.vpinspectionstudents,arrayList);
                    inspectionsstudentslistview.setAdapter(adapter);
                    AlertDialog.Builder builder = new AlertDialog.Builder(vpinspectionsStudentsPage.this);
                    builder.setTitle("Yoklamaya Katılan Öğrenci Sayısı");
                    builder.setMessage("0");
                    builder.setNegativeButton("Tamam", null);
                    builder.show();
                }
                else
                {
                    try {

                        JSONObject jsonObject=new JSONObject(response);
                        JSONArray jsonArray= jsonObject.getJSONArray("students");
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            HashMap<String,String> hashMap=new HashMap<String, String>();
                            JSONObject ac=jsonArray.getJSONObject(i);
                            hashMap.put("sname", ac.getString("sname"));
                            hashMap.put("sno", ac.getString("sno"));
                            hashMap.put("lname",lname);
                            hashMap.put("lweek",lweek);
                            hashMap.put("status", "1");
                            arrayList.add(hashMap);

                        }
                        vpinspectionstudentsAdapter adapter=new vpinspectionstudentsAdapter(vpinspectionsStudentsPage.this,R.layout.vpinspectionstudents,arrayList);
                        inspectionsstudentslistview.setAdapter(adapter);
                        AlertDialog.Builder builder = new AlertDialog.Builder(vpinspectionsStudentsPage.this);
                        builder.setTitle("Yoklamaya Katılan Öğrenci Sayısı");
                        builder.setMessage(String.valueOf(arrayList.size()));
                        builder.setNegativeButton("Tamam", null);
                        builder.show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                params.put("lweek",lweek);
                params.put("status","1");
                progressBar.setVisibility(View.VISIBLE);


                return params;
            }
        };
        Volley.newRequestQueue(vpinspectionsStudentsPage.this).add(request);

    }
    public void vpstudentslistminus(){
        String url="http://192.168.2.56//bitirme/vp_inspections_students.php";
        final ArrayList<HashMap<String,String>> arrayList=new ArrayList<HashMap<String,String>>();

        StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);
                if(response.equals("{\"success\":\"0\",\"message\":\"Not successfully\"}"))
                {
                    vpinspectionstudentsAdapter adapter=new vpinspectionstudentsAdapter(vpinspectionsStudentsPage.this,R.layout.vpinspectionstudents,arrayList);
                    inspectionsstudentslistview.setAdapter(adapter);
                }
                else
                {
                    try {

                        JSONObject jsonObject=new JSONObject(response);
                        JSONArray jsonArray= jsonObject.getJSONArray("students");

                        for (int i=0;i<jsonArray.length();i++)
                        {

                            HashMap<String,String> hashMap=new HashMap<String, String>();
                            JSONObject ac=jsonArray.getJSONObject(i);
                            hashMap.put("sname", ac.getString("sname"));
                            hashMap.put("sno", ac.getString("sno"));
                            hashMap.put("lname",lname);
                            hashMap.put("lweek",lweek);
                            hashMap.put("status", "0");

                            arrayList.add(hashMap);

                        }
                        vpinspectionstudentsAdapter adapter=new vpinspectionstudentsAdapter(vpinspectionsStudentsPage.this,R.layout.vpinspectionstudents,arrayList);
                        inspectionsstudentslistview.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                params.put("lweek",lweek);
                params.put("status","0");
                progressBar.setVisibility(View.VISIBLE);


                return params;
            }
        };
        Volley.newRequestQueue(vpinspectionsStudentsPage.this).add(request);

    }
}
