package com.mojodictive.makeyournode.model;

import java.util.Arrays;
import java.util.List;

public class SimpleTodoCRUDOperations implements ITodoCRUDOperations {

    @Override
    public Todo createTodo(Todo todo) {

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return todo;
    }

    @Override
    public List<Todo> readTodos() {
        return Arrays.asList(new Todo[]{new Todo("lorem"), new Todo("something")});
    }

    @Override
    public Todo readTodo(long id) {
        return null;
    }

    @Override
    public Todo updateTodo(long id, Todo todo) {
        return null;
    }

    @Override
    public boolean deleteTodo(long id) {
        return false;
    }
}
