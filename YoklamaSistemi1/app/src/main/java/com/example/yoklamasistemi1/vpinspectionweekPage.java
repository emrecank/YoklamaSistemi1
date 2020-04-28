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

public class vpinspectionweekPage extends AppCompatActivity {
    String lname,user;
    ListView vpyoklamahaftasi_listview;
    TextView ladi,inspectionweekuser;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpinspectionweek_page);
        vpyoklamahaftasi_listview=findViewById(R.id.vpyoklamahaftasi_listview);
        ladi=findViewById(R.id.textView7);
        progressBar=findViewById(R.id.progressBar3);
        inspectionweekuser=findViewById(R.id.title3);
        Intent intent=getIntent();
        lname=intent.getStringExtra("lname");
        user=intent.getStringExtra("user");
        ladi.setText(lname);
        inspectionweekuser.setText(user);
        vpweek();
    }
    public void vpweek(){
        String url="http://192.168.2.56//bitirme/vp_week.php";


        StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);

                try {
                    final ArrayList<HashMap<String,String>> arrayList=new ArrayList<HashMap<String,String>>();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray= jsonObject.getJSONArray("lessonweek");

                    for (int i=0;i<jsonArray.length();i++)
                    {

                        HashMap<String,String> hashMap=new HashMap<String, String>();
                        JSONObject ac=jsonArray.getJSONObject(i);
                        String ccc=ac.getString("weeks");
                        if(ccc.length()==8)
                        {
                            ccc=ccc.substring(0,6);
                        }
                        else
                        {
                            ccc=ccc.substring(0,7);
                        }
                        hashMap.put("weeks", ac.getString("weeks"));
                        hashMap.put("check", ac.getString("check"));
                        hashMap.put("online", ac.getString("online"));
                        arrayList.add(hashMap);

                    }
                    vpinspectionweekAdapter adapter=new vpinspectionweekAdapter(vpinspectionweekPage.this,R.layout.vpinspection,arrayList,lname,user);
                    vpyoklamahaftasi_listview.setAdapter(adapter);
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
        Volley.newRequestQueue(this).add(request);

    }

}
