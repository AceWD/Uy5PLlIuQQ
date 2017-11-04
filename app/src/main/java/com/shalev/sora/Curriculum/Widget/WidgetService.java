package com.shalev.sora.Curriculum.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by magshimim on 9/29/2017.
 */

public class WidgetService extends RemoteViewsService{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new LoremViewFactory(this.getApplicationContext(), intent));
    }
}
