package com.apass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnCreate;
    Button btnOpen;
    Button btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(this);
        btnOpen = (Button) findViewById(R.id.btnOpen);
        btnOpen.setOnClickListener(this);
        btnJoin = (Button) findViewById(R.id.btnJoin);
        btnJoin.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreate:
                Intent intentCreate = new Intent(this, CreateFileActivity.class);
                startActivity(intentCreate);
                break;
            case R.id.btnOpen:
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
                break;
            case R.id.btnJoin:
                Intent intentJoin = new Intent(this, ListActivity.class);
                startActivity(intentJoin);
                break;
            default:
                break;
        }
    }



}
