package com.mojodictive.makeyournode;

import android.app.Application;
import android.os.AsyncTask;

import com.mojodictive.makeyournode.model.ITodoCRUDOperations;
import com.mojodictive.makeyournode.model.ITodoCRUDOperationsAsync;
import com.mojodictive.makeyournode.model.RemoteTodoCRUDOperations;
import com.mojodictive.makeyournode.model.Todo;

import java.util.List;

public class CRUDOperationsApplication extends Application implements ITodoCRUDOperationsAsync {

    private ITodoCRUDOperations syncCRUDOperations;

    @Override
    public void onCreate() {
        super.onCreate();

        syncCRUDOperations = new RemoteTodoCRUDOperations();
    }

    public ITodoCRUDOperationsAsync getCRUDOperationsAsync() {
        return this;
    }

    @Override
    public void createTodo(Todo todo, final CallbackFunction<Todo> callback) {

        new AsyncTask<Todo, Void, Todo>() {

            @Override
            protected Todo doInBackground(Todo... params) {
                return syncCRUDOperations.createTodo(params[0]);
            }

            @Override
            protected void onPostExecute(Todo todo) {
                callback.process(todo);
            }
        }.execute(todo);
    }

    @Override
    public void readTodos(final CallbackFunction<List<Todo>> callback) {

        new AsyncTask<Void, Void, List<Todo>>() {

            @Override
            protected List<Todo> doInBackground(Void... params) {
                return syncCRUDOperations.readTodos();
            }

            @Override
            protected void onPostExecute(List<Todo> todos) {
                callback.process(todos);
            }
        }.execute();
    }

    @Override
    public void readTodo(long id, CallbackFunction<Todo> callback) {

    }

    @Override
    public void updateTodo(long id, Todo todo, CallbackFunction<Todo> callback) {

    }

    @Override
    public void deleteTodo(long id, final CallbackFunction<Boolean> callback) {

        new AsyncTask<Long, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Long... params) {
                return syncCRUDOperations.deleteTodo(params[0]);
            }

            @Override
            protected void onPostExecute(Boolean deleted) {
                callback.process(deleted);
            }
        }.execute(id);
    }
}
