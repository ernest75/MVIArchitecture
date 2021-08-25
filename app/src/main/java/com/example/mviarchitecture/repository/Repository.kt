package com.example.mviarchitecture.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.mviarchitecture.api.RetrofitBuilder
import com.example.mviarchitecture.model.User
import com.example.mviarchitecture.ui.main.state.MainViewState
import com.example.mviarchitecture.util.ApiEmptyResponse
import com.example.mviarchitecture.util.ApiErrorResponse
import com.example.mviarchitecture.util.ApiSuccessResponse
import com.example.mviarchitecture.util.DataState

object Repository {

    fun getBlogPosts(): LiveData<DataState<MainViewState>> {
        return Transformations
            .switchMap(RetrofitBuilder.apiService.getBlogPosts()) { apiResponse ->
                object : LiveData<DataState<MainViewState>>() {
                    override fun onActive() {
                        super.onActive()
                        when (apiResponse) {
                            is ApiSuccessResponse -> {
                                value = DataState.data(
                                    data = MainViewState(
                                        blogPost = apiResponse.body
                                    )
                                )
                            }

                            is ApiEmptyResponse -> {
                                value = DataState.error(message = "Http 204. Returned NOTHING")
                            }

                            is ApiErrorResponse -> {
                                value = DataState.error(message = apiResponse.errorMessage)
                            }
                        }

                    }
                }
            }
    }

    fun getUser(userID :String) :LiveData<DataState<MainViewState>>{
        return Transformations
            .switchMap(RetrofitBuilder.apiService.getUser(userID)){ apiResponse ->
                object: LiveData<DataState<MainViewState>>() {
                    override fun onActive() {
                        super.onActive()
                        when (apiResponse) {
                            is ApiSuccessResponse -> {
                                value = DataState.data(
                                    data = MainViewState(
                                        user = apiResponse.body
                                    )
                                )
                            }

                            is ApiEmptyResponse -> {
                                value = DataState.error(message = "Http 204. Returned NOTHING")
                            }

                            is ApiErrorResponse -> {
                                value = DataState.error(message = apiResponse.errorMessage)
                            }
                        }
                    }
                }
            }
    }
}