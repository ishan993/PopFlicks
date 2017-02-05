package com.ishanvadwala.nanodegree01.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ishanvadwala.nanodegree01.MainActivity;
import com.ishanvadwala.nanodegree01.R;
import com.ishanvadwala.nanodegree01.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishanvadwala on 8/15/16.
 */
public class ViewPagerFragment extends Fragment {
    private TabLayout tabLayout;
    private android.support.v7.widget.Toolbar tb1;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private PopMoviesFragment popMoviesFragment;
    private FavouritesFragments favouritesFragments;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPagerMovieSelectedListener listener;



    public void setMainActTabHolderListener(ViewPagerMovieSelectedListener listener) {
        this.listener = listener;
    }

    public interface ViewPagerMovieSelectedListener {
        void popMovieSelected(String movieId);

        void favMovieSelected(String movieId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Utility.isTabletWidth(getActivity()))
        setRetainInstance(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        popMoviesFragment.setMovieSelectedListener(new PopMoviesFragment.MovieSelectedListener() {
            @Override
            public void loadMovieInfo(String movieId) {
                if (listener != null) {
                    listener.popMovieSelected(movieId);
                }
            }
        });

        favouritesFragments.setFavFragmentListener(new FavouritesFragments.FavFragmentListener() {
            @Override
            public void bubbleUpFromFavFragment(String movieId) {
                if (listener != null) {
                    listener.favMovieSelected(movieId);
                }
            }
        });


    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        setHasOptionsMenu(false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_fragment, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.tabHolderToolbar);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new PopMoviesFragment(), "Movies");
        viewPagerAdapter.addFragment(new FavouritesFragments(), "Favourites");

        viewPager.setAdapter(viewPagerAdapter);
            popMoviesFragment = (PopMoviesFragment) viewPagerAdapter.getItem(0);
        favouritesFragments = (FavouritesFragments)
                viewPagerAdapter.getItem(1);
        favouritesFragments.setHasOptionsMenu(false);
        favouritesFragments.setMenuVisibility(false);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(R.color.colorPrimaryDark, R.color.colorAccent);
        
        return view;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();



        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}