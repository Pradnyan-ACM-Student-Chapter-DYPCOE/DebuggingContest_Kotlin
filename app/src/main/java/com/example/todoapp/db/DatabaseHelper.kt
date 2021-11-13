package com.example.todoapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context:Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TodosDatabase"
        private const val TABLE_TODOS = "TodosTable"
        const val KEY_ID = "id"
        const val KEY_TODO = "todo"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE " + TABLE_TODOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TODO + " TEXT NOT NULL"
                + ")")
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_TODOS")
        onCreate(db)
    }

    fun saveTodo(todoText: String):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TODO, todoText)
        // Inserting Row
        val success = db.insert(TABLE_TODOS, null, contentValues)
        db.close() // Closing database connection
        return success
    }

    //method to read data
    fun fetchAllTodos(): Cursor? {
        val selectQuery = "SELECT  * FROM $TABLE_TODOS"
        val db = this.readableDatabase
        val cursor: Cursor?
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return null
        }
        return cursor
    }

    //method to delete data
    fun deleteTodo(todoId: Int):Int{
        val db = this.writableDatabase
        // Deleting Row
        val success = db.delete(TABLE_TODOS, "id=$todoId",null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}