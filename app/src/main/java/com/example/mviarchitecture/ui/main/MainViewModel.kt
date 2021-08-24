package com.example.mviarchitecture.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.mviarchitecture.ui.main.state.MainStateEvent
import com.example.mviarchitecture.ui.main.state.MainStateEvent.*
import com.example.mviarchitecture.ui.main.state.MainViewState
import com.example.mviarchitecture.util.AbsentLiveData

class MainViewModel: ViewModel() {

    private val _stateEvent = MutableLiveData<MainStateEvent>()

    private val _viewState = MutableLiveData<MainViewState>()
    val viewState: LiveData<MainViewState>
        get() = _viewState

    val dateState: LiveData<MainStateEvent> = Transformations
        .switchMap(_stateEvent) { stateEvent ->
            when (stateEvent) {
                is GetBlogPostEvent -> {
                    AbsentLiveData.create()
                }

                is GetUserEvent -> {
                    AbsentLiveData.create()
                }

                is None -> {
                    AbsentLiveData.create()
                }
            }
        }

}