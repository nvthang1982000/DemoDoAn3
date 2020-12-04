package com.example.demodoan3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatCallback;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Database;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.IOException;
import java.util.ArrayList;


public class PlayVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    final String DATABASE_NAME = "videoYouTube.db";
    SQLiteDatabase SQLDB;
    private MediaRecorder myRecorder;
    private String outputFile;
    private String fileName = "nhacvideo";
    private MediaPlayer myPlayer;

    yeuthichFragment yt = new yeuthichFragment();
    String id = "";
    String title = "";
    String url = "";
    YouTubePlayerView youTubePlayerView;
    ImageView back;
    TextView tenvideo;
    int REQUEST_VIDEO = 12;
    Button Phat;
    ToggleButton ghiam;
    SwitchCompat YeuThich;
    ArrayList<VideoYouTube> arrayYeuThich = new ArrayList<VideoYouTube>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        inits();
        ghiam.setChecked(false);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayVideoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        id = intent.getStringExtra("idVideoYouTubeId");
        title = intent.getStringExtra("idVideoYouTubeTitle");
        url = intent.getStringExtra("idVideoYouTubeUrl");
        tenvideo.setText(title+"");
        youTubePlayerView.initialize(videosFragment.API_KEY, this);
        ArrayList<VideoYouTube>arr=new ArrayList<>();
        arr=readData();
        for(int i = 0; i < arr.size();i++){
            if(arr.get(i).getIdVideo().equals(id)){
                YeuThich.setChecked(true);
                break;
            }
        }
        YeuThich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(YeuThich.isChecked() == true){

                    Toast.makeText(PlayVideoActivity.this,"Đã thêm yêu thích",Toast.LENGTH_SHORT).show();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("IdVideo",id);
                    contentValues.put("Title",title);
                    contentValues.put("Thumbnail",url );

                    SQLiteDatabase database = DataBase.initDatabase(PlayVideoActivity.this, DATABASE_NAME);

                    database.insert("Video",null,contentValues);

                }
                else{
                    Toast.makeText(PlayVideoActivity.this,"Đã bỏ yêu thích",Toast.LENGTH_SHORT).show();
                    SQLiteDatabase database = DataBase.initDatabase(PlayVideoActivity.this, DATABASE_NAME);
                    database.delete("Video","IdVideo = ?",  new String[]{id});
                }

            }
        });



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplication().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                        30);
            } else {
                Log.d("Home", "Already granted access");
//                initializeView(v);
            }
        }
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+fileName+".mp3";
        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myRecorder.setOutputFile(outputFile);
        events();

    }
    private ArrayList<VideoYouTube> readData() {
       SQLiteDatabase database = DataBase.initDatabase(PlayVideoActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("select * from Video",null);
        ArrayList<VideoYouTube>arr=new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i ++){
            cursor.moveToPosition(i);
            String IdVideo = cursor.getString(0);
            String Title = cursor.getString(1);
            String Thumbnail = cursor.getString(2);
            arr.add(new VideoYouTube(Title, Thumbnail, IdVideo));
        }
        return arr;

    }
    private void inits() {
        back = findViewById(R.id.back);
        tenvideo = findViewById(R.id.tenvideo);
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.myYouTube);
        Phat = (Button) findViewById(R.id.play);
        YeuThich = (SwitchCompat) findViewById(R.id.YeuThich);
        ghiam = findViewById(R.id.ghiam);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo(id);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(PlayVideoActivity.this, REQUEST_VIDEO);
        } else {
            Toast.makeText(PlayVideoActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO) {
            youTubePlayerView.initialize(videosFragment.API_KEY, PlayVideoActivity.this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private void events() {
        Phat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    MediaPlayer myPlayer = new MediaPlayer();
                    myPlayer.setDataSource(outputFile);
                    myPlayer.prepare();
                    myPlayer.start();
                    Toast.makeText(getApplicationContext(), "Phát nhạc", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        ghiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ghiam.isChecked() == true){
                    try {
                        myRecorder.prepare();
                        myRecorder.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "Bắt đầu thu", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        myRecorder.stop();
                        myRecorder.release();
                        myRecorder = null;
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "Dừng thu",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 30: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Home", "Permission Granted");
//                    initializeView(v);
                } else {
                    Log.d("Home", "Permission Failed");
                    Toast.makeText(getApplication().getBaseContext(), "You must allow permission record audio to your mobile device.", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
            }
            // Add additional cases for other permissions you may have asked for
        }
    }

}
