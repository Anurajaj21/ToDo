package com.example.todo

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_add_task.*

class AddTask : BottomSheetDialogFragment() {



    private lateinit var viewModel: TasksViewModel

    var TAG: String = "ActionBottomDialog"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialohStyle)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(TasksViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_add_task, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return view
    }

    @SuppressLint("ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if (it == null){
                getString(R.string.successfully_added)
            }else{
                getString(R.string.error_occur, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            dismiss()
        })

        edit_task.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(edit_task.text.toString() == ""){
                    new_task.isEnabled = false
                    context?.let { ContextCompat.getColor(it, R.color.atEmpty)}?.let { new_task.setBackgroundColor(it) }
                }else{
                    new_task.isEnabled = true
                    context?.let { ContextCompat.getColor(it, R.color.teal_200)}?.let { new_task.setBackgroundColor(it) }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        new_task.setOnClickListener {
            val task_name = edit_task.text.toString().trim()

            if(task_name.isEmpty()){
                edit_task.error = "Task Name Required"
                edit_task.requestFocus()
                return@setOnClickListener
            }

            val task = Task()
            task.task = task_name

            viewModel.addTask(task)
        }
    }

}