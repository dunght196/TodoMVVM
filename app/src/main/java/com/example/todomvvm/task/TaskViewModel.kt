package com.example.todomvvm.task

import androidx.lifecycle.*
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import kotlinx.coroutines.launch

class TaskViewModel(
    private val tasksRepository: TasksRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _items: LiveData<List<Task>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate) {
            viewModelScope.launch {
                tasksRepository.refreshTasks()
            }
        }
        tasksRepository.observeTasks().distinctUntilChanged().switchMap { filterTasks(it) }
    }

    val items: LiveData<List<Task>> = _items


    private fun filterTasks(tasksResult: Result<List<Task>>): LiveData<List<Task>> {
        // TODO: This is a good case for liveData builder. Replace when stable.
        val result = MutableLiveData<List<Task>>()

        if (tasksResult is Result.Success) {
            viewModelScope.launch {
                result.value = tasksResult.data
            }
        } else {
            result.value = emptyList()
        }

        return result
    }

    fun loadTasks(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }
}