package com.example.yoklamasistemi1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button login,register;
    EditText no,password;
    String noap,passwordap;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=this.getPreferences(MainActivity.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        login=findViewById(R.id.button);
        register=findViewById(R.id.button2);
        no=findViewById(R.id.editText2);
        password=findViewById(R.id.editText);
        progressBar=findViewById(R.id.progressBar4);
        mac();

        if(sharedPreferences.getString("s_no","no-yok")!="no-yok" && sharedPreferences.getString("s_password","sifre-yok")!="sifre-yok") {
            noap=sharedPreferences.getString("s_no","no-yok");
            passwordap=sharedPreferences.getString("s_password","sifre-yok");
            no.setText(String.valueOf(noap));
            password.setText(String.valueOf(passwordap));
            login();

        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                noap=no.getText().toString();
                passwordap=password.getText().toString();
                editor.putString("s_no",noap);
                editor.putString("s_password",passwordap);
                editor.apply();

                login();

            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,registerPage.class);
                startActivity(intent);
            }
        });


    }
    public void login(){
        mac();
        String url="http://192.168.2.56//bitirme/login.php";

        StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String login=jsonObject.getString("success");
                    String user=jsonObject.getString("user");
                    System.out.println(login);

                    if(login.equals("1"))
                    {
                        Toast.makeText(MainActivity.this, "Başarılı", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MainActivity.this,mainPage.class);
                        intent.putExtra("s_no",noap);
                        intent.putExtra("user",user);
                        startActivity(intent);
                    }
                    else if(login.equals("2"))
                    {
                        Toast.makeText(MainActivity.this, "Farklı Cihazdan Giriş Yaptınız!", Toast.LENGTH_LONG).show();
                    }
                    else if(login.equals("3"))
                    {
                        Intent intent=new Intent(MainActivity.this,vpmainPage.class);
                        intent.putExtra("ino",noap);
                        intent.putExtra("user",user);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Kullanıcı adı veya şifre yanlış", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "İnternet Bağlantınızı Kontrol Edin!", Toast.LENGTH_SHORT).show();
            }
       }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("s_no",noap);
                params.put("s_password",passwordap);
                params.put("s_macadr",macadress());
                progressBar.setVisibility(View.VISIBLE);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);

    }
    public void mac(){
        String url="http://192.168.2.56//bitirme/mac.php";

        StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String mac=jsonObject.getString("msuccess");

                    if(mac.equals("1"))
                    {
                        register.setVisibility(View.INVISIBLE);
                    }
                    else {
                        register.setVisibility(View.VISIBLE);
                    }

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
                params.put("s_macadr",macadress());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);

    }

    public String macadress()
    {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }


}
