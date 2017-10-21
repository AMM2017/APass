package com.apass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CreateFileActivity  extends AppCompatActivity implements View.OnClickListener {

    Button btnCreateBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_file);

        btnCreateBase = (Button) findViewById(R.id.btnCreateBase);
        btnCreateBase.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateBase:
                this.finish();
                break;
            default:
                break;
        }
    }
}
