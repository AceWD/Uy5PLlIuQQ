package com.shalev.sora.Main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.BatteryManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.shalev.sora.Assistant.mRecognitionListener;
import com.shalev.sora.Constants;
import com.shalev.sora.Curriculum.DaysActivity;
import com.shalev.sora.Curriculum.Fragments.sunFragment;
import com.shalev.sora.Curriculum.Lesson;
import com.shalev.sora.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;

/**
 * Created by magshimim on 10/1/2017.
 */

public class MainActivityFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    private TextView returnedText;
    private ToggleButton toggleButton;
    private FloatingActionButton recordActionButton;


    private mRecognitionListener recognitionListener;

    //private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";

    private FirebaseAuth mAuth;


    private View view;

    private TextView nextLesson;

    private static boolean mAlreadyLoaded;

    private BroadcastReceiver mRecevier = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            float level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            float temp = ((float) level) / 10;
            //returnedText.setText(String.valueOf(temp));
        }
    };

    public static MainActivityFragment newInstance()
    {
        MainActivityFragment fragment = new MainActivityFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_main, container, false);

        Constants.context = getContext();

        //returnedText = (TextView)view.findViewById(R.id.resultText);
        nextLesson = (TextView)view.findViewById(R.id.nextLessonText);
        getActivity().registerReceiver(this.mRecevier, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO}, 10);
        }

        //view.findViewById(R.id.sign_in_button).setOnClickListener(this);
        view.findViewById(R.id.sign_out_button).setOnClickListener(this);



        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.navigation);


        speech = SpeechRecognizer.createSpeechRecognizer(getContext());
        recognitionListener = new mRecognitionListener(speech);
        speech.setRecognitionListener(recognitionListener);


        //returnedText = (TextView) view.findViewById(R.id.resultText);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        toggleButton = (ToggleButton) view.findViewById(R.id.recordButton);
        recordActionButton = (FloatingActionButton)view.findViewById(R.id.recordActionButton);


        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra("android.speech.extra.DICTATION_MODE", true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getContext().getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);


        // startActivity(recognizerIntent);

        recordActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                speech.startListening(recognizerIntent);

            }
        });



        //progressBar.setVisibility(View.INVISIBLE);


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {

                    speech.startListening(recognizerIntent);
                } else {

                    speech.stopListening();
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();

        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null)
                {
                    // signInButton.setVisibility(View.INVISIBLE);
                    // curButton.setVisibility(View.VISIBLE);
                    System.out.println("onAuthStateChanged: signed_in: " + user.getUid());

                }

                else
                {
                    System.out.println("DO something here");
                    //curButton.setVisibility(View.INVISIBLE);
                }
                //updateUI(user);
            }
        };


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }



    private void updateUI()
    {
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch (today)
        {
            case Calendar.SUNDAY:
            {
                sunFragment sun = new sunFragment();
                ArrayList<Lesson> list = sun.getInfoList();

                for(int i = 0; i < list.size(); i++) {
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                    try {
                        Date d = df.parse(list.get(i).time);
                        Calendar temp = Calendar.getInstance();

                        temp.setTime(d);
                        temp.add(Calendar.MINUTE, 45);

                        if(d.after(calendar.getTime()) && d.before(temp.getTime()))
                        {
                            nextLesson.setText("Next Lesson: " + list.get(i++).time);
                        }
                        String endTime = df.format(calendar.getTime());
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }






    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        //Constants.cGoogleApiClient.connect();
        // Google sign out
        Auth.GoogleSignInApi.signOut(Constants.cGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
            //mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
            //mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            //view.findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            view.findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);

        } else {
            //mStatusTextView.setText(R.string.signed_out);
            //mDetailTextView.setText(null);

            //findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_out_button) {
            signOut();
        }
        /*
        else if (i == R.id.sign_out_button) {
            //signIn();
        }
        */
        //else if (i == R.id.disconnect_button) {
        //    revokeAccess();
        //}
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("TAG", "onConnectionFailed:" + connectionResult);
        Toast.makeText(getContext(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
