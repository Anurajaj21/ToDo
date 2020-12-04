package com.example.todo

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.fragment_tasks.*

class Tasks : Fragment() {


    private lateinit var viewModel: TasksViewModel
    private val adapter = TaskAdapter(this@Tasks)
    private var deletedTask : Task? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(TasksViewModel::class.java)
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        show_task.adapter = adapter


        viewModel.fetchTasks()
        viewModel.getRealTimeUptades()

        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            adapter.setTasks(it)
        })

        viewModel.task.observe(viewLifecycleOwner, Observer {
            adapter.addTask(it)
        })


        val item1 = object :RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                super.onSwiped(viewHolder, direction)
                val position = viewHolder.adapterPosition
                deletedTask = viewModel.get_Edit_Task(position)
                viewModel.delete_task(position)
                val snackbar = Snackbar.make(show_task, "Task Deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        deletedTask?.let { it1 -> viewModel.readdTask(it1) }
                        deletedTask?.let { it1 -> adapter.reAddTask(it1, position) }
                        //adapter.notifyItemInserted(position)
                    }
                snackbar.show()
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(Color.RED)
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete)
                        .create()
                        .decorate()
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            }

        }

        val item2 = object :RecyclerItemTouchHelper(0,ItemTouchHelper.RIGHT){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val editabeTask = viewModel.get_Edit_Task(position)
                editabeTask?.let { UpdateTask(it) }?.show(parentFragmentManager,"TAG")
                adapter.notifyDataSetChanged()
                super.onSwiped(viewHolder, direction)
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightBackgroundColor(Color.GREEN)
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_edit)
                        .create()
                        .decorate()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val itemTouchHelper1 = ItemTouchHelper(item1)
        itemTouchHelper1.attachToRecyclerView(show_task)

        val itemTouchHelper2 = ItemTouchHelper(item2)
        itemTouchHelper2.attachToRecyclerView(show_task)


        add.setOnClickListener{
            val addTask = AddTask()
            addTask.show(parentFragmentManager, "TAG")

        }
//        viewModel.show.observe(viewLifecycleOwner, Observer {
//            if (it){
//                instruction.visibility = View.VISIBLE
//            }else{
//                instruction.visibility = View.INVISIBLE
//            }
//        })

    }
    fun vis(){
        instruction.visibility = View.VISIBLE
    }
    fun invis(){
        instruction.visibility = View.INVISIBLE
    }
}