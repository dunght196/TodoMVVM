package com.example.todomvvm.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todomvvm.R
import com.example.todomvvm.TaskAdapterTest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_task.*

@AndroidEntryPoint
class TaskFragment : Fragment() {

    private val viewModel by viewModels<TaskViewModel>()

    private val args: TaskFragmentArgs by navArgs()

    private lateinit var listAdapter: TaskAdapterTest


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupAdapter()
        setupFab()

        viewModel.loadTasks(false);

        viewModel.empty.observe(viewLifecycleOwner, {
            no_tasks_layout.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.items.observe(viewLifecycleOwner, {
            listAdapter.initData(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setupAdapter() {
        listAdapter = TaskAdapterTest()
        list_task.adapter = listAdapter
    }

    private fun setupFab() {
        activity?.findViewById<FloatingActionButton>(R.id.add_task_fab)?.let {
            it.setOnClickListener {
                navigateToAddNewTask()
            }
        }
    }

    private fun navigateToAddNewTask() {
        val action = TaskFragmentDirections
            .actionTasksFragmentToAddEditTaskFragment(
                null,
                resources.getString(R.string.add_task)
            )
        findNavController().navigate(action)
    }
}