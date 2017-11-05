package com.shalev.sora.Curriculum;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shalev.sora.R;

import java.util.ArrayList;

/**
 * Created by magshimim on 9/17/2017.
 */

public class CuriculumAdapter extends BaseAdapter{

    private Context mContex;
    private LayoutInflater mInflater;
    private ArrayList<Lesson> mDataSource;
    private static int number;

    public CuriculumAdapter(Context context, ArrayList<Lesson> items)
    {
        mContex = context;
        mDataSource = items;
        mInflater = (LayoutInflater)mContex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.list_item_lessons, null);
        }

        //String temp = Integer.toString(number);

        //View rowView = mInflater.inflate(R.layout.list_item_lessons, parent, false);

        TextView lessonTextView =
                (TextView)convertView.findViewById(com.shalev.sora.R.id.lesson_list_lesson);

        TextView homeworkTextView = (TextView)convertView.findViewById(com.shalev.sora.R.id.lesson_list_homework);
        TextView timeTextView = (TextView)convertView.findViewById(com.shalev.sora.R.id.lesson_list_time);

        TextView numberTextView = (TextView)convertView.findViewById(R.id.numberText);

        ConstraintLayout constraint = (ConstraintLayout)convertView.findViewById(R.id.list_item_layout);

        Lesson lesson = (Lesson)getItem(position);
        //System.out.println("Blah " + lesson.lesson);
        lessonTextView.setText(lesson.lesson);
        homeworkTextView.setText(lesson.homework);
        timeTextView.setText(lesson.time);
        numberTextView.setText(lesson.number);
        //System.out.println("This is the lesson number: " + lesson.number);

        if(position == getCount() - 1)
        {
            //homeworkTextView.setPadding(0,0,0,100);
            convertView.setPadding(0,0,0,100);
        }

        //Handling padding for Break
        //IF the row is not null and it equals to "" that means its a break
        if(lesson.number != null && lesson.lesson.equals("Break"))
        {
            constraint.setBackgroundColor(Color.rgb(66, 134, 244));
            //Puts the break in the middle if there are not homework in there
            if(lesson.homework.equals(""))
            {
                lessonTextView.setPadding(30,130,0,0);
            }
            //Puts padding to match to the rest of the rows
            else
                {
                    lessonTextView.setPadding(30,0,0,0);
                    homeworkTextView.setPadding(30,0,0,0);
                }

        }

        else if(!lesson.lesson.equals("break"))
        {
            constraint.setBackgroundColor(Color.TRANSPARENT);
        }

        //Making sure that stuff that are not break will not get padding
        else
            {
                lessonTextView.setPadding(0,0,0,0);
                homeworkTextView.setPadding(0,0,0,0);
            }

        System.out.println("getCount " + getCount() + "position " + position);




        return convertView; //rowView
    }

    public void refresh(ArrayList<Lesson> items)
    {
        this.mDataSource = items;
        notifyDataSetChanged();
    }
}
