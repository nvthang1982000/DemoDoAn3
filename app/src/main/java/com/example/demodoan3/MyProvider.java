package com.example.demodoan3;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;



import java.util.ArrayList;

public class MyProvider {
    public static final String TAG = "MyProvider";
    private ContentResolver mResolver;
    private Context mContext;

    public MyProvider(Context mContext) {
        this.mContext = mContext;
        mResolver = mContext.getContentResolver();
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    // Load toan bo file nhac
    public ArrayList<Song> getData() {
        ArrayList<Song> arr = new ArrayList<Song>();
        // Thiết lập URI
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // Thiet lap điều kiện chị lấy file nhạc
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        // Thiet lập các trường sẽ được lấy
        final String[] projection = new String[]{
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };
        // Sắp xếp file nhạc theo tên
        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";
        try {
            Cursor cursor = mResolver.query(uri, projection,selection, null, sortOrder);
            cursor.moveToFirst();
//            Log.d("AZZ",cursor.getCount()+"-----");
            while (!cursor.isAfterLast()) {
                String title = cursor.getString(0);
                String data = cursor.getString(1);
                int duration = cursor.getInt(2);
                Song itemSong = new Song(title, data, duration);
                arr.add(itemSong);
                cursor.moveToNext();
            }
            return arr;
        } catch (Exception ex) {
            Toast.makeText(mContext, "Không tìm thấy file nhạc nào ", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
        return  null;
    }
}