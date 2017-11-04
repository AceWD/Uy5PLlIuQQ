package com.shalev.sora.Curriculum;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.shalev.sora.Constants;
import com.shalev.sora.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by magshimim on 9/17/2017.
 */

@IgnoreExtraProperties
public class Lesson{

    public String lesson;
    public String time;
    public String homework;
    public String number;

    //private static JSONArray lessonsArr;

    public static ArrayList<Lesson> getLessonsFromFile(String fileName, Context context, String day)
    {
        final ArrayList<Lesson> lessonList = new ArrayList<>();

        try
        {
            String jsonString = loadJSON(fileName);
            JSONObject json = new JSONObject(jsonString);
            JSONArray lessons = json.getJSONArray(day);
            //lessonsArr = lessons;

            for(int i = 0; i < lessons.length(); i++)
            {
                Lesson lesson = new Lesson();

                lesson.lesson = lessons.getJSONObject(i).getString("lesson");
                lesson.time = lessons.getJSONObject(i).getString("time");
                lesson.homework = lessons.getJSONObject(i).getString("homework");
                lesson.number = lessons.getJSONObject(i).getString("number");

                lessonList.add(lesson);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lessonList;
    }

    @Exclude
    public static String loadJSON(String fileName)
    {
        String json = null;

        try
        {
            //Constants.context.openFileInput(fileName); //
            InputStream is = Constants.context.getResources().openRawResource(R.raw.curiculum);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
            //System.out.println(json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        //System.out.println("THis is the json: " + json);
        return json;
    }

    @Exclude
    public static void mocdifyJSON(String fileName, String key, String value, int position, String day)
    {
        try {

            String jsonString = loadJSON(fileName);
            JSONObject json = new JSONObject(jsonString);
            JSONArray lessons = json.getJSONArray(day);

            //lessons.get(position).
            //((JSONObject)lessons.get(position)).remove();
            ((JSONObject)lessons.get(position)).put("lesson",value);

            //String jsonString = loadJSON(fileName);
            //JSONObject json = new JSONObject(jsonString);
            //JSONArray lessons = json.getJSONArray("Sunday");

            FileOutputStream fos = Constants.context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(lessons.toString().getBytes());
            fos.close();

            //startupJsonWrite(fileName, lessons);
            System.out.println(((JSONObject)lessons.get(position)));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Exclude
    public static void startupJsonWrite(String fileName, JSONArray jsonArray)
    {
        //String FILENAME = "curi.json";

        //String jsonString = loadJSON(fileName);
        //System.out.println("Blahadsasfa " + jsonString);
        try {

            String jsonString = loadJSON(fileName);
            //JSONObject json = new JSONObject(jsonString);
            //JSONArray lessons = json.getJSONArray("Sunday");

            FileOutputStream fos = Constants.context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(jsonString.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
