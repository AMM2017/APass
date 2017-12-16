package com.apass;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.apass.entity.Record;
import com.apass.entity.RecordList;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton secure;

    ImageButton copyLogin;
    ImageButton copyPass;
    ImageButton generator;
    Button btnAction;

    EditText name;
    EditText login;
    TextInputEditText etPass;
    EditText textDesc;

    String recordName;
    Record record;
    RecordList recordList;
    //добавление новой или изменение/просмотр существующей
    boolean isAdd;

    boolean editable = false;

    private final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Intent intent = getIntent();
        recordName = (String) intent.getSerializableExtra("recordName");
        recordList = (RecordList) intent.getSerializableExtra("recordList");
        isAdd = (boolean) intent.getSerializableExtra("isAdd");

        secure = (ImageButton) findViewById(R.id.ibtnSecure);
        secure.setOnClickListener(this);

        copyLogin = (ImageButton) findViewById(R.id.ibtnCopyLogin);
        copyLogin.setOnClickListener(this);

        copyPass = (ImageButton) findViewById(R.id.ibtnCopyPass);
        copyPass.setOnClickListener(this);

        generator = (ImageButton) findViewById(R.id.ibtnGenerator);
        generator.setOnClickListener(this);

        btnAction = (Button) findViewById(R.id.btnAction);
        btnAction.setOnClickListener(this);

        name = (EditText) findViewById(R.id.editName);

        login = (EditText) findViewById(R.id.editLogin);

        etPass = (TextInputEditText) findViewById(R.id.etPass);

        textDesc = (EditText) findViewById(R.id.editTextDesc);

        //изменение вида activity
        if (isAdd) {
            secure.setVisibility(View.GONE);
            copyLogin.setVisibility(View.GONE);
            copyPass.setVisibility(View.GONE);
        }
        else {
            generator.setVisibility(View.GONE);
            name.setEnabled(false);
            login.setEnabled(false);
            etPass.setEnabled(false);
            textDesc.setEnabled(false);

            record = recordList.get(recordName);

            name.setText(record.getName());
            login.setText(record.getLogin());
            etPass.setText(record.getPass());
            textDesc.setText(record.getComment());
        }

    }




    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip;
        switch (v.getId()) {
            //скопировать логин
            case R.id.ibtnCopyLogin:

                clip = ClipData.newPlainText("", login.getText().toString());
                clipboard.setPrimaryClip(clip);
                break;
            //скопировать пароль
            case R.id.ibtnCopyPass:
                clip = ClipData.newPlainText("", login.getText().toString());
                clipboard.setPrimaryClip(clip);
                break;

            case R.id.ibtnSecure:
                if (editable)
                {
                    secure.setImageResource(R.drawable.lock);
                    editable = false;
                    //доступность полей для изменения
                    login.setEnabled(false);
                    etPass.setEnabled(false);
                    textDesc.setEnabled(false);
                }
                else {
                    //запрос ввода пароля для доступа к изменению
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.prompt, null);
                    alertDialogBuilder.setView(promptsView);

                    final EditText securePass = (EditText) promptsView
                            .findViewById(R.id.editTextDialogUserInput);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            //проверка хешей паролей
                                            MessageDigest digest = null;
                                            try {
                                                digest = MessageDigest.getInstance("SHA-256");
                                            } catch (NoSuchAlgorithmException e) {
                                                e.printStackTrace();
                                            }
                                            byte[] usersha = digest.digest(securePass.getText().toString().getBytes());
                                            if(Arrays.toString(usersha).equals(Arrays.toString(recordList.getSha())))
                                            //если пароли совпадают
                                            {
                                                generator.setVisibility(View.VISIBLE);
                                                secure.setImageResource(R.drawable.unlock);
                                                editable = true;
                                                //доступность полей для изменения
                                                login.setEnabled(true);
                                                etPass.setEnabled(true);
                                                textDesc.setEnabled(true);
                                            }
                                            //если пароли не совпадают
                                            else
                                            {
                                                Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });
                    //показ диалогового окна
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                break;

                //TODO обработка генератора
            case R.id.btnAction:
                //если имя и логин не пустые
                if (name.getText().toString().equals("")||login.getText().toString().equals("")) {
                    Toast.makeText(context, "Name or login is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                //если добавление
                if(isAdd) {
                    record = new Record(name.getText().toString(),
                                        login.getText().toString(),
                                        etPass.getText().toString(),
                                        textDesc.getText().toString());
                    if (recordList.get(record.getName()) != null) {
                        Toast.makeText(context, "Name is already exist!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        recordList.add(record);
                    }
                }
                //если просмотр/изменение
                else
                {
                    //если какое-то поле изменили
                    if (!record.getLogin().equals(login.getText().toString())||
                        !record.getPass().equals(etPass.getText().toString())||
                        !record.getComment().equals(textDesc.getText().toString()))
                    {
                        record = new Record(name.getText().toString(),
                                login.getText().toString(),
                                etPass.getText().toString(),
                                textDesc.getText().toString());

                        //перезапись записи
                        recordList.remove(recordList.get(record.getName()));
                        recordList.add(record);
                    }
                }
                setResult(RESULT_OK, intent);
                intent.putExtra("recordList", recordList);
                this.finish();
                break;
            default:
                break;
        }


    }
}
