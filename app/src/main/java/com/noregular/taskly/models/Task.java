package com.noregular.taskly.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lswartsenburg on 5/18/16.
 */
public class Task {


    private String tid;
    private int priority;
    private String title;

    public Task(){

    }

    public Task (String key, String title, int priority){
        setPriority(priority);
        setTitle(title);
        setTid(key);
    }

    public static class Priority {
        public static final int LOW = 1;
        public static final int MEDIUM = 2;
        public static final int HIGH = 3;
    }


    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }



    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if(priority > 0 && priority < 4) {
            this.priority = priority;
        } else {
            // TODO
            // throw Error();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("tid", getTid());
        result.put("title", getTitle());
        result.put("priority", getPriority());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Task))
            return false;
        if (obj == this)
            return true;

        Task t = (Task) obj;
        return t.getTid() == this.getTid();
    }
}
