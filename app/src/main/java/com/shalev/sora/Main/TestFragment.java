package com.shalev.sora.Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shalev.sora.Curriculum.Fragments.DaysFragment;
import com.shalev.sora.R;

/**
 * Created by magshimim on 10/25/2017.
 */

public class TestFragment extends Fragment{

    View view;

    public static TestFragment newInstance()
    {
        TestFragment fragment = new TestFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.activity_test, container, false);

        return view;

    }
}
