package com.shalev.sora.Assistant;

import android.app.Activity;
import android.speech.tts.TextToSpeech;

import com.shalev.sora.Constants;
import com.shalev.sora.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Random;

/**
 * Created by magshimim on 9/16/2017.
 */

public class ResultManager{ //extends Activity{

    private TextToSpeech voice;
    //private String command;

    private JSONObject obj = null;

    public ResultManager() {

        try {
            obj = new JSONObject(loadJSON());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(obj != null)
        {
            System.out.println(obj);
        }


        voice = new TextToSpeech(Constants.context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    voice.setLanguage(Locale.US);
                }
            }
        });





    }

    public void speak(String sentence)
    {
        voice.speak(sentence, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void addJson(String sentence)
    {
        if(!obj.has(sentence))
        {
            String [] words = sentence.split(" ");

            if(words[0].equals("add"))
            {

            }

            JSONObject temp = new JSONObject();
            JSONArray arr = new JSONArray();

            try {
                temp.put("test","hello");
                arr.put(temp);
                obj.put(sentence, arr);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void reply(String sentence)
    {
        try {

            if(obj.has(sentence))
            {
                int count = obj.getJSONObject(sentence).length();
                Random rand = new Random();
                int i = rand.nextInt(count) + 1;
                String j = Integer.toString(i);
                System.out.println("The number is: " + j);
                System.out.println(obj.getJSONObject(sentence).getString(j));
                speak(obj.getJSONObject(sentence).getString(j));
            }
            else
                {
                    speak(sentence);
                }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSON()
    {
        String json = null;

        try
        {
            InputStream is = Constants.context.getResources().openRawResource(R.raw.reply);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
            System.out.println(json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        return json;
    }

}
