package com.mojodictive.makeyournode.model;

import java.util.List;

public interface ITodoCRUDOperationsAsync {

    public static interface CallbackFunction<T> {

        public void process(T result);
    }

    public void createTodo(Todo todo, CallbackFunction<Todo> callback);

    public void readTodos(CallbackFunction<List<Todo>> callback);

    public void readTodo(long id, CallbackFunction<Todo> callback);

    public void updateTodo(long id, Todo todo, CallbackFunction<Todo> callback);

    public void deleteTodo(long id, CallbackFunction<Boolean> callback);
}
