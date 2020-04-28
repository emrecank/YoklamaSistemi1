package com.example.yoklamasistemi1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.yoklamasistemi1.ui.main.vpSectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class vpmainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpmain_page);
        vpSectionsPagerAdapter sectionsPagerAdapter = new vpSectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        TextView txt = findViewById(R.id.title);
        Intent intent=getIntent();
        txt.setText(intent.getStringExtra("user"));
        tabs.setupWithViewPager(viewPager);
    }
}