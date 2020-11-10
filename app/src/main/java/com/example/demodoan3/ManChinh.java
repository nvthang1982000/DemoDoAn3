package com.example.demodoan3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ManChinh extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_chinh);
        TextView textView = findViewById(R.id.shine);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/VALLERGO.ttf"); // dat fonts
        textView.setTypeface(myTypeface);
        Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        textView.startAnimation(animation);


        final Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ManChinh.this, MainActivity.class);
                startActivity(intent);
                finish();
                hd.removeCallbacks(this);
            }
        },6000);
    }
}