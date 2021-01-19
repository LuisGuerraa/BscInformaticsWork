package com.example.drag.lobbies.create

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.drag.DragApp
import com.example.drag.lobbies.LobbyInfo
import com.example.drag.utils.Result
import com.example.drag.utils.State

class CreateLobbyViewModel(app: Application) : AndroidViewModel(app)  {

    val result: LiveData<Result<LobbyInfo, Exception>> = MutableLiveData()

    fun createLobby(name: String, nrOfRounds: String, nrOfPlayers: String, currentPlayers: String) {
        val app = getApplication<DragApp>()
        val mutableResult = result as MutableLiveData<Result<LobbyInfo, Exception>>
        app.repository.publishLobby(name, nrOfRounds, nrOfPlayers, currentPlayers,
                onSuccess = { mutableResult.value = Result(State.COMPLETE, result = it) },
                onError = { mutableResult.value = Result(State.COMPLETE, error = it) }
        )
    }
}