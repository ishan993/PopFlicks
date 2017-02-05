package com.ishanvadwala.nanodegree01.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishanvadwala.nanodegree01.Database.SQLiteClass;
import com.ishanvadwala.nanodegree01.MainActivity;
import com.ishanvadwala.nanodegree01.Models.FavouriteMovieListModel;
import com.ishanvadwala.nanodegree01.R;

import java.util.ArrayList;

/**
 * Created by ishanvadwala on 8/28/16.
 */
public class FavouritesListAdapter extends RecyclerView.Adapter<FavouritesListAdapter.MyViewHolder> {
    ArrayList<FavouriteMovieListModel> list;
    Context context;
    SQLiteClass db;
    private FavAdapterSenderListener favAdapterSenderListener;

    public interface FavAdapterSenderListener {
        void favAdapterSenderMethod(String movieId);
    }



    public void setFavAdapterSenderListener(FavAdapterSenderListener FavAdapterSenderListener) {
        this.favAdapterSenderListener = FavAdapterSenderListener;
        final MainActivity act = (MainActivity) context;
        act.setMainActSenderListener(new MainActivity.MainActSenderListener() {
            @Override
            public void changeDataSet() {
                if (favAdapterSenderListener != null) {
                    list = db.getMovieList();
                    notifyDataSetChanged();
                }
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView movieNameTextview;


        public MyViewHolder(View view) {
            super(view);
            movieNameTextview = (TextView) view.findViewById(R.id.favouritesMovieName);
            imageView = (ImageView) view.findViewById(R.id.prototypeImageRecyclerView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (favAdapterSenderListener != null){
                        favAdapterSenderListener.favAdapterSenderMethod(list.get(getPosition()).getMovieId());

                    }
                }
            });
        }
    }

    public FavouritesListAdapter(SQLiteClass db, Context context) {
        this.db = db;
        list = db.getMovieList();
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_favourites, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        byte[] temp = list.get(position).getMoviePoster();
        Bitmap bitmap = getBitmap(temp);
        holder.movieNameTextview.setText(list.get(position).getMovieName());
        holder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public Bitmap getBitmap(byte[] image) {
         Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        return bitmap;
    }

}