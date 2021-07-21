package com.example.newsbreeze.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsArgs(
    var title: String,
    var description: String,
    var imgURL: String,
    var content: String,
    var date: String,
    var author: String
) : Parcelable {
}