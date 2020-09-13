package com.example.todomvvm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.todomvvm.R

class TaskAdapterTest() : RecyclerView.Adapter<TaskAdapterTest.ViewHolder>() {
    private var datas: ArrayList<Task> = arrayListOf();

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.task_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = datas[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.complete_checkbox)
        val title: TextView = itemView.findViewById(R.id.title_text)

        fun bind(item: Task) {
            checkBox.isChecked = item.isCompleted
            title.text = item.titleForList
        }
    }

    fun initData(listTask: List<Task>) {
        datas.clear();
        datas.addAll(listTask);
        notifyDataSetChanged()
    }
}
