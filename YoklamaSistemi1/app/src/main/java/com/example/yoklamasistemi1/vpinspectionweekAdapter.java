package com.example.yoklamasistemi1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class vpinspectionweekAdapter extends ArrayAdapter<HashMap<String,String >> {
    private ArrayList<HashMap<String, String>> arrayList;
    private Context context;
    private int resource;
    private View view;
    private HashMap<String, String> hashMap;
    private String lname,user;

    public vpinspectionweekAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects, String lname,String user) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrayList = objects;
        this.lname=lname;
        this.user=user;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(resource, parent, false);
        final View divider=view.findViewById(R.id.divider);
        final Button lweek = view.findViewById(R.id.lweekbtn);
        final Button lweekcheck = view.findViewById(R.id.inspectionweekcheck);
        final Button lweekonline = view.findViewById(R.id.lweekonline);

        hashMap = arrayList.get(position);
        lweek.setText(hashMap.get("weeks"));
        /*if(hashMap.get("weeks").length()==8)
        {
            lweek.setText(hashMap.get("weeks").substring(0,6)+" "+hashMap.get("weeks").substring(7)+". Yoklama");
        }
        else
        {
            lweek.setText(hashMap.get("weeks").substring(0,7)+" "+hashMap.get("weeks").substring(8)+". Yoklama");
        }*/

        lweekcheck.setText(hashMap.get("check"));
        lweekonline.setText(hashMap.get("online"));
        if(position%3==0)
        {
            divider.setVisibility(View.VISIBLE);
        }
        else
        {
            divider.setVisibility(View.INVISIBLE);
        }
        if(lweekcheck.getText().toString().equals("0"))
        {
            lweekcheck.setVisibility(View.INVISIBLE);
            if(lweekonline.getText().toString().equals(hashMap.get("weeks")))
            {
                lweekonline.setVisibility(View.VISIBLE);
            }
            else
            {
                lweekonline.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            lweekcheck.setVisibility(View.VISIBLE);

        }

        lweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lweekcheck.getText().toString().equals("0"))
                {
                    Intent intent=new Intent(getContext(), vpinspectionStartStopPage.class);
                    intent.putExtra("lname",lname);
                    /*if(hashMap.get("weeks").length()==8)
                    {
                        intent.putExtra("lweek",lweek.getText().toString().substring(0,6)+"_"+lweek.getText().toString().substring(7,8));
                    }
                    else
                    {
                        intent.putExtra("lweek",lweek.getText().toString().substring(0,7)+"_"+lweek.getText().toString().substring(8,9));
                    }*/
                    intent.putExtra("lweek",lweek.getText().toString());
                    intent.putExtra("online",lweekonline.getText().toString());
                    intent.putExtra("again","0");
                    getContext().startActivity(intent);
                    ((vpinspectionweekPage)context).finish();
                }
                else{
                    Intent intent=new Intent(getContext(), vpinspectionsStudentsPage.class);
                    intent.putExtra("lname",lname);
                    /*if(hashMap.get("weeks").length()==8)
                    {
                        intent.putExtra("lweek",lweek.getText().toString().substring(0,6)+"_"+lweek.getText().toString().substring(7,8));
                    }
                    else
                    {
                        intent.putExtra("lweek",lweek.getText().toString().substring(0,7)+"_"+lweek.getText().toString().substring(8,9));
                    }*/
                    intent.putExtra("lweek",lweek.getText().toString());
                    intent.putExtra("again","0");
                    getContext().startActivity(intent);
                }
            }
        });
        return view;


    }
}
