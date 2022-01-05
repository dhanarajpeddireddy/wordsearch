package com.dana.wordsearch.data.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dana.wordsearch.model.Word;

import java.util.ArrayList;
import java.util.List;


public class WordSQLiteDataSource  {

    private final DbHelper mHelper;

    public WordSQLiteDataSource(DbHelper helper) {
        mHelper = helper;
    }

    public List<Word> getWords() {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String[] cols = {
                DbContract.WordBank._ID,
                DbContract.WordBank.COL_STRING
        };

        Cursor c = db.query(DbContract.WordBank.TABLE_NAME, cols, null, null, null, null, null);

        List<Word> wordList = new ArrayList<>();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {

                wordList.add(new Word(c.getInt(0), c.getString(1)));

                c.moveToNext();
            }
        }

        c.close();
        return wordList;
    }
}
