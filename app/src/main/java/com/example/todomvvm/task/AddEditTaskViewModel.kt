package com.example.todomvvm.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todomvvm.Event
import com.example.todomvvm.data.Task
import com.example.todomvvm.data.source.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val _taskUpdatedEvent = MutableLiveData<Event<Unit>>()
    val taskUpdatedEvent: LiveData<Event<Unit>> = _taskUpdatedEvent
    private var isNewTask: Boolean = false
    private var taskId: String? = null


    fun start(taskId: String?) {
        this.taskId = taskId
        if(taskId == null) {
            isNewTask = true
            return
        }
    }

    fun saveTask(currentTitle: String, currentDescription: String) {
        val currentTask = taskId
        if(currentTask != null || isNewTask) {
            createTask(Task(currentTitle, currentDescription))
        }
    }

    private fun createTask(newTask: Task) = viewModelScope.launch {
        tasksRepository.saveTask(newTask)
        _taskUpdatedEvent.value = Event(Unit)
    }

}