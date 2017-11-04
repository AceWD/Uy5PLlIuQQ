package com.shalev.sora.Curriculum.Widget;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by magshimim on 9/29/2017.
 */

public class LoremActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String word = getIntent().getStringExtra(CurrAppWidget.EXTRA_WORD);

        if(word == null)
        {
            word = "We did not get a word";
        }

        Toast.makeText(this, word, Toast.LENGTH_SHORT).show();

        finish();
    }


}
