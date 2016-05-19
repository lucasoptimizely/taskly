package com.noregular.taskly.models;

/**
 * Created by lswartsenburg on 5/18/16.
 */
public class Task {
    public static enum Priority {
        LOW, MEDIUM, HIGH
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    private Priority priority;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public Task (String title, Priority priority){
        this.priority = priority;
        this.title = title;
    }
}
