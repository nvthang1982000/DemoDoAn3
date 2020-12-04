package com.example.demodoan3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoYouTubeAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<VideoYouTube> videoYoutubeList;

    public VideoYouTubeAdapter(Context context, int layout, List<VideoYouTube> videoYoutubeList) {
        this.context = context;
        this.layout = layout;
        this.videoYoutubeList = videoYoutubeList;
    }

    @Override
    public int getCount() {
        return videoYoutubeList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private class ViewHolder{
        ImageView imgThumbnail;
        TextView txtTitle;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
            holder.txtTitle = (TextView) view.findViewById(R.id.textViewTitle);
            holder.imgThumbnail = (ImageView) view.findViewById(R.id.imageviewThumbnail);
            view.setTag(holder);

        }
        else{
            holder = (ViewHolder) view.getTag();
        }
        VideoYouTube video = videoYoutubeList.get(i);
        holder.txtTitle.setText(video.getTitle());

        Picasso.get().load(video.getThumbnail()).into(holder.imgThumbnail);
        return view;
    }
}

