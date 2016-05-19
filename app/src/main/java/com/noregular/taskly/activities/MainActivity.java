package com.noregular.taskly.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.noregular.activities.R;
import com.noregular.taskly.adapters.EditDeleteListItemAdapter;
import com.noregular.taskly.models.Task;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<Task> items;
    EditDeleteListItemAdapter itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskly_activity_main);
        lvItems = (ListView) findViewById((R.id.lvlItems));
        items = new ArrayList<>();
        items.add(new Task("Test task", Task.Priority.HIGH));
        //readItems();
        Log.v("Init", "Test");
        itemsAdapter = new EditDeleteListItemAdapter(this, R.layout.taskly_view_list_item, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Task task = new Task(itemText, Task.Priority.MEDIUM);
        items.add(task);
        etNewItem.setText("");
        writeItems();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter,
                                           View item, int pos, long id) {
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
                }
        );
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            List<String> lines = FileUtils.readLines(todoFile);
            items = new ArrayList<Task>();
            for (int i = 0; i < lines.size(); i++) {



            }



        } catch (IOException e) {
            items = new ArrayList<Task>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
