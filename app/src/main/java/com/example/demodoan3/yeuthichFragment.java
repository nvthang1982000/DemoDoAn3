package com.example.demodoan3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class yeuthichFragment extends Fragment {
    final String  DATABASE_NAME = "videoYouTube.db";
    SQLiteDatabase database;

    public yeuthichFragment() {
        // Required empty public constructor
    }
    ListView lvYeuThich;
    ArrayList<VideoYouTube> arrayYeuThich=new ArrayList<>();
    VideoYouTubeAdapter adapterYeuThich;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_yeuthich, container, false);
        lvYeuThich = (ListView) view.findViewById(R.id.yeu_thich);
        readData();
        
        if(arrayYeuThich.size()>0){
            adapterYeuThich = new VideoYouTubeAdapter(getActivity(),R.layout.row_video_youtube,arrayYeuThich);
//            arrayAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,arrayYeuThich);
            lvYeuThich.setAdapter(adapterYeuThich);
        }
        lvYeuThich.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), PlayVideoActivity.class);
                intent.putExtra("idVideoYouTubeId", arrayYeuThich.get(i).getIdVideo());
                intent.putExtra("idVideoYouTubeTitle", arrayYeuThich.get(i).getTitle());
                intent.putExtra("idVideoYouTubeUrl", arrayYeuThich.get(i).getThumbnail());
                startActivity(intent);
            }
        });


    return  view;
    }
    private void readData() {
        database = DataBase.initDatabase(getActivity(), DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from Video",null);
        arrayYeuThich.clear();
        for (int i = 0; i < cursor.getCount(); i ++){
            cursor.moveToPosition(i);
            String IdVideo = cursor.getString(0);
            String Title = cursor.getString(1);
            String Thumbnail = cursor.getString(2);
            arrayYeuThich.add(new VideoYouTube(Title, Thumbnail, IdVideo));
        }

    }
}