package com.example.demodoan3;

public class Song {
    private String Title; // Tiêu đề bài hát
    private String data; // Đường dẫn
    private long duration; // Thời lượng bài hát
    private boolean selected;


    public Song(String title, String data, long duration) {
        Title = title;
        this.data = data;
        this.duration = duration;
        selected = false;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public String DurationToString() {
        String durationText = null;
        if (((duration/1000)%60) >= 10){
            durationText = (duration/60000) + ":" + ((duration/1000)%60);
        }
        else {
            durationText = (duration/60000) + ":0" + ((duration/1000)%60);
        }
        return durationText;
    }
    public boolean isSelect() {
        return selected;
    }

}
