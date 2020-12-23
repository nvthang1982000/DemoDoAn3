package com.example.demodoan3;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class LichSuFragment extends Fragment {
    final String  DATABASE_NAME = "videoYouTube.db";
    SQLiteDatabase database;
    ImageView xoa;

    public LichSuFragment() {
        // Required empty public constructor
    }

    ListView lvLS;
    ArrayList<VideoYouTube> arrayLS=new ArrayList<>();
    VideoYouTubeAdapter adapterLS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lich_su, container, false);
        lvLS = (ListView) view.findViewById(R.id.lich_su);
        xoa = (ImageView) view.findViewById(R.id.xoa);
        readData();
        registerForContextMenu(lvLS);

        if(arrayLS.size()>0){
            adapterLS = new VideoYouTubeAdapter(getActivity(),R.layout.row_video_youtube,arrayLS);
//            arrayAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,arrayYeuThich);
            lvLS.setAdapter(adapterLS);
        }

        lvLS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), PlayVideoActivity.class);
                intent.putExtra("idVideoYouTubeId", arrayLS.get(i).getIdVideo());
                intent.putExtra("idVideoYouTubeTitle", arrayLS.get(i).getTitle());
                intent.putExtra("idVideoYouTubeUrl", arrayLS.get(i).getThumbnail());
                startActivity(intent);
            }
        });
        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrayLS.size()>0){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Xóa lịch sử");
                builder.setMessage("Bạn có muốn xóa lịch sử?");
                builder.setCancelable(false);
                builder.setPositiveButton("Trở lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });
                builder.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        database = DataBase.initDatabase(getActivity(), DATABASE_NAME);
                        String sql = "Delete from LichSu";
                        database.execSQL(sql);
                        arrayLS.clear();
                        adapterLS.notifyDataSetChanged();
                        lvLS.setAdapter(adapterLS);
                        Toast.makeText(getActivity(), "Đã xóa", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                }
                else {
                    Toast.makeText(getActivity(),"Không có gì để xóa", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return  view;
    }



    private void readData() {
        database = DataBase.initDatabase(getActivity(), DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from LichSu",null);
        arrayLS.clear();
        for (int i = cursor.getCount()-1; i >=0; i --){
            cursor.moveToPosition(i);
            String IdVideo = cursor.getString(0);
            String Title = cursor.getString(1);
            String Thumbnail = cursor.getString(2);
            arrayLS.add(new VideoYouTube(Title, Thumbnail, IdVideo));
        }

    }
}