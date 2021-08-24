package com.example.mviarchitecture.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.mviarchitecture.model.BlogPost
import com.example.mviarchitecture.model.User
import com.example.mviarchitecture.repository.Repository
import com.example.mviarchitecture.ui.main.state.MainStateEvent
import com.example.mviarchitecture.ui.main.state.MainStateEvent.*
import com.example.mviarchitecture.ui.main.state.MainViewState
import com.example.mviarchitecture.util.AbsentLiveData

class MainViewModel: ViewModel() {

    private val _stateEvent = MutableLiveData<MainStateEvent>()

    private val _viewState = MutableLiveData<MainViewState>()
    val viewState: LiveData<MainViewState>
        get() = _viewState

    val dataState: LiveData<MainViewState> = Transformations
        .switchMap(_stateEvent) { stateEvent ->
            when (stateEvent) {
                is GetBlogPostEvent -> {
                    Repository.getBlogPosts()
                }

                is GetUserEvent -> {
                    Repository.getUser(stateEvent.userID)
                }

                is None -> {
                    AbsentLiveData.create()
                }
            }
        }

    fun setBlogListData(blogPost: List<BlogPost>){
        val update = getCurrentViewStateOrNew()
        update.blogPost = blogPost
        _viewState.value = update
    }

    fun setUserData(user:User){
        val update = getCurrentViewStateOrNew()
        update.user = user
        _viewState.value = update
    }

    private fun getCurrentViewStateOrNew() :MainViewState{
        val value = viewState.value?.let{
            it
        }?: MainViewState()

        return value
    }

    fun setStateEvent(event:MainStateEvent){
        _stateEvent.value = event
    }

}