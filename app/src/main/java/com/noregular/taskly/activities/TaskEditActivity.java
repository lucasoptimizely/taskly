package com.noregular.taskly.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.noregular.taskly.R;
import com.noregular.taskly.models.Task;

/**
 * Created by lswartsenburg on 5/18/16.
 *
 * TODO check naming conventions throughout app
 */
public class TaskEditActivity extends AppCompatActivity {
    ListView editView;



    private int priority;

    public final static String TASK_KEY = "task_key";

    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskly_activity_edit);

        final EditText taskTitle = (EditText) findViewById(R.id.edit_task_title);
        final Button cancelButton = (Button) findViewById(R.id.cancel_edit);
        final Button saveButton = (Button) findViewById(R.id.save_task);

        String tid;
        database = FirebaseDatabase.getInstance();

        Task task;

        Log.d("TaskEditActivity", "start");
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                tid= null;
            } else {
                tid= extras.getString(TASK_KEY);
            }
        } else {
            tid= (String) savedInstanceState.getSerializable(TASK_KEY);
        }

        FirebaseUser user = checkLoggedInAndRedirect();



        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


        // TODO replace myRef with better variable name throughout app
        final DatabaseReference myRef = database.getReference("user").child(user.getUid()).child("tasks").child(tid);
        myRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        final Task task = dataSnapshot.getValue(Task.class);
                        taskTitle.setText(task.getTitle());

                        saveButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Log.v("prio", priority + "");
                                task.setPriority(priority);
                                String title = taskTitle.getText().toString();
                                task.setTitle(title);
                                myRef.setValue(task);
                                finish();
                            }
                        });

                        priority = task.getPriority();
                        switch (priority){
                            case Task.Priority.LOW:
                                ((RadioButton) findViewById(R.id.radio_low_priority)).setChecked(true);
                                break;
                            case Task.Priority.MEDIUM:
                                ((RadioButton) findViewById(R.id.radio_medium_priority)).setChecked(true);
                                break;
                            case Task.Priority.HIGH:
                                ((RadioButton) findViewById(R.id.radio_high_priority)).setChecked(true);
                                break;
                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("SingleValue", "getUser:onCancelled", databaseError.toException());
                    }
                });



        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.priority_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId) {
                    case R.id.radio_low_priority:
                        priority = Task.Priority.LOW;
                        break;
                    case R.id.radio_medium_priority:
                        priority = Task.Priority.MEDIUM;
                        break;
                    case R.id.radio_high_priority:
                        priority = Task.Priority.HIGH;
                        break;

                }
            }
        });


    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_low_priority:
                if (checked)
                    priority = Task.Priority.LOW;
                    break;
            case R.id.radio_medium_priority:
                if (checked)
                    priority = Task.Priority.MEDIUM;
                    break;
            case R.id.radio_high_priority:
                if (checked)
                    priority = Task.Priority.HIGH;
                    break;

        }
        Log.v("prio", priority + "");
    }

    // TODO Create interface
    public FirebaseUser checkLoggedInAndRedirect(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, AccountLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        return user;
    }
}
