package com.example.mviarchitecture.api

import com.example.mviarchitecture.model.BlogPost
import com.example.mviarchitecture.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("placeholder/blogs")
    fun getBlogs(): List<BlogPost>

    @GET("placeholder/user/{userID}")
    fun getUser(
        @Path("userId") userId: String
    ) : User


}