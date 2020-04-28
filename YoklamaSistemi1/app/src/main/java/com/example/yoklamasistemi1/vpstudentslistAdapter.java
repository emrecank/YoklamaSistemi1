package com.example.yoklamasistemi1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class vpstudentslistAdapter extends ArrayAdapter<HashMap<String,String >> {
    private ArrayList<HashMap<String, String>> arrayList;
    private Context context;
    private int resource;
    private View view;
    private HashMap<String, String> hashMap;

    public vpstudentslistAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrayList = objects;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(resource, parent, false);
        TextView ders=view.findViewById(R.id.textView8);
        TextView discontinuitycount=view.findViewById(R.id.discontinuitycount);
        final Button discontinuityday=view.findViewById(R.id.discontinuityday);
        hashMap=arrayList.get(position);
        ders.setText(hashMap.get("sname"));
        discontinuitycount.setText(hashMap.get("discontinuitycount")+"/"+hashMap.get("totalinspection"));
        discontinuityday.setText(hashMap.get("discontinuityday"));
        discontinuityday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Devamsızlık Günleri");
                builder.setMessage(discontinuityday.getText().toString());
                builder.setNegativeButton("Tamam", null);

                builder.show();
            }
        });
        return view;


    }
}

