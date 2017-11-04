package com.shalev.sora.Assistant;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import com.shalev.sora.Constants;

import java.util.ArrayList;

/**
 * Created by magshimim on 9/13/2017.
 */

public class mRecognitionListener implements RecognitionListener{

    //private TextView returnedText;

    private SpeechRecognizer speech = null;
    private Intent recognizerIntent = null;

    private ResultManager manager;

    public mRecognitionListener(SpeechRecognizer speech) {
        this.speech = speech;

        manager = new ResultManager();

    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        text = matches.get(0);
        //for (String result : matches)
        //    text += result + "\n";
        if(text.equals("hello"))
        {
            System.out.println(text);
            Toast.makeText(Constants.context, text, Toast.LENGTH_SHORT).show();
        }

        manager.reply(text);
        //manager.speak(text);

    }


    @Override
    public void onBeginningOfSpeech() {

    }
    @Override
    public void onBufferReceived(byte[] buffer) {
    }
    @Override
    public void onEndOfSpeech() {
    }
    @Override
    public void onError(int errorCode) {
        String message;

        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                //message = R.string.error_audio_error;
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                //message = R.string.error_client;
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                //message = R.string.error_permission;
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                //message = R.string.error_network;
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                //message = R.string.error_timeout;
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                //message = R.string.error_no_match;
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                //message = R.string.error_busy;
                break;
            case SpeechRecognizer.ERROR_SERVER:
                //message = R.string.error_server;
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                //message = R.string.error_timeout;
                break;
            default:
                //message = R.string.error_understand;
                break;
        }
    }
    @Override
    public void onEvent(int arg0, Bundle arg1) {
    }
    @Override
    public void onPartialResults(Bundle arg0) {
    }
    @Override
    public void onReadyForSpeech(Bundle arg0) {
    }


    @Override
    public void onRmsChanged(float rmsdB) {
        if(rmsdB == 0)
        {
            speech.stopListening();
        }
    }
}
