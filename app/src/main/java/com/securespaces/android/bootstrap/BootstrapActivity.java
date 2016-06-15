package com.securespaces.android.bootstrap;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BootstrapActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static final int FINAL_FRAGMENT = 2;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments;
    private ArrayList<ImageView> mPageIndicators;
    private Button mProceedButton;
    private int mCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bootstrap);

        mProceedButton = (Button)findViewById(R.id.proceedButton);
        mProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProceedPushed();
            }
        });

        mFragments = new ArrayList<>();
        mFragments.add(new FragmentOne());
        mFragments.add(new FragmentTwo());
        mFragments.add(new FragmentThree());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        mPageIndicators = new ArrayList<>();
        mPageIndicators.add((ImageView)findViewById(R.id.pageIndicator1));
        mPageIndicators.add((ImageView)findViewById(R.id.pageIndicator2));
        mPageIndicators.add((ImageView)findViewById(R.id.pageIndicator3));

        for (int i = 0; i < mPageIndicators.size(); i++) {
            final int x = i;
            mPageIndicators.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(x, true);
                }
            });
        }

    }

    private void setPageIndicatorDots(int oldPosition, int newPosition) {
        mPageIndicators.get(newPosition).setImageDrawable(getDrawable(R.drawable.circle_full));
        mPageIndicators.get(oldPosition).setImageDrawable(getDrawable(R.drawable.circle_empty));
    }

    private void onProceedPushed() {
        if (mCurrentPosition < FINAL_FRAGMENT) {
            mViewPager.setCurrentItem(mCurrentPosition + 1, true);
        } else if (mCurrentPosition == FINAL_FRAGMENT) {
            finish();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setPageIndicatorDots(mCurrentPosition, position);
        mCurrentPosition = position;
        if (position == FINAL_FRAGMENT) {
            mProceedButton.setText(R.string.launch);
        } else {
            mProceedButton.setText(R.string.next);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_bootstrap, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}
