package com.ishanvadwala.nanodegree01.Fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.ishanvadwala.nanodegree01.Database.SQLiteClass;
import com.ishanvadwala.nanodegree01.Adapters.MovieTrailerAdapter;
import com.ishanvadwala.nanodegree01.MainActivity;
import com.ishanvadwala.nanodegree01.Models.FavouriteMovieListModel;
import com.ishanvadwala.nanodegree01.Utilities.AppController;
import com.ishanvadwala.nanodegree01.R;
import com.ishanvadwala.nanodegree01.Utilities.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by ishanvadwala on 7/24/16.
 */
public class MovieInfoFragment extends Fragment{
    private View view;
    private TextView movieYear, moviePlot, movieRating, movieReview;
    private String URL, imgURL;
    private String movieId;
    private RecyclerView recyclerView;
    private NetworkImageView moviePoster, movieBackdrop;
    private SQLiteClass db;
    private ProgressDialog progressDialog;
    public static Boolean  fromFavouriteFragment;
    private ArrayList<String> trailerURLs;
    private MovieTrailerAdapter movieTrailerAdapter;
    private String trailerKey;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton favouritesButton;
    private FavouriteMovieListModel favouriteMovieListModel;
    private MovieInfoFragmentListener movieInfoFragmentListener;

    public static MovieInfoFragment newInstance() {
        MovieInfoFragment movieInfoFragment = new MovieInfoFragment();
        return movieInfoFragment;
    }

    public interface MovieInfoFragmentListener{
        void changeDataSet();
    }

    public void setMovieInfoFragmentListener(MovieInfoFragmentListener movieInfoFragmentListener) {
        this.movieInfoFragmentListener = movieInfoFragmentListener;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db=SQLiteClass.getDBInstance(getContext());
        setHasOptionsMenu(true);
        if (savedInstanceState != null){
            movieId = savedInstanceState.get("movieId").toString();
            if(savedInstanceState.getBoolean("fromFavouriteFragment")) {
                getMovieInfoFromDB(movieId);
            }
            else
                getMovieInfo(movieId);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        checkIfIsFavourite(movieId);
    }


    public void attachListener() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setMainFragmentHolderListener(new MainActivity.MovieSelectionListener() {

            @Override
            public void getPopMovie(String passedMovieId) {
                movieId = passedMovieId;
                getMovieInfo(movieId);
                checkIfIsFavourite(movieId);
                fromFavouriteFragment = false;
            }

            @Override
            public void getFavMovie(String passedMovieId) {
                movieId = passedMovieId;
                getMovieInfoFromDB(movieId);
                checkIfIsFavourite(movieId);
                fromFavouriteFragment = true;
            }
        });
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("movieId", movieId);
        outState.putBoolean("fromFavouritesFragment", fromFavouriteFragment);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.movie_info, container, false);
        attachListener();
        if (!Utility.isTabletWidth(getActivity())) {
            Toolbar toolbar = (Toolbar)view.findViewById(R.id.movieInfoToolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        collapsingToolbarLayout = (CollapsingToolbarLayout)view.findViewById(R.id.htab_collapse_toolbar);

        if(fromFavouriteFragment !=null && !fromFavouriteFragment) {
            trailerURLs = new ArrayList<>();
            movieTrailerAdapter = new MovieTrailerAdapter(trailerURLs, getContext());
            recyclerView = (RecyclerView) view.findViewById(R.id.youtubeRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(movieTrailerAdapter);
        }


        movieReview = (TextView)view.findViewById(R.id.infoActReviewText);
        favouritesButton = (FloatingActionButton)view.findViewById(R.id.floatingFAB);
        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decideFavourite();
            }
        });

