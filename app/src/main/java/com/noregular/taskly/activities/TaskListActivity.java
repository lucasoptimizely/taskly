package com.noregular.taskly.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noregular.taskly.R;
import com.noregular.taskly.adapters.EditDeleteListItemAdapter;
import com.noregular.taskly.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskListActivity extends AppCompatActivity {
    ArrayList<Task> items;
    EditDeleteListItemAdapter itemsAdapter;
    ListView lvItems;
    FirebaseDatabase database;
    ChildEventListener taskListener;
    private static String TAG = "TaskListActivity";
    DatabaseReference taskReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskly_activity_main);
        database =  FirebaseDatabase.getInstance();

        lvItems = (ListView) findViewById((R.id.lvlItems));
        items = new ArrayList<>();

        itemsAdapter = new EditDeleteListItemAdapter(this, R.layout.taskly_view_list_item, items);

        lvItems.setAdapter(itemsAdapter);

        taskListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                // Get Post object and use the values to update the UI
                Log.w(TAG, "loadPost:onAdded " + dataSnapshot.getKey());
                Task task = dataSnapshot.getValue(Task.class);
                items.add(task);
                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.w(TAG, "loadPost:onCancelled " + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.w(TAG, "loadPost:onRemoved " + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }
        };
        FirebaseUser user = checkLoggedInAndRedirect();
        if(user != null) {
            taskReference = database.getReference("user").child(user.getUid()).child("tasks");
            taskReference.addChildEventListener(taskListener);
        }


    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);

        FirebaseUser user = checkLoggedInAndRedirect();
        if(user != null) {
            DatabaseReference myRef = database.getReference("user").child(user.getUid());
            String key = myRef.child("tasks").push().getKey();

            String itemText = etNewItem.getText().toString();
            Task task = new Task(key, itemText, Task.Priority.MEDIUM);


            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/tasks/" + key, task.toMap());
            myRef.updateChildren(childUpdates);

            etNewItem.setText("");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        return true;//return true so that the menu pop up is opened

    }

    public void logout(){
        taskReference.removeEventListener(taskListener);
        FirebaseAuth.getInstance().signOut();
        goBackToLogin();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout_button:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public FirebaseUser checkLoggedInAndRedirect(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, AccountLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        return user;
    }

    public void goBackToLogin(){
        Intent intent = new Intent(this, AccountLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLoggedInAndRedirect();
    }
}
