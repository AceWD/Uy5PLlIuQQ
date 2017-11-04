package com.shalev.sora.Curriculum.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.shalev.sora.Curriculum.Lesson;
import com.shalev.sora.Curriculum.Fragments.monFragment;
import com.shalev.sora.Curriculum.Fragments.sunFragment;
import com.shalev.sora.Curriculum.Fragments.thuFragment;
import com.shalev.sora.Curriculum.Fragments.tueFragment;
import com.shalev.sora.Curriculum.Fragments.wedFragment;
import com.shalev.sora.R;

import java.util.ArrayList;

/**
 * Created by magshimim on 9/29/2017.
 */

public class LoremViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private static ArrayList<Lesson> items = null;

    private Context context;
    private int appWigetId;

    public LoremViewFactory(Context context, Intent intent)
    {
        this.context = context;
        appWigetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        Calendar day = Calendar.getInstance();

        if(day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
        {
            items = new sunFragment().getInfoList();
        }

        else if(day.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
        {
            items = new monFragment().getInfoList();
        }

        else if(day.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY)
        {
            items = new tueFragment().getInfoList();
        }

        else if(day.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY)
        {
            items = new wedFragment().getInfoList();
        }

        else if(day.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
        {
            items = new thuFragment().getInfoList();
        }

        else
            {
                items = new sunFragment().getInfoList();
            }

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.list_item_lessons);

        row.setTextViewText(R.id.lesson_list_lesson, items.get(position).lesson);
        row.setTextViewText(R.id.lesson_list_homework, items.get(position).homework);
        row.setTextViewText(R.id.lesson_list_time, items.get(position).time);
        row.setTextViewText(R.id.numberText, items.get(position).number);

        Intent i = new Intent();
        Bundle extras = new Bundle();

        extras.putString(CurrAppWidget.EXTRA_WORD, items.get(position).lesson);
        extras.putString(CurrAppWidget.EXTRA_WORD, items.get(position).homework);
        extras.putString(CurrAppWidget.EXTRA_WORD, items.get(position).time);
        extras.putString(CurrAppWidget.EXTRA_WORD, items.get(position).number);
        i.putExtras(extras);

        row.setOnClickFillInIntent(R.id.lesson_list_lesson, i);

        return (row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
