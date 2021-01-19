package com.example.drag

import android.os.Parcelable
import com.example.drag.game.data.Drawing
import kotlinx.android.parcel.Parcelize
import com.example.drag.GameState.State.*
import com.example.drag.game.data.PointsDTO
import com.example.drag.lobbies.Player


data class GameStateDTO(
        val drawing: PointsDTO,
        val currWord: String?,
        val turn: Int?,
        val winner: Player?,
        val state: GameState.State
) {

}

fun GameStateDTO.toGameState() = GameState(Drawing(drawing.points.toMutableList()), currWord, turn, winner, state)



@Parcelize
data class GameState(
        val drawing: Drawing = Drawing(),
        val currWord: String? = null,
        var turn: Int? = 1,
        val winner: Player? = null,
        val state: State = INIT,
        var endGame: Boolean = false
) : Parcelable {

    enum class State { INIT, NOT_STARTED, DRAW_WORD, GUESS_WORD,WAITING_FOR_PLAYERS, CHOOSE_WORD, END_ROUND}

    fun toGameStateDTO() = GameStateDTO(drawing.toPointsDTO(), currWord, turn, winner, state)

}