package com.example.drag.lobbies

import android.os.Parcelable
import com.example.drag.game.data.Drawing
import com.example.drag.game.data.Point
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LobbyInfo(
        val id: String,
        val lobbyPlayerName: String,
        val numberOfRounds: String,
        val numberOfPlayers: String,
        var currentPlayers: String
) : Parcelable

@Parcelize
data class Player(
        val id: Int,
        val username: String?,
        val points: Int = 0
) : Parcelable

@Parcelize
data class Move(
        val id: String,
        val playerName: String?,
        val currRound: Int,
        val drawing: List<Point>,
        val word: String?
) : Parcelable