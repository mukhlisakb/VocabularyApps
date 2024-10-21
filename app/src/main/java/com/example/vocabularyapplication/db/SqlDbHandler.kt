package com.example.vocabularyapplication.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.vocabularyapplication.model.WordCategory
import com.example.vocabularyapplication.model.WordData

class SqlDbHandler(context: Context?): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private const val DB_NAME = "vocabdb"
        private const val TABLE_NAME = "vocab"
        private const val DB_VERSION = 1
        private const val COL_ID = "id"
        private const val COL_NAME = "name"
        private const val COL_MEANING = "meaning"
        private const val COL_CATEGORY = "category"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + COL_NAME + " TEXT, "
                + COL_MEANING + " TEXT"
                + COL_CATEGORY + " TEXT" + ")")
        db?.execSQL(query)
    }
    // create
    fun addVocab(name: String?, meaning: String?, category: String?) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_NAME, name)
        values.put(COL_MEANING, meaning)
        values.put(COL_CATEGORY, category)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    // Read

    fun getVocab(): ArrayList<WordData> {
        val db = this.readableDatabase
        val cursorVocab = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val vocabList: ArrayList<WordData> = arrayListOf()

        if (cursorVocab.moveToFirst()) {
            do {
                WordCategory.values().map {
                    if (it.title == cursorVocab.getString(3)) {
                        vocabList.add(
                            WordData(
                                cursorVocab.getInt(0),
                                cursorVocab.getString(1),
                                cursorVocab.getString(2),
                                it
                            )
                        )
                    }
                }
            } while (cursorVocab.moveToNext())
        }
        cursorVocab.close()
        return vocabList
    }

    // Removed
    fun deleteVocab(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "id=?", arrayOf(id.toString()))
        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}