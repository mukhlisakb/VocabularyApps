package com.example.vocabularyapplication.model

import androidx.annotation.ColorRes
import com.example.vocabularyapplication.R


enum class WordCategory(val title: String, @ColorRes val color: Int) {
    ALL_CATEGORY("All Categories", R.color.black),
    ADJECTIVE("Adjective", R.color.green_51DC2E),
    PREPOSITION("Preposition", R.color.red_DC572E),
    VERB("Verb", R.color.yellow_D6B709),
    NOUN("Noun", R.color.purple_9A96FF)
}

enum class ListWordState {
    NORMAL,
    REMOVED
}