package com.apass;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.text.InputType.TYPE_CLASS_TEXT;

public class ShowRecordActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnShowPass;
    Button btnCopy;
    Button btnBack;
    TextView editPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_record);

        btnShowPass = (Button) findViewById(R.id.btnShowPass);
        btnShowPass.setOnClickListener(this);

        btnCopy = (Button) findViewById(R.id.btnCopy);
        btnCopy.setOnClickListener(this);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        editPass = (TextView) findViewById(R.id.editPass);
        editPass.setOnClickListener(this);
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
