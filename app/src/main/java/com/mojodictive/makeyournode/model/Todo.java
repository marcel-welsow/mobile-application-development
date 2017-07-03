package com.mojodictive.makeyournode.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Todo implements Serializable {

    public static final String NAME = "todo";

    private long id;
    private String name;

    @SerializedName("expiry")
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
