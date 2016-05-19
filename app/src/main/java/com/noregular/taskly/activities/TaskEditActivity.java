package com.noregular.taskly.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.noregular.taskly.R;

/**
 * Created by lswartsenburg on 5/18/16.
 */
public class TaskEditActivity extends AppCompatActivity {
    ListView editView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskly_activity_edit);

    }

}
