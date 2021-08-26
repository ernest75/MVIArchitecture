package com.example.mviarchitecture.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mviarchitecture.R
import com.example.mviarchitecture.model.BlogPost
import com.example.mviarchitecture.ui.DataStateListener
import com.example.mviarchitecture.ui.main.state.MainStateEvent.GetBlogPostEvent
import com.example.mviarchitecture.ui.main.state.MainStateEvent.GetUserEvent
import com.example.mviarchitecture.util.TopSpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), BlogPostAdapter.Interaction {

    lateinit var viewModel: MainViewModel

    lateinit var dataStateListener: DataStateListener

    lateinit var blogPostAdapter: BlogPostAdapter

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
        initRecyclerView()

    }

    private fun initRecyclerView(){
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            val topSpacingDecoration = TopSpaceItemDecoration(30)
            addItemDecoration(topSpacingDecoration)
            blogPostAdapter = BlogPostAdapter(this@MainFragment)
            adapter = blogPostAdapter

        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->

            //Handle loading + message
            dataStateListener.onDataStateChange(dataState)

            //Handle Data<T>
            dataState.data?.let { event ->
                event.getContentIfNotHandled()?.let { mainViewState->

                    println("DEBUG: DataState: ${mainViewState}")

                    mainViewState.blogPost?.let {
                        viewModel.setBlogListData(it)
                    }

                    mainViewState.user?.let {
                        viewModel.setUserData(it)
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.blogPost?.let {
                // set BlogPosts to RecyclerView
                println("DEBUG: Setting blog posts to RecyclerView: ${viewState.blogPost}")
                blogPostAdapter.submitList(it)
            }

            viewState.user?.let{
                // set User data to widgets
                println("DEBUG: Setting User data: ${viewState.user}")
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataStateListener = context as DataStateListener

        }catch (e:ClassCastException){
            println("DEBUG: $context must implement DataStateListener")
        }
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        println("DEBUG: CLICKED $position")
        println("DEBUG: CLICKED ${item.title}")
    }

}