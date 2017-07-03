package com.mojodictive.makeyournode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

import com.mojodictive.makeyournode.model.ITodoCRUDOperationsAsync;

import com.mojodictive.makeyournode.model.Todo;

import java.util.List;

public class OverviewActivity extends AppCompatActivity {

    private static final int EDIT_TODO = 2;
    private static final int CREATE_TODO = 1;

    private ITodoCRUDOperationsAsync todoCRUDOperations;

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
        todoCRUDOperations = ((CRUDOperationsApplication) getApplication()).getCRUDOperationsAsync();

        readItemsAndFillListView();
    }

    private void addNode() {

        Intent detailviewIntent = new Intent(this, DetailviewActivity.class);

        startActivityForResult(detailviewIntent, CREATE_TODO);
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

    private void readItemsAndFillListView() {

        progressDialog.show();
        progressDialog.setMessage("list will be created ...");

        todoCRUDOperations.readTodos(new ITodoCRUDOperationsAsync.CallbackFunction<List<Todo>>() {
            @Override
            public void process(List<Todo> todos) {

                progressDialog.hide();

                for (Todo todo : todos) {
                    addItemToListView(todo);
                }
            }
        });
    }

    private void createAndShowTodo(Todo todo) {

        progressDialog.show();
        progressDialog.setMessage("todo will be created ...");

        todoCRUDOperations.createTodo(todo, new ITodoCRUDOperationsAsync.CallbackFunction<Todo>() {

            @Override
            public void process(Todo todo) {
                addItemToListView(todo);

                progressDialog.hide();
            }
        });
    }

    private void deleteAndRemoveTodo(final Todo todo) {

        progressDialog.show();
        progressDialog.setMessage("todo will be deleted ...");

        todoCRUDOperations.deleteTodo(todo.getId(), new ITodoCRUDOperationsAsync.CallbackFunction<Boolean>() {

            @Override
            public void process(Boolean deleted) {
                if (deleted) listViewAdapter.remove(findTodoInListView(todo.getId()));

                progressDialog.hide();
            }
        });
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
