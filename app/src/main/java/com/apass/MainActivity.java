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

import javax.crypto.BadPaddingException;

import static com.apass.entity.RecordList.fileName;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnCreate;
    Button btnOpen;
    TextView editPass;
    TextView editPassRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //в зависимости от наличия файла загружаем разные layout
        File f = new File(this.getFilesDir(), fileName);
        if (f.exists()) {
            setContentView(R.layout.activity_open_base);
            btnOpen = (Button) findViewById(R.id.btnOpenBase);
            btnOpen.setOnClickListener(this);
            editPass = (TextView) findViewById(R.id.editPassOpen);
        } else {
            setContentView(R.layout.activity_create_base);

            btnCreate = (Button) findViewById(R.id.btnCreateBase);
            btnCreate.setOnClickListener(this);
            editPassRepeat = (TextView) findViewById(R.id.editPassRepeat);
            editPass = (TextView) findViewById(R.id.editPass);
        }
    }


    @Override
    public void onClick(View v) {
        RecordList recordList;
        switch (v.getId()) {
            case R.id.btnCreateBase:
                //проверка на одинаковость паролей
                if (!editPass.getText().toString().equals(editPassRepeat.getText().toString())) {
                    Toast.makeText(this, R.string.pwd_not_match, Toast.LENGTH_SHORT).show();
                    return;
                }
                //проверка на длину пароля
                if (editPass.getText().toString().length() < 4) {
                    Toast.makeText(this, R.string.pwd_short, Toast.LENGTH_SHORT).show();
                    return;
                }
                //создание пустого файла и сохранение его
                recordList = new RecordList();
                recordList.create(this, editPass.getText().toString());
                recordList.save(this);
                Intent intentCreate = new Intent(this, ListActivity.class);
                intentCreate.putExtra("recordList", recordList);
                startActivity(intentCreate);
                finish();
                break;
            case R.id.btnOpenBase:
                try {
                    recordList = new RecordList();
                    recordList.open(this, editPass.getText().toString());
                    Intent intentOpen = new Intent(this, ListActivity.class);
                    intentOpen.putExtra("recordList", recordList);
                    startActivity(intentOpen);
                    finish();
                }
                //если неверный пароль
                catch (BadPaddingException ePadding) {
                    Toast.makeText(this, R.string.pwd_incorrect, Toast.LENGTH_SHORT).show();
                }
                //другие ошибки
                catch (Exception e) {
                    Toast.makeText(this, R.string.wrong_open, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


}
