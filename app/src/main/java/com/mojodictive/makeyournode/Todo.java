package com.mojodictive.makeyournode;

import java.io.Serializable;

public class Todo implements Serializable {

    public static final String NAME = "todo";

    private String name;

    private long dueDate;

    public Todo() {

    }

    public Todo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }
}
