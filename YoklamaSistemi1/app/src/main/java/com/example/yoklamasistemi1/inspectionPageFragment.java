package com.example.yoklamasistemi1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class inspectionPageFragment extends Fragment {
    String s_no;
    ListView yoklamalistview;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button refreshbutton;
    ProgressBar progressBar;
    public static inspectionPageFragment getInstance() {
        return new inspectionPageFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inspection_page, container, false);
        yoklamalistview=root.findViewById(R.id.yoklamalistview);
        refreshbutton=root.findViewById(R.id.refreshbutton);
        progressBar=root.findViewById(R.id.progressBar8);
        Intent intent=getActivity().getIntent();
        s_no=intent.getStringExtra("s_no");


        refreshbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshfragment();
            }
        });
        lessons();



        return root;
    }
    public void refreshfragment()
    {
        progressBar.setVisibility(View.VISIBLE);
        Fragment currentFragment = getFragmentManager().findFragmentByTag(getTag());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();
        progressBar.setVisibility(View.INVISIBLE);

    }
    public void lessons(){
        String url="http://192.168.2.56//bitirme/lessons1.php";


        StringRequest request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);

                try {
                    final ArrayList<HashMap<String,String>> arrayList=new ArrayList<HashMap<String,String>>();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray= jsonObject.getJSONArray("lessons");

                    for (int i=0;i<jsonArray.length();i++)
                    {

                        HashMap<String,String> hashMap=new HashMap<String, String>();
                        JSONObject ac=jsonArray.getJSONObject(i);
                        hashMap.put("lessons", ac.getString("lesson"));
                        hashMap.put("week", ac.getString("week"));
                        hashMap.put("sno",s_no);
                        arrayList.add(hashMap);

                    }
                    inspectionAdapter adapter=new inspectionAdapter(getActivity(),R.layout.inspection,arrayList);
                    yoklamalistview.setAdapter(adapter);
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

                params.put("s_no",s_no);
                progressBar.setVisibility(View.VISIBLE);

                return params;
            }
        };
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);

    }
}
