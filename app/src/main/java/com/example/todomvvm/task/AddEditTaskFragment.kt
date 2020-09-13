package com.example.todomvvm.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.architecture.blueprints.todoapp.EventObserver
import com.example.android.architecture.blueprints.todoapp.util.getViewModelFactory
import com.example.todomvvm.R
import kotlinx.android.synthetic.main.fragment_addedittask.*

class AddEditTaskFragment : Fragment() {

    private val args: AddEditTaskFragmentArgs by navArgs()

    private val viewModel by viewModels<AddEditTaskViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_addedittask, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupNavigation()
        viewModel.start(args.taskId)
        setupNavigation()

        save_task_fab.setOnClickListener {
            val title = add_task_title_edit_text.text.toString()
            val description = add_task_description_edit_text.text.toString()
            if(title != null && description != null) {
                viewModel.saveTask(title, description)
            }
        }
    }

    private fun setupNavigation() {
        viewModel.taskUpdatedEvent.observe(viewLifecycleOwner, EventObserver {
            val action = AddEditTaskFragmentDirections
                .actionAddEditTaskFragmentToTasksFragment()
            findNavController().navigate(action)
        })
    }
}