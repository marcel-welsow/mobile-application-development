package com.mojodictive.makeyournode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.mojodictive.makeyournode.model.Todo;

public class DetailviewActivity extends AppCompatActivity {

    public static final int RESULT_DELETE = 10;

    private TextView todoNameView;
    private Button saveButton;
    private Todo todo;

    @Override
    public void onCreate(Bundle savedInstancedState) {

        super.onCreate(savedInstancedState);
        setContentView(R.layout.activity_detailview);

        todo = (Todo) getIntent().getSerializableExtra(Todo.NAME);

        todoNameView = (TextView) findViewById(R.id.todoName);

        if (todo != null) {
            todoNameView.setText(todo.getName());
        }
    }

    private void saveNode() {

        Intent returnIntent = new Intent();
        String todoName = todoNameView.getText().toString();

        Todo todo = new Todo(todoName);

        returnIntent.putExtra(Todo.NAME, todo);

        setResult(Activity.RESULT_OK, returnIntent);

        finish();
    }

    private void deletenNode() {

        Intent returnIntent = new Intent();
        returnIntent.putExtra(Todo.NAME, todo);

        setResult(RESULT_DELETE, returnIntent);

        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_overview, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.saveButton:
                saveNode();
                break;
            case R.id.deleteButton:
                deletenNode();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
