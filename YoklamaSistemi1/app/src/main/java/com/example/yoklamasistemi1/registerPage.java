package com.example.yoklamasistemi1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class registerPage extends AppCompatActivity {
    EditText number_text,name_text,surname_text,mail_text,password_text;
    Button user_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        number_text=findViewById(R.id.number_text);
        name_text=findViewById(R.id.name_text);
        surname_text=findViewById(R.id.surname_text);
        mail_text=findViewById(R.id.mail_text);
        password_text=findViewById(R.id.password_text);
        user_save=findViewById(R.id.user_save);
        user_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number_text.getText().toString().equals("") || name_text.getText().toString().equals("") || surname_text.getText().toString().equals("") || mail_text.getText().toString().equals("") ||  password_text.getText().toString().equals("") )
                {

                    Toast.makeText(registerPage.this, "Boş Alan Bırakmayanız!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    register();
                }


            }
        });
    }
    public void register(){
        String url="http://192.168.2.56//bitirme/register.php";

        StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String register=jsonObject.getString("success");


                    if(register.equals("1"))
                    {
                        Toast.makeText(registerPage.this, "Kayıt Başarılı!", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(registerPage.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(register.equals("2"))
                    {
                        Toast.makeText(registerPage.this, "Bu kullanıcı numarasıyla kayıt bulunmaktadır!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(registerPage.this, "İşlem Yapılamadı!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
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

                params.put("s_no",number_text.getText().toString());
                params.put("s_name",name_text.getText().toString());
                params.put("s_surname",surname_text.getText().toString());
                params.put("s_mail",mail_text.getText().toString());
                params.put("s_password",password_text.getText().toString());
                params.put("s_macadr",macaddress());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    public String macaddress()
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
