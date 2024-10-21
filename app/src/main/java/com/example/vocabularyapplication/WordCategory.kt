package com.example.vocabularyapplication

import androidx.annotation.ColorRes

enum class WordCategory(val title: String, @ColorRes val color: Int) {
    ALL_CATEGORY("All Categories", R.color.black),
    ADJECTIVES("Adjectives", R.color.green_cal),
    PREPOSITIONS("Prepositions", R.color.orange_cal),
    VERB("Verbs", R.color.yellow_cal),
    NOUN("Nouns", R.color.purple_cal)
}