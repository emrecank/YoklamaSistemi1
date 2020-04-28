package com.example.yoklamasistemi1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class vpinspectionStartStopPage extends AppCompatActivity {
    TextView lname,lweek,ttt;
    String dd,hh,tt,ff;
    Button start,stop,inspectionlist;
    String currenttime,weektime;
    public static long mills;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startstop);
        lname=findViewById(R.id.lnametxt);
        lweek=findViewById(R.id.lweektxt);
        start=findViewById(R.id.startbutton);
        stop=findViewById(R.id.stopbutton);
        inspectionlist=findViewById(R.id.inspectionlist);
        ttt=findViewById(R.id.ttt);
        progressBar=findViewById(R.id.progressBar6);
        Intent intent=getIntent();
        dd=intent.getStringExtra("lname");
        hh=intent.getStringExtra("lweek");
        tt=intent.getStringExtra("online");
        ff=intent.getStringExtra("again");


        if(hh.equals(tt))
        {
            start.setEnabled(false);
            start.setBackgroundResource(android.R.color.darker_gray);
            inspectiontime(true);

        }
        else
        {
            stop.setEnabled(false);
            stop.setBackgroundResource(android.R.color.darker_gray);
        }
        lname.setText(dd);
        lweek.setText(hh);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startinspection();
                start.setEnabled(false);
                start.setBackgroundResource(android.R.color.darker_gray);
                stop.setEnabled(true);
                stop.setBackgroundResource(android.R.color.holo_red_dark);
                inspectionlist.setVisibility(View.INVISIBLE);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                inspectiontime(true);

                ttt.setVisibility(View.VISIBLE);



            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopinspection();
                start.setEnabled(true);
                start.setBackgroundResource(android.R.color.holo_green_light);
                stop.setEnabled(false);
                stop.setBackgroundResource(android.R.color.darker_gray);
                inspectionlist.setVisibility(View.VISIBLE);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                inspectiontime(false);




            }
        });
        inspectionlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newintent=new Intent(vpinspectionStartStopPage.this, vpinspectionsStudentsPage.class);
                newintent.putExtra("lname",dd);
                newintent.putExtra("lweek",hh);
                startActivity(newintent);
                finish();
            }
        });

    }
    public void startinspection(){
        String url="http://192.168.2.56//bitirme/start_inspection.php";


        StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(vpinspectionStartStopPage.this, "Yoklama Başlatıldı!", Toast.LENGTH_LONG).show();

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

                params.put("lname",dd);
                params.put("lweek",hh);


                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);

    }
    public void stopinspection(){
        String url="http://192.168.2.56//bitirme/stop_inspection.php";


        StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(vpinspectionStartStopPage.this, "Yoklama Durduruldu!", Toast.LENGTH_LONG).show();

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


                params.put("lname",dd);
                params.put("lweek",hh);


                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);

    }
    public void inspectiontime(final Boolean check){
        String url="http://192.168.2.56//bitirme/time.php";
        StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray= jsonObject.getJSONArray("time");
                    for (int i=0;i<jsonArray.length();i++) {

                        JSONObject ac = jsonArray.getJSONObject(i);
                        currenttime = ac.getString("currenttimes");
                        weektime = ac.getString("weektimes");

                        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                        Date date1 = format.parse(currenttime);
                        Date date2 = format.parse(weektime);
                        mills = date1.getTime() - date2.getTime();

                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
                CountDownTimer t=new CountDownTimer(120700-mills,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        ttt.setText("Yoklamanın otomatik olarak durdurulmasına : "+(millisUntilFinished/1000)/60+":"+(((millisUntilFinished/1000)%60)<10 ? ("0"+(millisUntilFinished/1000)%60):((millisUntilFinished/1000)%60)));
                    }

                    @Override
                    public void onFinish() {
                        start.setEnabled(true);
                        start.setBackgroundResource(android.R.color.holo_green_light);
                        stop.setEnabled(false);
                        stop.setBackgroundResource(android.R.color.darker_gray);
                        inspectionlist.setVisibility(View.VISIBLE);
                        Toast.makeText(vpinspectionStartStopPage.this, "Yoklama Durduruldu!", Toast.LENGTH_LONG).show();

                    }
                }.start();

                if(check==false)
                {
                    t.onFinish();
                    ttt.setVisibility(View.INVISIBLE);
                    t.cancel();
                    Intent newintent=new Intent(vpinspectionStartStopPage.this, vpinspectionsStudentsPage.class);
                    newintent.putExtra("lname",dd);
                    newintent.putExtra("lweek",hh);
                    startActivity(newintent);
                    finish();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(vpinspectionStartStopPage.this, "Hataaaa", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();



                params.put("lname",dd);
                params.put("lweek",hh);




                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }


}
