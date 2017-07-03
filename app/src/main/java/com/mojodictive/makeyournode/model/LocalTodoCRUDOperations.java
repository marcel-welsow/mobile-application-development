package com.mojodictive.makeyournode.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class LocalTodoCRUDOperations implements ITodoCRUDOperations {

    private static final String TABLE = "TODOS";
    private static final String NAME = "NAME";
    private static final String DUEDATE = "DUEDATE";

    private SQLiteDatabase database;

    public LocalTodoCRUDOperations(Context context) {

        database = context.openOrCreateDatabase("mojodictive.todo", Context.MODE_PRIVATE, null);

        if (database.getVersion() == 0) {
            database.setVersion(1);
            database.execSQL("CREATE TABLE " + TABLE + " (ID INTEGER PRIMARY KEY, " + NAME + " TEXT, " + DUEDATE + " INTEGER)");
        }
    }

    @Override
    public Todo createTodo(Todo todo) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, todo.getName());
        contentValues.put(DUEDATE, todo.getDueDate());

        long id = database.insert(TABLE, null, contentValues);

        todo.setId(id);

        return todo;
    }

    @Override
    public List<Todo> readTodos() {

        List<Todo> todos = new ArrayList<Todo>();

        Cursor cursor = database.query(TABLE, new String[]{"ID", NAME, DUEDATE}, null, null, null, null, "ID");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            boolean next = false;

            do {
                Todo todo = new Todo();
                todo.setId(cursor.getLong(cursor.getColumnIndex("ID")));
                todo.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                todo.setDueDate(cursor.getLong(cursor.getColumnIndex(DUEDATE)));

                todos.add(todo);

                next = cursor.moveToNext();
            } while (next);
        }

        return todos;
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

        int numOfRows = database.delete(TABLE, "ID=?", new String[]{String.valueOf(id)});

        return numOfRows > 0;
    }
}
