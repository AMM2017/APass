package com.apass.tools;

import com.apass.entity.Record;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by Ivan on 14.12.2017.
 */

public class RecordComparator implements Comparator<Record> {
    @Override
    public int compare(Record o1, Record o2) {
        //специанльный абстрактный класс для сравнения строк
        return Collator.getInstance().compare(o1.getName(), o2.getName());
    }
}
