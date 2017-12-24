package com.apass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apass.entity.RecordList;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class DeleteActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnDelete;
    private final Context context = this;
    Intent intent = getIntent();
    static RecordList recordList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);


        Intent intent = getIntent();
        recordList = (RecordList) intent.getSerializableExtra("recordList");


        btnDelete = (Button) findViewById(R.id.btnConfirmDelete);
        btnDelete.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConfirmDelete:
                                //подтверждение удаления
                // запрос ввода пароля для доступа к изменению
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.prompt, null);
                alertDialogBuilder.setView(promptsView);

                final EditText securePass = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(R.string.dialog_btn_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //проверка хешей паролей
                                        MessageDigest digest = null;
                                        try {
                                            digest = MessageDigest.getInstance("SHA-256");
                                        } catch (NoSuchAlgorithmException e) {
                                            e.printStackTrace();
                                        }
                                        byte[] usersha = digest.digest(securePass.getText().toString().getBytes());
                                        if (Arrays.toString(usersha).equals(Arrays.toString(recordList.getSha())))
                                        //если пароли совпадают
                                        {
                                            //удаляем базу, возвращаем результат
                                            recordList.delete(context);
                                            Intent intent = new Intent();
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        }
                                        //если пароли не совпадают
                                        else {
                                            Toast.makeText(context, R.string.pwd_not_match, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                        .setNegativeButton(R.string.dialog_btn_cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                //показ диалогового окна
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }

    }
}
