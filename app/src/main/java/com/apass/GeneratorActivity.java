package com.apass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GeneratorActivity extends AppCompatActivity implements View.OnClickListener,
        NumberPicker.OnValueChangeListener, CompoundButton.OnCheckedChangeListener {

    NumberPicker numberPicker;

    CheckBox lowEng;
    CheckBox upEng;
    CheckBox lowRu;
    CheckBox upRu;
    CheckBox number;
    CheckBox specSymbols;

    TextView txtgeneratedPass;

    private boolean isLowEng = true;
    private boolean isUpEng = false;
    private boolean isLowRu = false;
    private boolean isUpRu = false;
    private boolean isNumber = false;
    private boolean isSpecSymbols = false;

    String resultPass;
    int resultLength;

    Button btnGeneratePass;
    Button btnDoneGenerate;

    public static final String UPPER_EN = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String LOWER_EN = UPPER_EN.toLowerCase();

    public static final String UPPER_RU = "БВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";

    public static final String LOWER_RU = UPPER_RU.toLowerCase();

    public static final String DIGITS = "0123456789";

    public static final String SYMBOLS = "!?@#$%^&*()-_";

    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);

        btnGeneratePass = (Button) findViewById(R.id.btnGeneratePass);
        btnGeneratePass.setOnClickListener(this);

        btnDoneGenerate = (Button) findViewById(R.id.btnDoneGenerate);
        btnDoneGenerate.setOnClickListener(this);

        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setOnValueChangedListener(this);

        lowEng = (CheckBox) findViewById(R.id.cbLowEng);
        lowEng.setOnCheckedChangeListener(this);

        upEng = (CheckBox) findViewById(R.id.cbUpEng);
        upEng.setOnCheckedChangeListener(this);

        lowRu = (CheckBox) findViewById(R.id.cbLowRu);
        lowRu.setOnCheckedChangeListener(this);

        upRu = (CheckBox) findViewById(R.id.cbUpRu);
        upRu.setOnCheckedChangeListener(this);

        number = (CheckBox) findViewById(R.id.cbNumber);
        number.setOnCheckedChangeListener(this);

        specSymbols = (CheckBox) findViewById(R.id.cbSpecSymbols);
        specSymbols.setOnCheckedChangeListener(this);

        txtgeneratedPass = (TextView)findViewById(R.id.txtGeneratedPass);


        //установка начальных значений
        lowEng.setChecked(isLowEng);
        upEng.setChecked(isUpEng);
        lowRu.setChecked(isLowRu);
        upRu.setChecked(isUpRu);
        number.setChecked(isNumber);
        specSymbols.setChecked(isSpecSymbols);

        numberPicker.setMinValue(2);
        numberPicker.setMaxValue(20);
        resultLength = numberPicker.getValue();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGeneratePass:
                if (!(isLowEng || isUpEng || isLowRu || isUpRu || isNumber || isSpecSymbols))
                    Toast.makeText(this, R.string.no_symbols_set, Toast.LENGTH_SHORT).show();
                else {
                    String source = "";
                    if (isLowEng) source += LOWER_EN;
                    if (isUpEng) source += UPPER_EN;
                    if (isLowRu) source += LOWER_RU;
                    if (isUpRu) source += UPPER_RU;
                    if (isNumber) source += DIGITS;
                    if (isSpecSymbols) source += SYMBOLS;

                    char[] sourceArray = source.toCharArray();
                    resultPass = "";
                    for (int i = 0; i < resultLength; ++i)
                        resultPass += sourceArray[random.nextInt(sourceArray.length)];

                    txtgeneratedPass.setText(resultPass);
                }
                break;
            case R.id.btnDoneGenerate:
                if (resultPass == null)
                    Toast.makeText(this, R.string.pass_not_gnerated, Toast.LENGTH_SHORT).show();
                else
                {
                    Intent intent = new Intent();
                    intent.putExtra("generatedPass", resultPass);
                    setResult(RESULT_OK, intent);
                    this.finish();
                }
                break;
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        resultLength = newVal;
    }

    //изменение состояний флагов по чекбоксам
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cbLowEng:
                isLowEng = isChecked;
                break;
            case R.id.cbUpEng:
                isUpEng = isChecked;
                break;
            case R.id.cbLowRu:
                isLowRu = isChecked;
                break;
            case R.id.cbUpRu:
                isUpRu = isChecked;
                break;
            case R.id.cbNumber:
                isNumber = isChecked;
                break;
            case R.id.cbSpecSymbols:
                isSpecSymbols = isChecked;
                break;
        }
    }


}
