package com.example.mviarchitecture.model

import android.media.Image
import android.provider.ContactsContract
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(

    @Expose
    @SerializedName(value = "email")
    val email: String? = null,

    @Expose
    @SerializedName(value = "username")
    val username: String? = null,

    @Expose
    @SerializedName(value = "image")
    val image: String? = null
) {

    override fun toString(): String {
        return "User(email=$email, username=$username, image=$image)"
    }
}