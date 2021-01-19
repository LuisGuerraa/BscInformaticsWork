package com.example.drag

import android.app.Application
import androidx.room.Room
import com.android.volley.toolbox.Volley
import com.example.drag.game.data.DragRepository
import com.example.drag.game.history.DragPlayerDrawing
import com.example.drag.game.history.DragWord
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

private const val GLOBAL_PREFS = "GlobalPreferences"

class DragApp : Application() {

    private val drawingDatabase by lazy {
        Room.databaseBuilder(this, DragPlayerDrawing::class.java, "history_db")
                .fallbackToDestructiveMigration()
                .build()
    }

    private val wordDatabase by lazy {
        Room.databaseBuilder(this, DragWord::class.java, "word_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    val repository by lazy {
        DragRepository(
                getSharedPreferences(GLOBAL_PREFS, MODE_PRIVATE),
                drawingDatabase,
                wordDatabase,
                jacksonObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false),
                Volley.newRequestQueue(this)
        )
    }
}

val Application.repository
    get() = (this as DragApp).repository