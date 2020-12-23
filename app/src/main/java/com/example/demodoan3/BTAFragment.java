package com.example.demodoan3;

import android.app.Dialog;

import android.content.DialogInterface;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class BTAFragment extends Fragment implements AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener,View.OnClickListener{
    private MyProvider myProvider;
    private ImageView imPrev, imPlay, imNext, imStop, imloop;
    private SeekBar sbPlay;
    private SongMaster songMaster;
    private Handler handler = new Handler();
    TextView tvCurrentSong, tvCurrentTime, tvTotalTime;
    private Dialog dialog;


    ListView lv;
    ArrayList<Song> arr=new ArrayList<>();
    ArrayList<Song> arr1=new ArrayList<>();
    Adapter adapter;
    String a = Environment.getExternalStorageDirectory() + "/BanThuAm";

    public BTAFragment() {
        // Required empty public constructor
    }
    ArrayList<VideoYouTube> arrayList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b_t_a, container, false);
        // Inflate the layout for this fragment
        lv = view.findViewById(R.id.lvBTA);
        imNext = (ImageView) view.findViewById(R.id.imNext);
        imPrev = (ImageView) view.findViewById(R.id.imPrev);
        imPlay = (ImageView) view.findViewById(R.id.imPlay);
        imStop = (ImageView) view.findViewById(R.id.imStop);
        imloop=(ImageView) view.findViewById(R.id.imloop);
        tvCurrentSong = (TextView) view.findViewById(R.id.tvCurrentSong);
        sbPlay = (SeekBar) view.findViewById(R.id.sbPlay);
        tvCurrentTime = (TextView) view.findViewById(R.id.tvCurrentTime);
        tvTotalTime = (TextView) view.findViewById(R.id.tvTotalTime);
        adapter = new Adapter(getActivity(), android.R.layout.simple_list_item_1, arr1);
        myProvider = new MyProvider(getActivity());

        // Inflate the layout for this fragment
        arr = myProvider.getData();
        for(int i = 0; i < arr.size();i++){
            if(arr.get(i).getData().contains(a)){
                arr1.add(arr.get(i));
            }
        }
        if(arr1.size()>0){
            adapter = new Adapter(getActivity(),R.layout.adapter,arr1);
            songMaster = new SongMaster(getActivity(), arr1);
            initViews();
            updateSeekBar();
        }
        else{

        }

        return view;
    }

    private void initViews() {
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);
        imloop.setOnClickListener(this);

        imNext.setOnClickListener(this);
        imPlay.setOnClickListener(this);
        imPrev.setOnClickListener(this);
        imStop.setOnClickListener(this);

        sbPlay.setThumb(null);
        sbPlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && songMaster.checkPlaying()){
                    songMaster.seekMedia(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private void updateSeekBar() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvCurrentSong.setText(songMaster.getTitle());
                tvTotalTime.setText(songMaster.getDuration());
                sbPlay.setMax(songMaster.getDurationInt());
                if (songMaster.checkPlaying()){
                    sbPlay.setProgress(songMaster.getCurrentDuration());

                    int duration = songMaster.getCurrentDuration();
                    if (((duration/1000)%60) >= 10){
                        tvCurrentTime.setText((duration/60000) + ":" + ((duration/1000)%60));
                    }
                    else {
                        tvCurrentTime.setText((duration/60000) + ":0" + ((duration/1000)%60));
                    }
                }
                handler.postDelayed(this, 1);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        for (Song itemSong : arr1) {
            itemSong.setSelected(false);
        }
        arr1.get(position).setSelected(true);
        adapter.notifyDataSetChanged();
//        Toast.makeText(this,arrData.get(position).getData(),Toast.LENGTH_LONG).show();
        File f = new File(arr1.get(position).getData());
        songMaster.loadSong(Uri.fromFile(f));
        // songMaster.loadSong(arrData.get(position).getData());
//        MediaScannerConnection.scanFile(this, new String[] { arrData.get(position).getData()}, null,
//                new MediaScannerConnection.OnScanCompletedListener() {
//                    @Override
//                    public void onScanCompleted(String path, Uri uri) {
//                        songMaster.loadSong(uri);
//                        Log.i("URI", uri.toString());
//                    }
//                });
//
//        Toast.makeText(getApplicationContext(),arrData.get(position).getTitle().toString(),Toast.LENGTH_LONG).show();

        songMaster.setCurrentIndex(position);
        songMaster.startSong();
        boolean state = songMaster.checkPlaying();
        imPlay.setSelected(state);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imNext:
                songMaster.changeSong(1);
                adapter.notifyDataSetChanged();
                break;
            case R.id.imPrev:
                songMaster.changeSong(-1);
                adapter.notifyDataSetChanged();
                break;
            case R.id.imPlay:
                boolean state = songMaster.pauseOrResume();
                imPlay.setSelected(state);
                break;
            case R.id.imStop:
                songMaster.stopSong();
                imPlay.setSelected(false);
                break;
            case R.id.imloop:
                boolean loop = songMaster.loop();
                imloop.setSelected(loop);
        }

    }
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Xóa bản thu âm");
        builder.setMessage("Bạn có muốn xóa bản thu này?");
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
                String xoa = arr1.get(position).getTitle();
                getActivity().deleteFile(xoa);
                arr1.remove(position);
                adapter.notifyDataSetChanged();
                lv.setAdapter(adapter);
                Toast.makeText(getActivity(), "Đã xóa", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return false;
    }

}