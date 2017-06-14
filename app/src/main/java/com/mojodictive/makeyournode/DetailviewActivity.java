package com.mojodictive.makeyournode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailviewActivity extends AppCompatActivity {

    private TextView todoNameView;
    private Button saveButton;

    @Override
    public void onCreate(Bundle savedInstancedState) {

        super.onCreate(savedInstancedState);
        setContentView(R.layout.activity_detailview);

        String todoName = getIntent().getStringExtra("todoName");

        todoNameView = (TextView) findViewById(R.id.todoName);

        if (todoName != null) {
            todoNameView.setText(todoName);
        }

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                saveNode();
            }
        });
    }

    private void saveNode() {
        Intent returnIntent = new Intent();
        String todoName = todoNameView.getText().toString();
        returnIntent.putExtra("todoName", todoName);

        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
