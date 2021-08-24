package com.example.mviarchitecture.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.mviarchitecture.api.RetrofitBuilder
import com.example.mviarchitecture.model.User
import com.example.mviarchitecture.ui.main.state.MainViewState
import com.example.mviarchitecture.util.ApiEmptyResponse
import com.example.mviarchitecture.util.ApiErrorResponse
import com.example.mviarchitecture.util.ApiSuccessResponse

object Repository {

    fun getBlogPosts(): LiveData<MainViewState> {
        return Transformations
            .switchMap(RetrofitBuilder.apiService.getBlogPosts()) { apiResponse ->
                object : LiveData<MainViewState>() {
                    override fun onActive() {
                        super.onActive()
                        when (apiResponse) {
                            is ApiSuccessResponse -> {
                                value = MainViewState(
                                    blogPost = apiResponse.body
                                )
                            }

                            is ApiEmptyResponse -> {
                                value = MainViewState()
                            }

                            is ApiErrorResponse -> {
                                value = MainViewState()
                            }
                        }

                    }
                }
            }
    }

    fun getUser(userID :String) :LiveData<MainViewState>{
        return Transformations
            .switchMap(RetrofitBuilder.apiService.getUser(userID)){ apiResponse ->
                object : LiveData<MainViewState>() {
                    override fun onActive() {
                        super.onActive()
                        when(apiResponse) {
                            is ApiSuccessResponse -> {
                                value = MainViewState(user = apiResponse.body)
                            }
                            is ApiEmptyResponse -> value = MainViewState()
                            is ApiErrorResponse -> value = MainViewState()
                        }
                    }
                }
            }
    }
}