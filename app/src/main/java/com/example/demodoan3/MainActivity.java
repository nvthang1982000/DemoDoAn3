package com.example.demodoan3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.widget.SearchView;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ListView listView;
    private Toolbar toolbar;
    DrawerLayout drawerLayout;
    ImageButton IBmenu;
    Button hd,tt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        init();
        IBmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        setSupportActionBar(toolbar);
        tabLayout.setupWithViewPager(viewPager);
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPageAdapter);
        events();
    }

    private void events() {
        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, thongtinungdung.class);
                startActivity(intent);
            }
        });
        hd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, huongdan.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewpage);
        listView = findViewById(R.id.list_videos);
        toolbar = findViewById(R.id.mytoolbar);
        drawerLayout = findViewById(R.id.drawablelayout);
        IBmenu = findViewById(R.id.menu);
        hd = findViewById(R.id.btnhuongdan);
        tt = findViewById(R.id.btnthongtin);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_search_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.TimKiem);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("          Show Your Voice");
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);

        SearchView searchView  = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Tìm kiếm(Bài hát, Ca sĩ)");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}