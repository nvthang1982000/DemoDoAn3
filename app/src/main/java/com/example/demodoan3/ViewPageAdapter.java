package com.example.demodoan3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPageAdapter extends FragmentStatePagerAdapter {
    public ViewPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new videosFragment();
            case 1: return new yeuthichFragment();
            case 2: return new BTAFragment();
            case 3: return new LichSuFragment();
        }
        return new videosFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0: title = "VIDEOS"; break;
            case 1: title = "YÊU THÍCH"; break;
            case 2: title = "BẢN THU ÂM"; break;
            case 3: title = "LỊCH SỬ"; break;
        }
        return title;
    }
}
