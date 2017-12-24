package com.apass;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.apass.entity.RecordList;

public class ListActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    final int REQUEST_CODE_ADD = 1;
    final int REQUEST_CODE_CHANGE = 2;
    final int REQUEST_CODE_DELETE = 7;
    private final Context context = this;
    RecordList recordList = new RecordList();
    ListView lvMain;
    EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setOnClickListener(this);

        Intent intent = getIntent();
        recordList = (RecordList) intent.getSerializableExtra("recordList");

        //строка поиска
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        editTextSearch.setOnClickListener(this);
        editTextSearch.addTextChangedListener(this);

        //кнопка добавить
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        ShowRecords(this, recordList);
    }

    void ShowRecords(final ContextWrapper contextWrapper, final RecordList rl) {

        lvMain = (ListView) findViewById(R.id.lvMain);
        //адаптеры связывают данные и предстваление, ArrayAdapter связывает layout-list с массивом, взятым из списка паролей
        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, rl.getNames());

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);

        //показ записи
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intentCreate = new Intent(ListActivity.this, RecordActivity.class);
                intentCreate.putExtra("recordName", rl.get(position).getName());
                intentCreate.putExtra("recordList", recordList);
                intentCreate.putExtra("isAdd", false);
                startActivityForResult(intentCreate, REQUEST_CODE_CHANGE);
                editTextSearch.setText("");
                ShowRecords(contextWrapper, recordList);
            }
        });

        //удаление записи
        lvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long id) {
                //получение подтверждения - диалог да/нет
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.is_delete_record)
                        .setTitle(R.string.confirmation);
                //добавление кнопок
                builder.setPositiveButton(R.string.dialog_btn_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        recordList.remove(rl.get(rl.get(position).getName()));
                        recordList.save(contextWrapper);
                        editTextSearch.setText("");
                        ShowRecords(contextWrapper, recordList);
                    }
                });
                builder.setNegativeButton(R.string.dialog_btn_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                editTextSearch.getText().clear();
                Intent intentCreate = new Intent(this, RecordActivity.class);
                intentCreate.putExtra("recordName", "");
                intentCreate.putExtra("recordList", recordList);
                intentCreate.putExtra("isAdd", true);
                startActivityForResult(intentCreate, REQUEST_CODE_ADD);
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intentSettings= new Intent(this, SettingsActivity.class);
                intentSettings.putExtra("recordList", recordList);
                startActivityForResult(intentSettings, REQUEST_CODE_DELETE);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // если пришло ОК
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ADD:
                case REQUEST_CODE_CHANGE:
                    recordList = (RecordList) data.getSerializableExtra("recordList");
                    recordList.save(this);
                    ShowRecords(this, recordList);
                    break;
                case REQUEST_CODE_DELETE:
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0)
            ShowRecords(this, recordList);
        else
            ShowRecords(this, recordList.GetFilteredList(s.toString()));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
