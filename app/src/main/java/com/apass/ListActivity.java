package com.apass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.apass.entity.Record;
import com.apass.entity.RecordList;

import java.io.FileOutputStream;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    RecordList recordList = new RecordList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        //TEST

        //recordList.add(new Record("name1", "log1", "pass1", "desc1"));
        //recordList.add(new Record("name2", "log2", "pass2", "desc2"));



        //recordList.save(this);
        //recordList.clear();
        recordList.load(this);




















        //TEST

        ListView lvMain = (ListView) findViewById(R.id.lvMain);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, recordList.getNames());

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);

        //показ записи
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intentCreate = new Intent(ListActivity.this, ShowRecordActivity.class);
                intentCreate.putExtra("record", recordList.get(position));
                startActivity(intentCreate);
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intentCreate = new Intent(this, AddChangeActivity.class);
                startActivity(intentCreate);
                break;
            default:
                break;
        }
    }
}
