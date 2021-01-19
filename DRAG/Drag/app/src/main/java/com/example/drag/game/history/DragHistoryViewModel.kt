package com.example.drag.game.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.drag.GameState
import com.example.drag.lobbies.LobbyInfo
import com.example.drag.lobbies.Player
import com.example.drag.repository

class DragHistoryViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = app.repository

    fun deleteHistory(localPlayer: Player?, id: String?) {
        if (localPlayer == null) {
            repository.deleteHistory()
        } else {
            repository.unpublishMoves(id)
        }
    }

    fun resetRepositoryRound() {
        repository.resetRepositoryRound()
    }



    fun resetGameState(lobbyInfo: LobbyInfo?) {
        repository.updateGameState(GameState(
            state = GameState.State.INIT
        ), lobbyInfo, {}, {})
    }

    fun loadCurrentRound(currRound: Int) {
        repository.loadCurrentRound(currRound)
    }

    fun getCurrRound(): Int {
        return repository.currRound
    }

    val history
        get() = repository.allHistoryItems

    val wordsHistory
        get() = repository.allWordItems

}