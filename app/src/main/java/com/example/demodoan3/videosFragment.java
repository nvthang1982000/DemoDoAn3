package com.example.demodoan3;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class videosFragment extends Fragment {
    public static String API_KEY = "AIzaSyC5XDmStzn0S3lDMEtUXPrZANDHq4ZMh1s";
    String ID_PLAYLIST = "PLHcHRgq0F87kwIOLtVb1zXUsRyOGpdOFN";
    String urlGetJson = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+ID_PLAYLIST+"&key="+API_KEY+"&maxResults=50";
    ListView lvVideo;

    ArrayList<VideoYouTube> arrayVideo;
    VideoYouTubeAdapter adapter;
    public videosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos, container, false);

        lvVideo = (ListView) view.findViewById(R.id.list_videos);
        arrayVideo = new ArrayList<>();
        adapter = new VideoYouTubeAdapter(getActivity(),R.layout.row_video_youtube,arrayVideo);
        lvVideo.setAdapter(adapter);
        GetJsonYouTube(urlGetJson);
        lvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), PlayVideoActivity.class);
                intent.putExtra("idVideoYouTubeId", arrayVideo.get(i).getIdVideo());
                intent.putExtra("idVideoYouTubeTitle", arrayVideo.get(i).getTitle());
                intent.putExtra("idVideoYouTubeUrl", arrayVideo.get(i).getThumbnail());
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    private void GetJsonYouTube(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonItems = response.getJSONArray("items");

                            String title = ""; String url = ""; String idVideo="";
                            for (int i = 0; i<jsonItems.length();i++){
                                JSONObject jsonItem = jsonItems.getJSONObject(i);

                                JSONObject jsonSnippet = jsonItem.getJSONObject("snippet");
                                title = jsonSnippet.getString("title");

                                JSONObject jsonThumbnail = jsonSnippet.getJSONObject("thumbnails");
                                JSONObject jsonMedium = jsonThumbnail.getJSONObject("medium");
                                url = jsonMedium.getString("url");
                                JSONObject jsonResourceID = jsonSnippet.getJSONObject("resourceId");
                                idVideo = jsonResourceID.getString("videoId");
                                arrayVideo.add(new VideoYouTube(title, url, idVideo));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Lỗi",Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }
}