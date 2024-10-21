package com.example.vocabularyapplication.model

data class WordData (
    var id: Int,
    var name: String,
    var meaning: String,
    var category: WordCategory
)