        moviePoster = (NetworkImageView)view.findViewById(R.id.movieImage);
        movieYear=(TextView)view.findViewById(R.id.infoActMovieYear);
        moviePlot=(TextView)view.findViewById(R.id.infoActMoviePlot);
        movieRating=(TextView)view.findViewById(R.id.infoActMovieRating);
        movieBackdrop =(NetworkImageView)view.findViewById(R.id.movieBackdrop);
        return view;
    }


    public void getMovieInfoFromDB(String movieId){
        fromFavouriteFragment = true;
        db = SQLiteClass.getDBInstance(getContext());
        favouriteMovieListModel = db.getMovie(movieId);
        collapsingToolbarLayout.setTitle(String.valueOf(favouriteMovieListModel.getMovieName()));
        moviePlot.setText(favouriteMovieListModel.getMoviePlot());
        movieReview.setText(favouriteMovieListModel.getMovieReview());
        movieYear.setText(favouriteMovieListModel.getMovieYear());
        moviePoster.setBackground(getBitmap(favouriteMovieListModel.getMoviePoster()));
        movieBackdrop.setBackground(getBitmap(favouriteMovieListModel.getMovieBackdrop()));
        movieRating.setText(favouriteMovieListModel.getMovieRating());
    }

    public Drawable getBitmap(byte[] image) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        return new BitmapDrawable(getResources(), bitmap);
    }
    public void decideFavourite() {
        if (db.isPresent(movieId)) {
            db.deleteFavourite(movieId);
            favouritesButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            if (fromFavouriteFragment && Utility.isTabletWidth(getActivity()))
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

        } else {
            try {
                Bitmap bitmap = ((BitmapDrawable) moviePoster.getDrawable()).getBitmap();
                Bitmap backdropBitmap = ((BitmapDrawable) movieBackdrop.getDrawable()).getBitmap();
                if (bitmap != null) {
                    //other code to processing bitmap
                    byte[] data = getBitmapAsByteArray(bitmap);
                    byte[] backdrop = getBitmapAsByteArray(backdropBitmap);
                    db.storeMovie(movieId, collapsingToolbarLayout.getTitle().toString(), movieYear.getText().toString(),
                            moviePlot.getText().toString(), movieReview.getText().toString(), data, backdrop, movieRating.getText().toString());
                }
                moviePoster.setImageBitmap(bitmap);
                favouritesButton.setImageResource(R.drawable.ic_favorite_black_24dp);
            }catch (Exception E){
                E.printStackTrace();
            }


        }

        if (movieInfoFragmentListener != null) {
            movieInfoFragmentListener.changeDataSet();
        }
    }




    public void checkIfIsFavourite(String movieId){
        if (db.isPresent(movieId)){
            favouritesButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        }else{
            favouritesButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getMovieInfo(String passedId){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(R.string.progressDialogBoxTitle);
        progressDialog.show();
        JSONObject movieListJSONObj=new JSONObject();
            String API_URL=getString(R.string.baseAPIURL)+passedId+"?api_key="+getString(R.string.APIKey);
            JsonObjectRequest volleyStringRequest = new JsonObjectRequest(Request.Method.GET, API_URL, movieListJSONObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        URL = getString(R.string.moviePosterImageURL)+response.get("poster_path");
                        imgURL =  getString(R.string.movieBackdropImageURL)+response.get("backdrop_path");
                        done();
                        collapsingToolbarLayout.setTitle(response.get("title").toString());
                        moviePlot.setText(response.get("overview").toString());
                        movieRating.setText(getString(R.string.Rating)+":"+response.get("vote_average").toString());
                        movieYear.setText(getString(R.string.releaseYear)+response.get("release_date").toString().substring(0,4));
                    } catch (Exception E) {
                    E.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {


            };
        AppController.getInstance().addToRequestQueue(volleyStringRequest, "REQ");

        String API_URL_Video=getString(R.string.baseAPIURL)+passedId+
                "/videos?api_key="+getString(R.string.APIKey);

        JsonObjectRequest volleyMovieTrailerRequest = new JsonObjectRequest(Request.Method.GET,
                API_URL_Video, movieListJSONObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressDialog.hide();
                    JSONArray tempJSONArray =response.getJSONArray("results");

                    for (int i= 0; i<tempJSONArray.length();i++) {
                        trailerKey = tempJSONArray.getJSONObject(i).get("key").toString();
                        trailerURLs.add(trailerKey);
                        movieTrailerAdapter.notifyDataSetChanged();
                    }
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
        };

        AppController.getInstance().addToRequestQueue(volleyMovieTrailerRequest);
        String API_URL_Reviews=getString(R.string.baseAPIURL)
                +passedId+"/reviews?api_key="+getString(R.string.APIKey);
        JsonObjectRequest volleyReviewRequest = new JsonObjectRequest(Request.Method.GET,
                API_URL_Reviews, movieListJSONObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray temp =  response.getJSONArray("results");
                    movieReview.setText("Reviews: "+temp.getJSONObject(0).get("content"));
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {


        };
        AppController.getInstance().addToRequestQueue(volleyReviewRequest);
        }


    public void done(){
        try {
            moviePoster.setImageUrl(URL, AppController.getInstance().getImageLoader());
            movieBackdrop.setImageUrl(imgURL, AppController.getInstance().getImageLoader());
        }catch (Exception E){
            E.printStackTrace();
        }
        if (progressDialog != null)
            progressDialog.hide();
    }

    public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}

