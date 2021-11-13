package com.example.todoapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.db.DatabaseHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TodoAdapter.OnItemClickListener {
    private val todoList = ArrayList<TodoData>()
    private val myDB : DatabaseHelper = DatabaseHelper(this)
    private val todoAdapter = TodoAdapter(todoList, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnAddTodo.setOnClickListener{addTodo()}
        rvTodos.layoutManager = LinearLayoutManager(this)
        rvTodos.adapter = todoAdapter

        loadTodos()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadTodos(){
        val cursor = myDB.fetchAllTodos()
        if(cursor!=null){
            if (cursor.moveToFirst()) {
                var todoText:String
                var todoId:Int
                do {
                    todoId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_ID))
                    todoText = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TODO))
                    todoList.add(TodoData(id=todoId, todoText=todoText))
                } while (cursor.moveToNext())
            }
            cursor.close()
            todoAdapter.notifyDataSetChanged()
        }
    }

    override fun onDeleteClick(position: Int) {
        val itemIdToDelete = todoList[position].id
        Toast.makeText(this, "Delete button clicked for id $itemIdToDelete", Toast.LENGTH_SHORT).show()
        loadTodos()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addTodo(){
            val status = myDB.saveTodo(etTodo.text.toString())
            if(status > -1){
                loadTodos()
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
    }
}