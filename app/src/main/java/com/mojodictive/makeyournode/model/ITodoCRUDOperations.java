package com.mojodictive.makeyournode.model;

import java.util.List;

public interface ITodoCRUDOperations {

    public Todo createTodo(Todo todo);

    public List<Todo> readTodos();

    public Todo readTodo(long id);

    public Todo updateTodo(long id, Todo todo);

    public boolean deleteTodo(long id);
}
