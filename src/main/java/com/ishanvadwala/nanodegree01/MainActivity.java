package com.ishanvadwala.nanodegree01;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ishanvadwala.nanodegree01.Adapters.MovieListAdapter;
import com.ishanvadwala.nanodegree01.Fragments.ViewPagerFragment;
import com.ishanvadwala.nanodegree01.Fragments.MovieInfoFragment;
import com.ishanvadwala.nanodegree01.R;
import com.ishanvadwala.nanodegree01.Utilities.Utility;


/**
 * Created by ishanvadwala on 10/20/16.
 */
public class MainActivity extends AppCompatActivity implements MovieListAdapter.RecyclerViewClickListener {
    private MovieInfoFragment movieInfoFragment;
    private ViewPagerFragment viewPagerFragment;
    private MovieSelectionListener movieSelectionListener;
    private MainActSenderListener mainActSenderListener;

    //Interfaces
    public interface MainActSenderListener{
        void  changeDataSet();
    }

    public interface MovieSelectionListener {
        void getPopMovie(String movieId);
        void getFavMovie(String moveId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check if the device is tablet width or not.
        if (Utility.isTabletWidth(this)) {
            setContentView(R.layout.main_w735dp);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainActivityFragmentHolderLeft, new ViewPagerFragment(), "viewPager");
            ft.commit();
            getSupportFragmentManager().executePendingTransactions();
       }else{
            setContentView(R.layout.main_fragment_holder);
            //If the movieInfo activity was visible before orientation change, reload it.
            movieInfoFragment = (MovieInfoFragment) getSupportFragmentManager().findFragmentByTag("MovieInfoFragment");
            if (savedInstanceState != null && movieInfoFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragmentHolderLow,movieInfoFragment, "MovieInfoFragment").commit();
            }else{
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainActivityFragmentHolderLow, new ViewPagerFragment(), "viewPager");
                ft.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        }
        viewPagerFragment  = (ViewPagerFragment) getSupportFragmentManager().findFragmentByTag("viewPager");
        setViewPagerFragmentListeners();

    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public void setViewPagerFragmentListeners(){
        viewPagerFragment.setMainActTabHolderListener(new ViewPagerFragment.ViewPagerMovieSelectedListener() {
            @Override
            public void popMovieSelected(String movieId) {

                if (Utility.isTabletWidth(MainActivity.this)) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().
                            replace(R.id.mainActivityFragmentHolderRight, new MovieInfoFragment(), "MovieInfoFragment");
                    fragmentTransaction.commit();
                    getSupportFragmentManager().executePendingTransactions();
                    movieSelectionListener.getPopMovie(movieId);
                    movieInfoFragment =(MovieInfoFragment) getSupportFragmentManager().findFragmentByTag("MovieInfoFragment");
                    setMovieInfolistener(movieInfoFragment);


                } else {
                    changeFragment();
                    if (movieSelectionListener != null)
                        movieSelectionListener.getPopMovie(movieId);
                    else
                    setMovieInfolistener(movieInfoFragment);
                }
            }

            @Override
            public void favMovieSelected(String movieId) {
                if(Utility.isTabletWidth(MainActivity.this)){
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().
                            replace(R.id.mainActivityFragmentHolderRight, new MovieInfoFragment(), "MovieInfoFragment");
                    fragmentTransaction.commit();
                    getSupportFragmentManager().executePendingTransactions();
                    movieInfoFragment =(MovieInfoFragment) getSupportFragmentManager().findFragmentByTag("MovieInfoFragment");
                    movieSelectionListener.getFavMovie(movieId);
                    setMovieInfolistener(movieInfoFragment);

                }else {
                    changeFragment();
                    getSupportFragmentManager().executePendingTransactions();
                    movieSelectionListener.getFavMovie(movieId);
                    setMovieInfolistener(movieInfoFragment);
                }
            }
        });
    }
    public void setMovieInfolistener(MovieInfoFragment movieInfoFragment){

        if (movieInfoFragment == null) {
            movieInfoFragment =(MovieInfoFragment) getSupportFragmentManager().findFragmentByTag("MovieInfoFragment");
        }
            movieInfoFragment.setMovieInfoFragmentListener(new MovieInfoFragment.MovieInfoFragmentListener() {
                @Override
                public void changeDataSet() {
                    if (mainActSenderListener != null){
                        mainActSenderListener.changeDataSet();
                    }
                }
            });


    }


    public void setMainActSenderListener(MainActSenderListener mainActSenderListener) {
        this.mainActSenderListener = mainActSenderListener;

    }

    public void setMainFragmentHolderListener(MovieSelectionListener listener) {
        this.movieSelectionListener = listener;
    }



    public void changeFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragmentHolderLow,
                MovieInfoFragment.newInstance(),"MovieInfoFragment").addToBackStack("viewPager").commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void itemClicked(String movieId) {
    }
}
