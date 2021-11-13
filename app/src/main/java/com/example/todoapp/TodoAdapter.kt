package com.example.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter (private val list: List<TodoData>,
    private val listener: OnItemClickListener)
    : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todoItem = list[position]
        holder.tvTodoId.text = position.toString()
        holder.tvTodoText.text = todoItem.todoText
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView), View.OnClickListener{

        val tvTodoId: TextView = ItemView.findViewById(R.id.tvTodoId)
        val tvTodoText: TextView = ItemView.findViewById(R.id.tvTodoText)
        private val btnDelete: ImageButton = ItemView.findViewById(R.id.btnDelete)

        init {
            btnDelete.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if(adapterPosition != RecyclerView.NO_POSITION){
                listener.onDeleteClick(adapterPosition)
            }
        }
    }

    interface OnItemClickListener{
        fun onDeleteClick(position: Int)
    }

}