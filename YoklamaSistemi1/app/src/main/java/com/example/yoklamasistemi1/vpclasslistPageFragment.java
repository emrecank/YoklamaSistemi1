package com.example.yoklamasistemi1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

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

public class vpclasslistPageFragment extends Fragment {
    ListView classlistview;
    String ino,user;

    public static vpclasslistPageFragment getInstance() {
        return new vpclasslistPageFragment();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vpclasslist_page,container, false);
        classlistview=root.findViewById(R.id.classlistview);
        Intent intent=getActivity().getIntent();
        ino=intent.getStringExtra("ino");
        user=intent.getStringExtra("user");
        vplessons();



        return root;
    }
    public void vplessons(){
        String url="http://192.168.2.56/bitirme/vp_lessons.php";


        StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    final ArrayList<HashMap<String,String>> arrayList=new ArrayList<HashMap<String,String>>();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray= jsonObject.getJSONArray("lessons");

                    for (int i=0;i<jsonArray.length();i++)
                    {

                        HashMap<String,String> hashMap=new HashMap<String, String>();
                        JSONObject ac=jsonArray.getJSONObject(i);
                        hashMap.put("lessons", ac.getString("lesson"));
                        arrayList.add(hashMap);

                    }
                    vpclasslistAdapter adapter=new vpclasslistAdapter(getActivity(),R.layout.classlist,arrayList,user);
                    classlistview.setAdapter(adapter);
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

                params.put("i_no",ino);

                return params;
            }
        };
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);

    }

}
