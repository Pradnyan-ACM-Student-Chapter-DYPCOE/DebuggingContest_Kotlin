package com.example.todoapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.db.DatabaseHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.update_dialog.*

class MainActivity : AppCompatActivity(), TodoAdapter.OnItemClickListener {
    private val todoList = ArrayList<TodoData>()
    private val myDB : DatabaseHelper = DatabaseHelper(this)
    private val todoAdapter = TodoAdapter(todoList, this)
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private lateinit var del: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAddTodo.setOnClickListener{
            val text = etTodo.text.toString()
            // checking edit text is null or not
            if (text.trim().isNotEmpty()) {
                addTodo(text)
                //clear edit text
                etTodo.text?.clear()
            } else {
                Toast.makeText(this, "Enter Valid Todo text", Toast.LENGTH_SHORT).show()
            }

        }
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
        dialog(position)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addTodo(text:String){
            val status = myDB.saveTodo(text)
            if(status > 0){
                loadTodos()
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }

    }
    // created dialog for deleting todo
    private fun dialog(position: Int){
        val itemIdToDelete = todoList[position].id
        builder = AlertDialog.Builder(this)
        val itemView: View =
            LayoutInflater.from(this@MainActivity).inflate(R.layout.update_dialog, null)
        dialog = builder.create()
        dialog.setView(itemView)
        del = itemView.findViewById<Button>(R.id.del_button)
        del.setOnClickListener {
            //deleting todo
            myDB.deleteTodo(itemIdToDelete)
            dialog.dismiss()
        }
        dialog.show()
    }

}