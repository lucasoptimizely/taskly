package com.noregular.taskly.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.noregular.activities.R;
import com.noregular.taskly.activities.EditActivity;
import com.noregular.taskly.models.Task;

import java.util.ArrayList;

/**
 * Created by lswartsenburg on 5/18/16.
 */
public class EditDeleteListItemAdapter extends ArrayAdapter<Task>  {
    private ArrayList<Task> list = new ArrayList<Task>();
    private Context context;
    private int resource;

    public EditDeleteListItemAdapter(Context context, int resource, ArrayList<Task> list) {
        super(context, resource, list);
        this.list = list;
        this. resource = resource;
        this.context = context;
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
                //do something
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), EditActivity.class);
                v.getContext().startActivity(intent);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}


