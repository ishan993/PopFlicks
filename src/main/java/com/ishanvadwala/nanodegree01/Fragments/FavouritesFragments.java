package com.ishanvadwala.nanodegree01.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ishanvadwala.nanodegree01.Database.SQLiteClass;
import com.ishanvadwala.nanodegree01.Adapters.FavouritesListAdapter;
import com.ishanvadwala.nanodegree01.R;

/**
 * Created by ishanvadwala on 8/28/16.
 */
public class FavouritesFragments extends Fragment {
    SQLiteClass db;
    RecyclerView recyclerView;
    FavouritesListAdapter favouriteAdapter;
    View view;
    private FavFragmentListener favFragmentListener;

    public void setFavFragmentListener(FavFragmentListener favFragmentListener) {
        this.favFragmentListener = favFragmentListener;
    }

    interface FavFragmentListener{
        void bubbleUpFromFavFragment(String movieId);
    }



    @Override
    public void onResume() {
        super.onResume();
        favouriteAdapter.notifyDataSetChanged();
        favouriteAdapter.setFavAdapterSenderListener(new FavouritesListAdapter.FavAdapterSenderListener() {
            @Override
            public void favAdapterSenderMethod(String movieId) {
                if (favFragmentListener != null) {
                    favFragmentListener.bubbleUpFromFavFragment(movieId);
                }
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.popmovies_fragment,container,false);
        db=  SQLiteClass.getDBInstance(getContext());
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        final android.support.v7.widget.LinearLayoutManager mLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        favouriteAdapter = new FavouritesListAdapter(db, getContext());
        recyclerView.setAdapter(favouriteAdapter);


        return view;
    }

}




