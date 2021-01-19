package com.example.drag.utils

import kotlin.random.Random

object Words {

    private val offlineWords = arrayOf(
        "car", "piano", "thimble", "guitar",
        "doll", "monkey", "onion", "olive oil",
        "kitchen", "rolling", "scaffolding", "virtue",
        "printed", "cushion", "carpet", "writer"
    )

    private val rand = Random(System.nanoTime())

    fun getRandomOfflineWord() = offlineWords[rand.nextInt(offlineWords.size)]
}
