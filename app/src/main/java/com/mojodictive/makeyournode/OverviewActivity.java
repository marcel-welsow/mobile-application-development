package com.mojodictive.makeyournode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mojodictive.makeyournode.model.ITodoCRUDOperations;
import com.mojodictive.makeyournode.model.LocalTodoCRUDOperations;
import com.mojodictive.makeyournode.model.RemoteTodoCRUDOperations;
import com.mojodictive.makeyournode.model.SimpleTodoCRUDOperations;
import com.mojodictive.makeyournode.model.Todo;

import java.util.List;

public class OverviewActivity extends AppCompatActivity {

    private static final int EDIT_TODO = 2;
    private static final int CREATE_TODO = 1;

    private ITodoCRUDOperations todoCRUDOperations;

    private ListView todoList;
    private ArrayAdapter<Todo> listViewAdapter;

    private ProgressDialog progressDialog;

    private class TodoViewHolder {

        public TextView todoTextView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        todoList = (ListView) findViewById(R.id.todoList);

        progressDialog = new ProgressDialog(this);

        View addNodeActionButton = findViewById(R.id.addNodeAction);
        addNodeActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNode();
            }
        });

        listViewAdapter = new ArrayAdapter<Todo>(this, R.layout.todoview_overview){

            @NonNull
            @Override
            public View getView(int position, View todoView, ViewGroup parent) {

                if (todoView == null) {
                    todoView = getLayoutInflater().inflate(R.layout.todoview_overview, null);

                    TodoViewHolder todoViewHolder = new TodoViewHolder();
                    todoViewHolder.todoTextView = (TextView) todoView.findViewById(R.id.todoName);

                    todoView.setTag(todoViewHolder);
                }

                TodoViewHolder todoViewHolder = (TodoViewHolder) todoView.getTag();

                Todo todo = getItem(position);

                try {
                    todoViewHolder.todoTextView.setText(todo.getName());
                } catch (NullPointerException e) {
                    todoViewHolder.todoTextView.setText("");
                }

                return todoView;
            }
        };
        listViewAdapter.setNotifyOnChange(true);
        todoList.setAdapter(listViewAdapter);

        todoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Todo todo = listViewAdapter.getItem(position);

                showDetailView(todo);
            }
        });

//        todoCRUDOperations = new LocalTodoCRUDOperations(this);
        todoCRUDOperations = new RemoteTodoCRUDOperations();

        readItemsAndFillListView();
    }

    private void addNode() {

        Intent detailviewIntent = new Intent(this, DetailviewActivity.class);

        startActivityForResult(detailviewIntent, CREATE_TODO);
    }

    private void readItemsAndFillListView() {

        new AsyncTask<Void, Void, List<Todo>>() {

            @Override
            protected void onPreExecute() {
                progressDialog.show();
                progressDialog.setMessage("list will be created ...");
            }

            @Override
            protected List<Todo> doInBackground(Void... params) {
                return todoCRUDOperations.readTodos();
            }

            @Override
            protected void onPostExecute(List<Todo> todos) {

                progressDialog.hide();

                for (Todo todo : todos) {
                    addItemToListView(todo);
                }
            }
        }.execute();
    }

    private void addItemToListView(Todo todo) {
        listViewAdapter.add(todo);
    }

    private void showDetailView(Todo todo) {

        Intent detailviewIntent = new Intent(this, DetailviewActivity.class);
        detailviewIntent.putExtra(Todo.NAME, todo);

        startActivityForResult(detailviewIntent, EDIT_TODO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CREATE_TODO:
                if (resultCode == Activity.RESULT_OK) createTodoOnActivityResult(data);
                break;
            case EDIT_TODO:
                if (resultCode == DetailviewActivity.RESULT_DELETE) deleteTodoOnActivityResult(data);
                break;
        }
    }

    private void createTodoOnActivityResult(Intent data) {

        Todo todo = (Todo) data.getSerializableExtra(Todo.NAME);

        createAndShowTodo(todo);

        Toast.makeText(this, "got new todo: " + todo.getName(), Toast.LENGTH_SHORT).show();
    }

    private void deleteTodoOnActivityResult(Intent data) {

        Todo todo = (Todo) data.getSerializableExtra(Todo.NAME);

        deleteAndRemoveTodo(todo);

        Toast.makeText(this, "delete todo: " + todo.getName(), Toast.LENGTH_SHORT).show();
    }

    private void createAndShowTodo(Todo todo) {

        new AsyncTask<Todo,Void,Todo>(){

            @Override
            protected void onPreExecute() {
                progressDialog.show();
                progressDialog.setMessage("todo will be created ...");
            }

            @Override
            protected Todo doInBackground(Todo... params) {
                return todoCRUDOperations.createTodo(params[0]);
            }

            @Override
            protected void onPostExecute(Todo todo) {

                addItemToListView(todo);

                progressDialog.hide();
            }
        }.execute(todo);
    }

    private void deleteAndRemoveTodo(Todo todo) {

        new AsyncTask<Todo, Void, Todo>() {

            @Override
            protected void onPreExecute() {
                progressDialog.show();
                progressDialog.setMessage("todo will be deleted ...");
            }

            @Override
            protected Todo doInBackground(Todo... params) {

                todoCRUDOperations.deleteTodo(params[0].getId());

                return params[0];
            }

            @Override
            protected void onPostExecute(Todo todo) {

                listViewAdapter.remove(findTodoInListView(todo.getId()));

                progressDialog.hide();
            }
        }.execute(todo);

//        boolean deleted = todoCRUDOperations.deleteTodo(todo.getId());
//
//        if (deleted) {
//            listViewAdapter.remove(findTodoInListView(todo.getId()));
//        }
    }

    private Todo findTodoInListView(long id) {

        for (int i=0;  i<listViewAdapter.getCount(); i++) {

            if (listViewAdapter.getItem(i).getId() == id) {
                return listViewAdapter.getItem(i);
            }
        }

        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        progressDialog.dismiss();
    }
}
