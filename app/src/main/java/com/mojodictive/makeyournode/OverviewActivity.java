package com.mojodictive.makeyournode;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class OverviewActivity extends AppCompatActivity {

    private List<Todo> todos = Arrays.asList(new Todo[]{new Todo("lorem"),new Todo("ipsum"), new Todo("dolor"), new Todo("sed"), new Todo("dispincing"), new Todo("elit")});

    private ListView todoList;
    private ArrayAdapter<Todo> listViewAdapter;

    private class TodoViewHolder {

        public TextView todoTextView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        todoList = (ListView) findViewById(R.id.todoList);

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

        readItemsAndFillListView();
    }

    private void addNode() {

        Intent detailviewIntent = new Intent(this, DetailviewActivity.class);

        startActivityForResult(detailviewIntent, 1);
    }

    private void readItemsAndFillListView() {

        for (Todo todo : todos) {
            addItemToListView(todo);
        }
    }

    private void addItemToListView(Todo todo) {
        listViewAdapter.add(todo);
    }

    private void showDetailView(Todo todo) {

        Intent detailviewIntent = new Intent(this, DetailviewActivity.class);
        detailviewIntent.putExtra(Todo.NAME, todo);

        startActivity(detailviewIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Todo todo = (Todo) data.getSerializableExtra(Todo.NAME);

        addItemToListView(todo);

        Toast.makeText(this, "got new todo: " + todo.getName(), Toast.LENGTH_SHORT).show();
    }
}
