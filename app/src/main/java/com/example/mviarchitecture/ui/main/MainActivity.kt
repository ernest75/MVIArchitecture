package com.example.mviarchitecture.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mviarchitecture.R
import com.example.mviarchitecture.ui.DataStateListener
import com.example.mviarchitecture.util.DataState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),DataStateListener {

    override fun onDataStateChange(dataState: DataState<*>?) {
        handleDataState(dataState)
    }

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        showMainFragment()

    }

    fun showMainFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MainFragment(),"MainFragment")
            .commit()
    }

    fun handleDataState(dataState: DataState<*>?) {

        dataState?.let {
            //handle loading
            showProgressBar(it.loading)

            //handle error message
            dataState.message?.let { event->
                event.getContentIfNotHandled()?.let { message->
                    showToastMessage(message)
                }
            }
        }
    }

    private fun showToastMessage(message: String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    fun showProgressBar(isVisible: Boolean){
        if (isVisible){
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }
}