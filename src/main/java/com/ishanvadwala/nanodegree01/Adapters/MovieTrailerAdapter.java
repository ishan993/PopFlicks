package com.ishanvadwala.nanodegree01.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ishanvadwala.nanodegree01.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;

/**
 * Created by ishanvadwala on 8/27/16.
 */
public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MyViewHolder>   {
    ArrayList<String> trailerURLs;
    ImageView playButton;
    Context context;




    public class MyViewHolder extends RecyclerView.ViewHolder {
         YouTubeThumbnailView youTubeThumbnailView;

        public MyViewHolder(View view) {
            super(view);
            playButton = (ImageView) view.findViewById(R.id.btnYoutube_player);
            youTubeThumbnailView = (YouTubeThumbnailView) view.findViewById(R.id.youtube_thumbnail);
        }
    }

    public MovieTrailerAdapter(ArrayList<String> trailerURLs, Context context) {
        this.context = context;
        this.trailerURLs = trailerURLs;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_youtube, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
try {
    playButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context,
                    String.valueOf(R.string.youtubeAPIKey),
                    trailerURLs.get(position),//video id
                    100,     //after this time, video will start automatically
                    true,               //autoplay or not
                    false);             //lightbox mode or not; show the video in a small box
            context.startActivity(intent);
        }
    });
}catch (Exception E){
    Log.d("Exception",  E.toString());
}


        if (trailerURLs.get(position)!=null) {
          holder.youTubeThumbnailView.initialize(String.valueOf(R.string.youtubeAPIKey), new YouTubeThumbnailView.OnInitializedListener() {
              @Override
              public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {

               youTubeThumbnailLoader.setVideo(trailerURLs.get(position));
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();


                }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });

              }

              @Override
              public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

              }
          });
        }

    }

    @Override
    public int getItemCount() {
        return trailerURLs.size();
    }
}
