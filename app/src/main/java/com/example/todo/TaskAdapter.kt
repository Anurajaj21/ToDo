package com.example.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.task_detail.view.*

class TaskAdapter(private val frag: Tasks): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var tasks = mutableListOf<Task>()
    private val dbTasks = FirebaseDatabase.getInstance().getReference("TASK_ NODE")
    class TaskViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_detail,parent,false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.view.taskCheckBox.text = tasks[position].task
        holder.view.taskCheckBox.setOnCheckedChangeListener {buttonView, ischecked->

                val temp = tasks[position]
                if (ischecked) {
                    temp.chkbox = true
                    dbTasks.child(tasks[position].id!!).setValue(temp)
                }else{
                    temp.chkbox = false
                    dbTasks.child(tasks[position].id!!).setValue(temp)
                }
            }
        holder.view.taskCheckBox.isChecked = tasks[position].chkbox
    }

    override fun getItemCount() = tasks.size

    fun setTasks(tasks: List<Task>){
        this.tasks = tasks as MutableList<Task>
        this.tasks.reverse()
        notifyDataSetChanged()
    }

    fun addTask(task: Task){
        if(!tasks.contains(task)) {
            tasks.reverse()
            tasks.add(task)
            tasks.reverse()
        }else{
            val index = tasks.indexOf(task)
            if(task.isDeleted){
                tasks.removeAt(index)
            }else {
                tasks[index] = task
            }
        }
        if(itemCount == 0){
            frag.instruction.visibility = View.INVISIBLE
            frag.nothing.visibility = View.VISIBLE
        }else{
            frag.instruction.visibility = View.VISIBLE
            frag.nothing.visibility = View.INVISIBLE
        }
        notifyDataSetChanged()
    }
    fun reAddTask(task: Task, position: Int){
        tasks.add(position, task)
        notifyItemInserted(position)
    }

}