package com.example.nasaapodlist

//Data class to store the picture, date, and title in a single list
data class apoddata(
    val url: String,
    val title: String,
    val date: String
) {
    override fun toString(): String {
        return "$url, $title, $date"
    }
}
