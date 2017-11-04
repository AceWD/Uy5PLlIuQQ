package com.shalev.sora.Curriculum;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shalev.sora.Curriculum.Fragments.monFragment;
import com.shalev.sora.Curriculum.Fragments.sunFragment;
import com.shalev.sora.Curriculum.Fragments.thuFragment;
import com.shalev.sora.Curriculum.Fragments.tueFragment;
import com.shalev.sora.Curriculum.Fragments.wedFragment;
import com.shalev.sora.R;

import java.io.File;
import java.util.ArrayList;

public class DaysActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static ArrayList<Lesson> sunList;
    private static ArrayList<Lesson> monList;
    private static ArrayList<Lesson> tueList;
    private static ArrayList<Lesson> wedList;
    private static ArrayList<Lesson> thuList;

    private static ArrayList<Lesson> tempList;


    private static ArrayList<String> fileList;

    private static CuriculumAdapter adapter;
    private static CuriculumAdapter monAdapter;

    private static TabLayout tabLayout;

    private static File jsonFile = null;

    private static DatabaseReference mDatabase;

    //private static ListView list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days);

        if(mDatabase == null)
        {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
            mDatabase = database.getReference();
        }



        //updateFab = (FloatingActionButton)findViewById(R.id.updateFab);

        tempList = new ArrayList<>();

        sunList = new ArrayList<>();
        monList = new ArrayList<>();
        tueList = new ArrayList<>();
        wedList = new ArrayList<>();
        thuList = new ArrayList<>();

         Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //submitPost();
            }
        });

        //thread.start();

        mDatabase.keepSynced(true);





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_curr);
        mViewPager.setAdapter(mSectionsPagerAdapter);



        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_days, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            //return PlaceholderFragment.newInstance(position + 1);
            switch (position)
            {
                case 0:
                    sunFragment sunFragment = new sunFragment();
                    //Bundle args = new Bundle();
                    //args.putAll("mDatabase", mDatabase);
                    //sunFragment.setArguments(args);
                    return sunFragment;

                case 1:
                    monFragment monFragment = new monFragment();
                    return monFragment;

                case 2:
                    tueFragment tueFragment = new tueFragment();
                    return tueFragment;

                case 3:
                    wedFragment wedFragment = new wedFragment();
                    return wedFragment;

                case 4:
                    thuFragment thuFragment = new thuFragment();
                    return thuFragment;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {


            switch (position) {
                case 0:

                    return "Sun";
                case 1:

                    return "Mon";
                case 2:
                    return "Tue";

                case 3:
                    return "Wed";
                case 4:
                    return "Thu";
            }
            return null;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        //IMPORTANT
        adapter = new CuriculumAdapter(getApplicationContext(), sunList);
    }
}
