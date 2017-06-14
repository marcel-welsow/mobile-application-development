package com.mojodictive.makeyournode;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class OverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        TextView helloWorld = (TextView) findViewById(R.id.helloWorld);

        helloWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listElementSelected (view);
            }
        });

        View addNodeActionButton = findViewById(R.id.addNodeAction);
        addNodeActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNode();
            }
        });
    }

    private void listElementSelected(View view) {

        String todoName = ((TextView) view).getText().toString();

        Intent detailviewIntent = new Intent(this, DetailviewActivity.class);
        detailviewIntent.putExtra("todoName", todoName);

        startActivity(detailviewIntent);
    }

    private void addNode() {

        Intent detailviewIntent = new Intent(this, DetailviewActivity.class);

        startActivityForResult(detailviewIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String todoName = data.getStringExtra("todoName");
        Toast.makeText(this, "got new todo: " + todoName, Toast.LENGTH_SHORT).show();
    }
}
