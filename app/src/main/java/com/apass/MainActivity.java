package com.apass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apass.entity.RecordList;

import java.io.File;

import static com.apass.entity.RecordList.fileName;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnCreate;
    Button btnOpen;
    TextView editPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File f = new File(this.getFilesDir(), fileName);
        if (f.exists()) {
            setContentView(R.layout.activity_open_file);
            btnOpen = (Button) findViewById(R.id.btnOpenBase);
            btnOpen.setOnClickListener(this);
        }
        else {
            setContentView(R.layout.activity_create_file);

            btnCreate = (Button) findViewById(R.id.btnCreateBase);
            btnCreate.setOnClickListener(this);
            editPass = (TextView) findViewById(R.id.editPassRepeat);
            editPass.setOnClickListener(this);
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateBase:
                //new RecordList().create(this, editPass.getText().toString());
                Intent intentCreate = new Intent(this, ListActivity.class);
                startActivity(intentCreate);
                finish();
                break;
            case R.id.btnOpenBase:
                try {
                    //new RecordList().open(this, editPass.getText().toString());
                    Intent intentOpen = new Intent(this, ListActivity.class);
                    startActivity(intentOpen);
                    finish();
                }
                catch (Exception e)
                {
                    Toast.makeText(this, "Wrong open", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }



}
