package com.example.demodoan3;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by User on 29-7-2016.
 */
public class SongMaster implements MediaPlayer.OnCompletionListener
{
    private ArrayList<Song> arrData;
    private MediaPlayer mediaPlayer;
    private Context context;
    private int currentIndex = 0;

    public SongMaster(Context context, ArrayList<Song> arrData) {
        this.context = context;
        this.arrData = arrData;
        mediaPlayer = new MediaPlayer();
    }
    public void loadSong(String uri){
        resetSong();

//        mediaPlayer = MediaPlayer.create(context, uri);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(uri);

        }catch (Exception ex){
            System.out.println("Lỗi mất rồi ------------------------------------------------------------------------"+ uri);
        }
        if(mediaPlayer!=null)
            mediaPlayer.setOnCompletionListener(this);
//        else
//            mediaPlayer = MediaPlayer.create(context, );
    }
    public void loadSong(Uri uri){

        try {
            if(mediaPlayer!=null)
            {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(context, uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.reset();
//            mediaPlayer.setDataSource(uri.getPath());
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//        try {
//            mediaPlayer.prepare();
//        } catch (IllegalStateException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        mediaPlayer.start();
        mediaPlayer.setLooping(true);



//        resetSong();
//
//        mediaPlayer = MediaPlayer.create(context, uri);
//        mediaPlayer.reset();
//
//        //if(mediaPlayer!=null)
//        mediaPlayer.setOnCompletionListener(this);
//        else
//            mediaPlayer = MediaPlayer.create(context, );
    }

    public void startSong(){
        if (mediaPlayer == null){
            String data = arrData.get(currentIndex).getData();
            Uri uri = Uri.parse(data);
            try {
                mediaPlayer.setDataSource(data);

            }catch (Exception ex){
                System.out.println("Lỗi mất rồi --------------------------------------------------------------"+ data);
            }
            //mediaPlayer = MediaPlayer.create(context, uri);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.start();
        }
        else
            mediaPlayer.start();
    }

    public void stopSong(){
        if (mediaPlayer != null){
            currentIndex=0;
            mediaPlayer.stop();
        }
    }

    public void pauseSong(){
        if (mediaPlayer != null){
            mediaPlayer.pause();
        }
    }

    public void resetSong(){
        if (mediaPlayer != null){
            mediaPlayer.reset();
        }
    }

    public void loopMedia(boolean isLooping){
        if (mediaPlayer != null){
            mediaPlayer.setLooping(isLooping);
        }
    }

    public void seekMedia(int position){
        if (mediaPlayer != null){
            mediaPlayer.seekTo(position);
        }
    }

    public void changeSong(int state){
        currentIndex += state;
        if (currentIndex < 0){
            currentIndex = arrData.size() - 1;
        }
        if (currentIndex > arrData.size() - 1){
            currentIndex = 0;
        }
        String data = arrData.get(currentIndex).getData();
        Uri uri = Uri.parse(data);
        loadSong(uri);
        startSong();
    }


    public void setCurrentIndex(int currentIndex) {

        this.currentIndex = currentIndex;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        for (Song itemSong : arrData) {
            itemSong.setSelected(false);
        }
        //Nếu không lặp thì mới thực hiện chuyển bài hát
        if (!mediaPlayer.isLooping()) {
            changeSong(1);
            arrData.get(currentIndex).setSelected(true);
        }
        else // Nếu người dùng nhấn loop thì phát lại bài hát vừa phát
        {
//
            String data = arrData.get(currentIndex).getData();
            Uri uri = Uri.parse(data);
            mediaPlayer = MediaPlayer.create(context, uri);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.start();
        }
    }

    public void updateListView(Adapter myAdapter){
        myAdapter.notifyDataSetChanged();
    }
    public boolean loop()
    {
        if (mediaPlayer.isLooping()) {
            mediaPlayer.setLooping(false);
            return false;
        }
        else
        {
            mediaPlayer.setLooping(true);
            return true;
        }
    }

    public boolean pauseOrResume(){
        if (mediaPlayer == null){
            return false;
        }
        boolean isPlaying = mediaPlayer.isPlaying();
        if (isPlaying){
            pauseSong();
        }
        else {
            startSong();
        }
        return !isPlaying;
    }

    public boolean checkPlaying(){
        if (mediaPlayer == null){
            return false;
        }
        return mediaPlayer.isPlaying();
    }

    public int getCurrentDuration() {
        if (mediaPlayer != null){
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public String getDuration(){
        return arrData.get(currentIndex).DurationToString();
    }

    public int getDurationInt(){
        return (int)arrData.get(currentIndex).getDuration();
    }

    public String getTitle(){
        return arrData.get(currentIndex).getTitle();
    }
}
