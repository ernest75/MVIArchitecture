package com.example.mviarchitecture.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mviarchitecture.R
import com.example.mviarchitecture.ui.main.state.MainStateEvent.GetBlogPostEvent
import com.example.mviarchitecture.ui.main.state.MainStateEvent.GetUserEvent

class MainFragment : Fragment(){

    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        }?:throw Exception("Activity is null")

        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            println("DEBUG: DataState: ${dataState}")
            dataState.blogPost?.let { blogPost ->
                    viewModel.setBlogListData(blogPost)
            }

            dataState.user?.let {
                    user -> viewModel.setUserData(user)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.blogPost?.let {
                println("DEBUG Setting blog posts to recyclerview : $it")
            }

            viewState.user?.let {
                println("DEBUG Setting user data :$it")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu,menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_get_user -> triggerGetUserEvent()
            R.id.action_get_blogs -> triggerBlogPostsEvent()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun triggerBlogPostsEvent() {
        viewModel.setStateEvent(GetBlogPostEvent)
    }

    private fun triggerGetUserEvent() {
        viewModel.setStateEvent(GetUserEvent("1"))
    }
}