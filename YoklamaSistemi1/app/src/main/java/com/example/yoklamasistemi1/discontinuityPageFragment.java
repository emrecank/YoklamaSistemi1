package com.example.yoklamasistemi1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class discontinuityPageFragment extends Fragment {
    ListView devamsizliklarimlistview;
    String s_no;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView ee;
    public static discontinuityPageFragment getInstance() {
        return new discontinuityPageFragment();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_discontinuity_page, container, false);
        devamsizliklarimlistview = root.findViewById(R.id.devamsizliklarimlistview);
        ee=root.findViewById(R.id.textView10);
        Intent intent=getActivity().getIntent();
        s_no=intent.getStringExtra("s_no");

        lessons();
        WifiManager wifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ScanResult result0 = (ScanResult) wifi.getScanResults();
        String ssid = result0.SSID;
        int rssi = result0.level;
        String rssiString0 = String.valueOf(rssi);
        ee.setText("\n" + ssid + "   " + rssiString0);



        return root;
    }

    public void lessons() {
        String url = "http:/192.168.2.56//bitirme/discontinuity.php";


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("lessons");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        JSONObject ac = jsonArray.getJSONObject(i);

                        hashMap.put("lessons", ac.getString("lesson"));
                        hashMap.put("discontinuityday", ac.getString("discontinuityday"));
                        hashMap.put("discontinuitycount", ac.getString("discontinuitycount"));
                        arrayList.add(hashMap);

                    }

                    discontinuityAdapter adapter = new discontinuityAdapter(getActivity(), R.layout.discontinuity, arrayList);
                    devamsizliklarimlistview.setAdapter(adapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();

                params.put("s_no",s_no);

                return params;
            }
        };
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
    }
}
