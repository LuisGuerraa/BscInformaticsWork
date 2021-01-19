package com.example.drag.repo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RandomWords(
    val words: MutableList<String>
) : Parcelable


fun modelFromDTO (dto: List<RandomWordDTO>) = RandomWords(dto.map {
    randomWordDTO -> randomWordDTO.word
}.toMutableList())

enum class State {
    IDLE,
    IN_PROGRESS,
    COMPLETE
}