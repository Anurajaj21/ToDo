package com.example.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

class TasksViewModel: ViewModel() {

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
    get() = _result

    private val _show = MutableLiveData<Boolean>()
    val show: LiveData<Boolean>
    get() = _show

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>>
    get() = _tasks

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task>
        get() = _task

    val temp = Tasks()
    val adapter = TaskAdapter(temp)


    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val currenUser = auth.currentUser
    private val userId = currenUser?.uid
    private val dbTasks = userId?.let { FirebaseDatabase.getInstance().reference.child("TASK_NODES").child(it) }
    fun addTask(task: Task){

        if (dbTasks != null) {
            task.id = dbTasks.push().key
        }

        if (dbTasks != null) {
            dbTasks.child(task.id!!).setValue(task)
                    .addOnCompleteListener{
                        if(it.isSuccessful)
                            _result.value = null
                        else
                            _result.value = it.exception


                    }
        }
        adapter.notifyDataSetChanged()
    }

    fun readdTask(task: Task){
        if (dbTasks != null) {
            dbTasks.child(task.id!!).setValue(task)
                    .addOnCompleteListener{
                        if(it.isSuccessful)
                            _result.value = null
                        else
                            _result.value = it.exception


                    }
        }
    }

    private val childEventListener = object  : ChildEventListener{
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val task = snapshot.getValue(Task::class.java)
            task?.id = snapshot.key
            _task.value = task
            adapter.notifyDataSetChanged()
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val task = snapshot.getValue(Task::class.java)
            task?.id = snapshot.key
            _task.value = task
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val task = snapshot.getValue(Task::class.java)
            task?.id = snapshot.key
            task?.isDeleted = true
            _task.value = task
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    fun getRealTimeUptades(){
        if (dbTasks != null) {
            dbTasks.addChildEventListener(childEventListener)
        }
    }
    fun fetchTasks(){
        if (dbTasks != null) {
            dbTasks.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val tasks = mutableListOf<Task>()
                        for (taskSnapshot in snapshot.children) {
                            val task = taskSnapshot.getValue(Task::class.java)
                            task?.id = taskSnapshot.key
                            task?.let { tasks.add(it) }
                        }
                        _show.value = true
                        //tasks.reverse()
                        _tasks.value = tasks
                    }else{
                        _show.value = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }

    fun edit_task(task: Task){

        if (dbTasks != null) {
            dbTasks.child(task.id!!).setValue(task)
                    .addOnCompleteListener{
                        if(it.isSuccessful)
                            _result.value = null
                        else
                            _result.value = it.exception

                    }
        }
    }
    fun get_Edit_Task(position: Int): Task?{
        return _tasks.value?.get(position)
    }

    fun delete_task(position: Int){
        val task = _tasks.value?.get(position)
        if (task != null) {
            if (dbTasks != null) {
                dbTasks.child(task.id!!).setValue(null)
                        .addOnCompleteListener{
                            if(it.isSuccessful)
                                _result.value = null
                            else
                                _result.value = it.exception


                        }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (dbTasks != null) {
            dbTasks.removeEventListener(childEventListener)
        }
    }

}