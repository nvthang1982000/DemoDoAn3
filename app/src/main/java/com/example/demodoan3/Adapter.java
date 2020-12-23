package com.example.demodoan3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Song> songs ;

    public Adapter(Context context, int layout, ArrayList<Song> songs) {
        this.context = context;
        this.layout = layout;
        this.songs = songs;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
            holder.tvTitle = (TextView) view.findViewById(R.id.ten);
            holder.tvDuration = (TextView) view.findViewById(R.id.time);
            holder.pl = (ImageView) view.findViewById(R.id.imageviewpl);
            view.setTag(holder);

        }
        holder = (ViewHolder) view.getTag();

        holder.tvTitle.setText(songs.get(i).getTitle());
        holder.tvDuration.setText(songs.get(i).DurationToString());

        if (songs.get(i).isSelect()){
            holder.pl.setSelected(true);
        }
        else {
            holder.pl.setSelected(false);
        }

        return view;
    }
    public class ViewHolder{
        private TextView tvTitle;// Tiêu đề của bài hát
        private TextView tvDuration; // Thời gian của bài hát
        private ImageView pl;
    }
}
