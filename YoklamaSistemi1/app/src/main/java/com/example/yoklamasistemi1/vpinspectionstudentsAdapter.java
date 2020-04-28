package com.example.yoklamasistemi1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class vpinspectionstudentsAdapter extends ArrayAdapter<HashMap<String,String >> {
    private ArrayList<HashMap<String, String>> arrayList;
    private Context context;
    private int resource;
    private View view;
    private HashMap<String, String> hashMap;

    public vpinspectionstudentsAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrayList = objects;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(resource, parent, false);
        final Button sname = view.findViewById(R.id.snamebtn);
        final Button status = view.findViewById(R.id.status);


        hashMap = arrayList.get(position);

        if(hashMap.get("status").equals("0"))
        {
            notifyDataSetChanged();
            status.setBackgroundResource(android.R.drawable.ic_input_add);
            sname.setText(hashMap.get("sname"));
            status.setText(hashMap.get("sno"));
            status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url="http://192.168.2.56//bitirme/vp_changeinspection.php";


                    StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                String login=jsonObject.getString("success");

                                if(login.equals("1"))
                                {
                                    arrayList.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Yoklama Listesine Eklendi!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(context, "Hata Oluştu!", Toast.LENGTH_SHORT).show();
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
                            params.put("sno",status.getText().toString());
                            params.put("lname",hashMap.get("lname"));
                            params.put("lweek",hashMap.get("lweek"));
                            params.put("changeinspection","1");


                            return params;
                        }
                    };
                    Volley.newRequestQueue(context).add(request);
                }
            });
        }
        else
        {
            notifyDataSetChanged();
            status.setBackgroundResource(android.R.drawable.presence_busy);
            sname.setText(hashMap.get("sname"));
            status.setText(hashMap.get("sno"));
            status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String url="http://192.168.2.56//bitirme/vp_changeinspection.php";


                    StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                String login=jsonObject.getString("success");

                                if(login.equals("1"))
                                {
                                    arrayList.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Yoklama Listesinden Çıkarıldı!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(context, "Hata Oluştu!", Toast.LENGTH_SHORT).show();
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
                            params.put("sno",status.getText().toString());
                            params.put("lname",hashMap.get("lname"));
                            params.put("lweek",hashMap.get("lweek"));
                            params.put("changeinspection","0");


                            return params;
                        }
                    };
                    Volley.newRequestQueue(context).add(request);

                }
            });
        }
        return view;


    }
}
