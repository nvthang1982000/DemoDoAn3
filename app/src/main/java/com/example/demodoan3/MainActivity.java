package com.example.demodoan3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.demodoan3.videosFragment.API_KEY;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ListView listView,timkiem;
    private Toolbar toolbar;
    DrawerLayout drawerLayout;
    ImageButton IBmenu;
    Button hd,tt;
    ArrayList<VideoYouTube> arr,arrTK;
    String ID_PLAYLIST = "PLHcHRgq0F87kwIOLtVb1zXUsRyOGpdOFN";
    String urlGetJson = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+ID_PLAYLIST+"&key="+API_KEY+"&maxResults=50";
    VideoYouTubeAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        checkAndRequestPermissions();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplication().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                        30);
            } else {
                Log.d("Home", "Already granted access");
//                initializeView(v);
            }
        }
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

        arr = new ArrayList<>();
        adapter = new VideoYouTubeAdapter(MainActivity.this,R.layout.row_video_youtube,arr);
        GetJsonYouTube(urlGetJson);
        timkiem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, PlayVideoActivity.class);
                intent.putExtra("idVideoYouTubeId", arrTK.get(i).getIdVideo());
                intent.putExtra("idVideoYouTubeTitle", arrTK.get(i).getTitle());
                intent.putExtra("idVideoYouTubeUrl", arrTK.get(i).getThumbnail());
                startActivity(intent);
            }
        });
    }
    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
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
        timkiem = findViewById(R.id.timkiem);
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
                s = s.toLowerCase();
                arrTK = new ArrayList<>();
                if(!s.equals("")){
                    for(int i = 0; i < arr.size();i++){
                        VideoYouTube vd = arr.get(i);
                        String ten = vd.getTitle().toLowerCase().trim();
                        if(ten.contains(s)){
                            arrTK.add(vd);
                        }
                    }
                    if(arrTK.size()>0){
                        viewPager.setVisibility(View.GONE);
                        adapter = new VideoYouTubeAdapter(MainActivity.this,R.layout.row_video_youtube,arrTK);
                        timkiem.setAdapter(adapter);
                        timkiem.setVisibility(View.VISIBLE);
                    }
                    else{
                        timkiem.setVisibility(View.GONE);
                        viewPager.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    timkiem.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private void GetJsonYouTube(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonItems = response.getJSONArray("items");

                            String title = ""; String url = ""; String idVideo="";
                            for (int i = 0; i<jsonItems.length();i++){
                                JSONObject jsonItem = jsonItems.getJSONObject(i);

                                JSONObject jsonSnippet = jsonItem.getJSONObject("snippet");
                                title = jsonSnippet.getString("title");

                                JSONObject jsonThumbnail = jsonSnippet.getJSONObject("thumbnails");
                                JSONObject jsonMedium = jsonThumbnail.getJSONObject("medium");
                                url = jsonMedium.getString("url");
                                JSONObject jsonResourceID = jsonSnippet.getJSONObject("resourceId");
                                idVideo = jsonResourceID.getString("videoId");
                                arr.add(new VideoYouTube(title, url, idVideo));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Lỗi",Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }
}