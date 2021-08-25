package com.example.mviarchitecture.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.mviarchitecture.util.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class NetworkBoundResource<ResponseObject,ViewStateType>  {

    protected val result = MediatorLiveData<DataState<ViewStateType>>()

    init {
        result.value = DataState.loading(true)

        GlobalScope.launch(IO) {

            delay(Constants.TESTING_NETWORK_DELAY)

            withContext(Main){
                val apiResponse = createCall()
                result.addSource(apiResponse){ response ->
                    result.removeSource(apiResponse)

                    handleNetworkCall(response)
                }
            }

        }
    }

    fun handleNetworkCall(response: GenericApiResponse<ResponseObject>){
        when (response) {
            is ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
            }

            is ApiEmptyResponse -> {
                val message = "Http 204. Returned NOTHING"
                println("DEBUG: NetworkBoundResource : $message")
                onReturnError(message)

            }

            is ApiErrorResponse -> {
                println("DEBUG: NetworkBoundResource : ${response.errorMessage}")
                onReturnError(response.errorMessage)
            }
        }
    }

    private fun onReturnError(errorMessage: String) {
        result.value = DataState.error(errorMessage)
    }

    abstract fun handleApiSuccessResponse(response: ApiSuccessResponse<ResponseObject>)

    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>

    fun asLivedata()= result as LiveData<DataState<ViewStateType>>
}