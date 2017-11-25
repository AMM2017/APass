package com.apass;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apass.entity.Record;
import com.apass.entity.RecordList;


public class AddChangeActivity extends AppCompatActivity implements View.OnClickListener {

    Button brnCreate;
    TextView editName;
    TextView editLogin;
    TextView editPass;
    TextView editTextDesc;

    RecordList recordList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_change);

        Intent intent = getIntent();
        recordList = (RecordList)intent.getSerializableExtra("recordList");

        brnCreate = (Button) findViewById(R.id.btnCreateBase);
        brnCreate.setOnClickListener(this);

        editName = (TextView) findViewById(R.id.editName);
        editName.setOnClickListener(this);

        editLogin = (TextView) findViewById(R.id.editLogin);
        editLogin.setOnClickListener(this);

        editPass = (TextView) findViewById(R.id.editPass);
        editPass.setOnClickListener(this);

        editTextDesc = (TextView) findViewById(R.id.editTextDesc);
        editTextDesc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btnCreateBase:
                recordList.add(new Record(editName.getText().toString(),
                        editLogin.getText().toString(),
                        editPass.getText().toString(),
                        editTextDesc.getText().toString()));
                setResult(RESULT_OK, intent);
                intent.putExtra("recordList", recordList);
                this.finish();
                break;
            default:
                break;
        }
    }
}
