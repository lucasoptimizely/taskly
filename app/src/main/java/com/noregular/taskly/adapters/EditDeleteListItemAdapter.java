package com.noregular.taskly.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noregular.taskly.R;
import com.noregular.taskly.activities.TaskEditActivity;
import com.noregular.taskly.models.Task;

import java.util.ArrayList;

/**
 * TODO move all data syncing logic to this class instead of the activity.
 * Created by lswartsenburg on 5/18/16.
 */
public class EditDeleteListItemAdapter extends ArrayAdapter<Task>  {
    private ArrayList<Task> list = new ArrayList<Task>();
    private Context context;
    private int resource;
    FirebaseDatabase database;

    public EditDeleteListItemAdapter(Context context, int resource, ArrayList<Task> list) {
        super(context, resource, list);
        this.list = list;
        this.resource = resource;
        this.context = context;
        this.database =  FirebaseDatabase.getInstance();
    }

    public void removeTask(int position){
        //do something
        String uid = list.get(position).getUid();
        list.remove(position); //or some other task
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            DatabaseReference myRef = database.getReference("user").child(user.getUid()).child("tasks").child(uid);;
            myRef.removeValue();
        }

        notifyDataSetChanged();
    }

    public void editTask(){
        //TODO
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(this.resource, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position).getTitle());

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
        Button editBtn = (Button)view.findViewById(R.id.edit_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                removeTask(position);
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), TaskEditActivity.class);
                v.getContext().startActivity(intent);
                notifyDataSetChanged();
            }
        });

        return view;
    }

}


