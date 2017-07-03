package com.mojodictive.makeyournode.model;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class RemoteTodoCRUDOperations implements ITodoCRUDOperations {

    private static final String BASE_URL = "http://10.0.2.2:8080";

    public interface ITodoCRUDWebAPI {

        @POST("/api/todos")
        public Call<Todo> createTodo(@Body Todo todo);

        @GET("/api/todos")
        public Call<List<Todo>> readTodos();

        @GET("/api/todos/{id}")
        public Call<Todo> readTodo(@Path("id") long id);

        @PUT("/api/todos/{id}")
        public Call<Todo> updateTodo(@Path("id") long id, @Body Todo todo);

        @DELETE("/api/todos/{id}")
        public Call<Boolean> deleteTodo(@Path("id") long id);
    }

    private ITodoCRUDWebAPI webAPI;

    public RemoteTodoCRUDOperations() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // create dynamic proxy for retrofit with interface
        webAPI = retrofit.create(ITodoCRUDWebAPI.class);
    }

    @Override
    public Todo createTodo(Todo todo) {

        try {
            return webAPI.createTodo(todo).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Todo> readTodos() {

        try {
            return webAPI.readTodos().execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

        try {
            return webAPI.deleteTodo(id).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
