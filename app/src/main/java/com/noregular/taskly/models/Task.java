package com.noregular.taskly.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lswartsenburg on 5/18/16.
 */
public class Task {


    private String uid;
    private int priority;
    private String title;

    public Task(){

    }

    public Task (String key, String title, int priority){
        setPriority(priority);
        setTitle(title);
        setUid(key);
    }

    public static class Priority {
        public static int LOW = 1;
        public static int MEDIUM = 2;
        public static int HIGH = 3;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
        result.put("uid", getUid());
        result.put("title", getTitle());
        result.put("priority", getPriority());
        return result;
    }
}
