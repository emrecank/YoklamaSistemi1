package com.example.yoklamasistemi1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

public class vpinspectionAdapter extends ArrayAdapter<HashMap<String,String >> {
    private ArrayList<HashMap<String, String>> arrayList;
    private Context context;
    private int resource;
    private View view;
    private HashMap<String, String> hashMap;
    String user;

    public vpinspectionAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects,String user) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrayList = objects;
        this.user=user;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(resource, parent, false);
        final Button lesson = view.findViewById(R.id.lnamebtn);
        final Button online = view.findViewById(R.id.lonlinebtn);

        hashMap = arrayList.get(position);
        lesson.setText(hashMap.get("lessons"));
        if(hashMap.get("lonline").equals("0"))
        {
            online.setVisibility(View.INVISIBLE);
        }
        else
        {
            online.setVisibility(View.VISIBLE);
        }

        lesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), vpinspectionweekPage.class);
                intent.putExtra("lname", lesson.getText().toString());
                intent.putExtra("user", user);
                getContext().startActivity(intent);
            }
        });
        return view;


    }
}
