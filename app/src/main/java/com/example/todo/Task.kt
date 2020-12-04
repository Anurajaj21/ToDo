package com.example.todo

import com.google.firebase.database.Exclude

data class Task(
    @get:Exclude
    var id: String? = null,
    var task: String? = null,
    var chkbox: Boolean = false,
    @get:Exclude
    var isDeleted: Boolean = false
){
    override fun equals(other: Any?): Boolean {
        return if(other is Task) {
            other.id == id
        }else false
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (task?.hashCode() ?: 0)
        result = 31 * result + chkbox.hashCode()
        result = 31 * result + isDeleted.hashCode()
        return result
    }


}