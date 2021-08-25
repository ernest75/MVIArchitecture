package com.example.mviarchitecture.ui.main.state

sealed class MainStateEvent {

    object GetBlogPostEvent : MainStateEvent()

    class GetUserEvent(
        val userId: String
    ): MainStateEvent()

    object None : MainStateEvent()
}