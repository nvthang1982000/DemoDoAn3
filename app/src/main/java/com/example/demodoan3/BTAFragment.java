package com.example.demodoan3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class BTAFragment extends Fragment {

    public BTAFragment() {
        // Required empty public constructor
    }
    ArrayList<VideoYouTube> arrayList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyProvider myProvider = new MyProvider(getActivity());
        // Inflate the layout for this fragment
        arrayList = myProvider.getData();
        Toast.makeText(getActivity(),arrayList.size()+"",Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_b_t_a, container, false);
    }
}