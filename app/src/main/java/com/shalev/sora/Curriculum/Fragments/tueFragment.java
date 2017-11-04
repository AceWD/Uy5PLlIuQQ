package com.shalev.sora.Curriculum.Fragments;

import android.content.Context;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shalev.sora.Curriculum.CuriculumAdapter;
import com.shalev.sora.Curriculum.Lesson;
import com.shalev.sora.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by magshimim on 9/29/2017.
 */

public class tueFragment extends Fragment {

    public static final String TAG = "TuesdayFragment";

    private CuriculumAdapter adapter;

    private ArrayList<Lesson> infoList;

    private ListView list;

    private DatabaseReference mDatabase;

    private boolean isPress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_days, container, false);
        View viewDays = inflater.inflate(R.layout.activity_days, container, false);
        list = (ListView)view.findViewById(R.id.curiculumViewFragment);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //addLessonFab = (FloatingActionButton)getActivity().findViewById(R.id.addLessonFab);

        final AppCompatActivity act = (AppCompatActivity) getActivity();
        if(act.getSupportActionBar() != null)
        {
            Toolbar toolbar = (Toolbar) act.getSupportActionBar().getCustomView();
        }

        infoList = new ArrayList<>();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                submitPost();
            }
        });

        thread.start();

        //System.out.println("SOagrharha " + sunList.get(0).lesson);

        adapter = new CuriculumAdapter(getActivity(), infoList);
        list.setAdapter(adapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                isPress = false;
                list.cancelPendingInputEvents();
                System.out.println("LongPress " + isPress);
                if(isNetworkAvailable())
                {
                    createDialog("Tuesday", Integer.toString(position), infoList.get(position).lesson, infoList.get(position).number, infoList.get(position).homework, true);
                }
                else
                {
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isPress = true;
                    }
                }, 2000);

                return false;
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                System.out.println("onClick outside " + isPress);
                int len = infoList.size();

                if(isPress == true)
                {
                    System.out.println("onClick inside " + isPress);
                    if(position + 1 < len)
                    {
                        Toast.makeText(getContext(), "The lesson ends at: " + infoList.get(position + 1).time, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                        try {
                            Date d = df.parse(infoList.get(position).time);
                            Calendar calendar = Calendar.getInstance();

                            calendar.setTime(d);
                            calendar.add(Calendar.MINUTE, 45);
                            String endTime = df.format(calendar.getTime());

                            Toast.makeText(getContext(), "The lesson ends at: " + endTime, Toast.LENGTH_SHORT).show();


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

        });



        return view;
    }


    private void submitPost()
    {
        infoList.clear();

        mDatabase.child("Days").child("0").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                try
                {


                    for(DataSnapshot postSnapshot : dataSnapshot.child("Tuesday").getChildren())
                    {
                        Lesson lesson = postSnapshot.getValue(Lesson.class);
                        //System.out.println("Blah a: " + lesson.lesson);
                        infoList.add(lesson);
                    }



                } catch (Exception e)
                {
                    System.out.println("Line 264 is the problem");
                    e.printStackTrace();
                    adapter.notifyDataSetChanged();
                }

                isPress = true;

                adapter.notifyDataSetChanged();
                //adapter = new CuriculumAdapter(getActivity(), sunList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Tag", "Error: ", databaseError.toException());
            }
        });
    }

    public void createDialog(final String day, final String lessonNum , final String lesson, String number, String homework, final boolean isLongPress)
    {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext()); //Possibly wont work
        View mView = getActivity().getLayoutInflater().inflate(R.layout.lesson_change_dialog, null);

        TextView mDay = (TextView) mView.findViewById(R.id.change_day_text);

        final EditText mLesson = (EditText) mView.findViewById(R.id.change_lesson_text);
        final EditText mNumber = (EditText) mView.findViewById(R.id.change_number_text);
        final EditText mHomework = (EditText) mView.findViewById(R.id.change_homework_text);
        final EditText mTime = (EditText) mView.findViewById(R.id.change_time_text);

        Button mSubmit = (Button) mView.findViewById(R.id.change_submit_button);
        Button mDelete = (Button) mView.findViewById(R.id.change_delete_button);

        if(isLongPress == true)
        {
            mDelete.setVisibility(View.VISIBLE);
        }
        else
        {
            mDelete.setVisibility(View.GONE);
            //Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        mDay.setText(day);
        mLesson.setText(lesson);
        mNumber.setText(number);
        mHomework.setText(homework);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    mDatabase.child("Days").child("0").child(day).child(lessonNum).child("lesson").setValue(mLesson.getText().toString());
                    mDatabase.child("Days").child("0").child(day).child(lessonNum).child("number").setValue(mNumber.getText().toString());
                    mDatabase.child("Days").child("0").child(day).child(lessonNum).child("homework").setValue(mHomework.getText().toString());
                    mDatabase.child("Days").child("0").child(day).child(lessonNum).child("time").setValue(mTime.getText().toString());


                    Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_SHORT).show();

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    submitPost();
                                }
                            });
                        }
                    });

                    thread.start();
                    isPress = true;
                    dialog.dismiss();

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Days").child("0").child(day).child(lessonNum).removeValue();

                Toast.makeText(getContext(), "Delete Successful", Toast.LENGTH_SHORT).show();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                submitPost();
                            }
                        });
                    }
                });

                thread.start();
                isPress = true;
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int count = list.getCount();
        int id = item.getItemId();

        if (id == R.id.action_add)
        {
            if(isNetworkAvailable())
            {
                createDialog("Tuesday", Integer.toString(count++),"","","", false);
                return true;
            }
            else
            {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }


        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Lesson> getInfoList() {
        return infoList;
    }
}
