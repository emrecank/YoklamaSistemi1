package com.example.yoklamasistemi1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

public class inspectionAdapter extends ArrayAdapter<HashMap<String,String >> {
    private ArrayList<HashMap<String, String>> arrayList;
    private Context context;
    private int resource;
    private View view;
    private HashMap<String, String> hashMap;

    public inspectionAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrayList = objects;

    }


    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(resource, parent, false);
        final Button lesson=view.findViewById(R.id.lnamebutton);
        final TextView inspectionweek=view.findViewById(R.id.inspectionweektxt);
        hashMap=arrayList.get(position);
        lesson.setText(hashMap.get("lessons"));
        inspectionweek.setText(hashMap.get("week"));

        lesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("");
                builder.setMessage(lesson.getText().toString()+" adlı dersin "+inspectionweek.getText().toString()+" yoklamasına katılmak istiyor musunuz?");
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url="http://192.168.2.56//bitirme/inspection.php";
                        StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    String inspection=jsonObject.getString("success");

                                    if(inspection.equals("1"))
                                    {
                                        Toast.makeText(getContext().getApplicationContext(), "Yoklamanız kaydedildi!", Toast.LENGTH_SHORT).show();
                                        arrayList.remove(position);
                                        notifyDataSetChanged();
                                    }
                                    else if(inspection.equals("0"))
                                    {
                                        Toast.makeText(getContext().getApplicationContext(), "Yoklamanız kaydedilemedi!", Toast.LENGTH_LONG).show();
                                    }
                                    else if(inspection.equals("00"))
                                    {
                                        Toast.makeText(context, "Yoklamaya Katılamadınız,Yoklama Kapandı!", Toast.LENGTH_SHORT).show();
                                        arrayList.remove(position);
                                        notifyDataSetChanged();
                                    }

                                    else {
                                        Toast.makeText(getContext().getApplicationContext(), "Yanlış giden bişeyler var.", Toast.LENGTH_SHORT).show();
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
                                params.put("sno",hashMap.get("sno"));
                                params.put("lname",lesson.getText().toString());
                                params.put("lweek",inspectionweek.getText().toString());

                                return params;
                            }
                        };
                        Volley.newRequestQueue(getContext()).add(request);

                    }
                });
                builder.show();
            }
        });
        return view;
    }
}
