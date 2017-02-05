package com.ishanvadwala.nanodegree01.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ishanvadwala.nanodegree01.DialogFragments.AboutDialogFragment;
import com.ishanvadwala.nanodegree01.Models.MovieListModel;
import com.ishanvadwala.nanodegree01.Adapters.MovieListAdapter;
import com.ishanvadwala.nanodegree01.Utilities.AppController;
import com.ishanvadwala.nanodegree01.R;
import com.ishanvadwala.nanodegree01.DialogFragments.SortDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PopMoviesFragment extends Fragment implements SortDialog.SortDialogChangeListener {
    private RecyclerView recyclerView;
    public static int sortSelector = 1;
    private MovieListAdapter adapter;
    private ArrayList<MovieListModel> movieListModelArrayList;
    private MovieListModel tempMovieListModel;
    private int visibleItemCount,totalItemCount, pastVisibleItems, pageCount;
    private boolean loading;
    private String API_URL,sortParams;
    public static Boolean isConnected=true;
    private MovieSelectedListener movieSelectedListener;

    View view;


    public interface MovieSelectedListener{
        void loadMovieInfo(String movieId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    public void setMovieSelectedListener(MovieSelectedListener listener){
        this.movieSelectedListener = listener;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.popmovies_fragment,container,false);
        pageCount=1;
        if (sortSelector==0){
            sortParams = getString(R.string.voteDesc);
            sortSelector=1;
        }else {
            sortParams = getString(R.string.popularityDesc);

        }

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        movieListModelArrayList = new ArrayList<>();
        final android.support.v7.widget.LinearLayoutManager mLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter =  new MovieListAdapter(movieListModelArrayList, getContext());
        adapter.setListener(new MovieListAdapter.RecyclerViewClickListener() {
            @Override
            public void itemClicked(String movieId) {
                if (movieSelectedListener != null && isConnected){
                    movieSelectedListener.loadMovieInfo(movieId);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if (!loading && ((totalItemCount - pastVisibleItems) <= 6)) {
                    pageCount++;
                    getMovieList(pageCount);
                }
            }
        });
        getMovieList(pageCount);
        return view;
    }


    public void getMovieList(int passedPageCount) {
        this.pageCount = passedPageCount;
        loading = true;
        JSONObject movieListJSONObj = new JSONObject();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath("" + sortParams + "")
                .appendQueryParameter("api_key", getString(R.string.APIKey))
                .appendQueryParameter("page", "" + pageCount);

        API_URL = builder.build().toString();

        JsonObjectRequest volleyStringRequest = new JsonObjectRequest(Request.Method.GET, API_URL, movieListJSONObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            isConnected = true;
                            JSONArray resultJSONArray = response.getJSONArray("results");
                            for (int i = 0; i < resultJSONArray.length(); i++) {
                                tempMovieListModel = new MovieListModel();
                                JSONObject tempJSONObj = resultJSONArray.getJSONObject(i);
                                tempMovieListModel.setName(tempJSONObj.get("original_title").toString());
                                tempMovieListModel.setURL(getString(R.string.moviePosterImageURL) + tempJSONObj.get("poster_path").toString());
                                tempMovieListModel.setId(tempJSONObj.get("id").toString());
                                movieListModelArrayList.add(tempMovieListModel);
                                adapter.notifyDataSetChanged();
                                loading = false;
                            }

                        } catch (Exception E) {
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                isConnected = false;
            }
        }) {
        };

        if (isConnected(getActivity())) {
            AppController.getInstance().addToRequestQueue(volleyStringRequest, "REQ");
        }else if(view != null){
            Snackbar snackbar = Snackbar.make(view,"No internet connection",
                    Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMovieList(pageCount);
                }
            });
            snackbar.show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void sortByPopular() {
        movieListModelArrayList.clear();
        adapter.notifyDataSetChanged();
        sortParams=getString(R.string.popularityDesc);
        pageCount=1;
        getMovieList(pageCount);
        adapter.notifyDataSetChanged();
    }

    public void sortByRatings() {
        movieListModelArrayList.clear();
        adapter.notifyDataSetChanged();
        sortParams=getString(R.string.voteDesc);
        pageCount=1;
        getMovieList(pageCount);
        adapter.notifyDataSetChanged();
    }



   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            AboutDialogFragment aboutDialogFragment = AboutDialogFragment.newInstance();
            aboutDialogFragment.setTargetFragment(PopMoviesFragment.this, 1);
            aboutDialogFragment.show(fm,"about_dialog");
        }
        if (id == R.id.filterSettings) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            SortDialog sortDialog = SortDialog.newInstance("Sort by:!");
            sortDialog.setTargetFragment(PopMoviesFragment.this, 1);
            sortDialog.show(fm, "fragment_alert");

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void optionSelected(int option) {
        if (option == 0){
            sortByPopular();
        }else
            sortByRatings();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

}
