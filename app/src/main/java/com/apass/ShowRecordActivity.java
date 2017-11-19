package com.apass;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apass.entity.Record;

public class ShowRecordActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnShowPass;
    Button btnCopy;
    Button btnBack;
    TextView editName;
    TextView editLogin;
    TextView editPass;
    TextView editTextDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_record);


        Intent intent = getIntent();

        Record record = (Record)intent.getSerializableExtra("record");



        btnShowPass = (Button) findViewById(R.id.btnShowPass);
        btnShowPass.setOnClickListener(this);

        btnCopy = (Button) findViewById(R.id.btnCopy);
        btnCopy.setOnClickListener(this);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        editName = (TextView) findViewById(R.id.editName);
        editName.setOnClickListener(this);
        editName.setText(record.getName());

        editLogin = (TextView) findViewById(R.id.editLogin);
        editLogin.setOnClickListener(this);
        editLogin.setText(record.getLogin());

        editPass = (TextView) findViewById(R.id.editPass);
        editPass.setOnClickListener(this);
        editPass.setText(record.getPass());

        editTextDesc = (TextView) findViewById(R.id.editTextDesc);
        editTextDesc.setOnClickListener(this);
        editTextDesc.setText(record.getComment());




        //editPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowPass:
                if (editPass.getInputType()==128)
                    editPass.setInputType(129);
                else editPass.setInputType(128);
                break;
            case R.id.btnCopy:
                ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", editPass.getText().toString());
                clipboard.setPrimaryClip(clip);
                break;
            case R.id.btnBack:
                this.finish();
                break;
            default:
                break;
        }
    }
}
