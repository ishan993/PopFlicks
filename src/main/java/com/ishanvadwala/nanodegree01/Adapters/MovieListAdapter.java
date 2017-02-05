package com.ishanvadwala.nanodegree01.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.ishanvadwala.nanodegree01.Models.MovieListModel;
import com.ishanvadwala.nanodegree01.Utilities.AppController;
import com.ishanvadwala.nanodegree01.R;

import java.util.ArrayList;

/**
 * Created by ishanvadwala on 7/24/16.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder>   {
    ArrayList<MovieListModel> list;
    Context context;

    private  RecyclerViewClickListener listener;

    public interface RecyclerViewClickListener{
        void itemClicked(String movieId);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView networkImageView;
        public TextView movieNameTextview;


        public MyViewHolder(final View view) {
            super(view);
            movieNameTextview=(TextView)view.findViewById(R.id.movieName);
            networkImageView=(NetworkImageView)view.findViewById(R.id.prototypeCellRecyclerView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = getPosition();
                    if (listener != null) {
                        listener.itemClicked(list.get(i).getId());
                    }
                }});
        }

    }

    public MovieListAdapter(ArrayList<MovieListModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (list.get(position).getName()!=null) {
            holder.movieNameTextview.setText(list.get(position).getName());
            holder.networkImageView.setImageUrl(list.get(position).getURL(), AppController.getInstance().getImageLoader());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setListener(RecyclerViewClickListener listener){
        this.listener = listener;
    }



}
