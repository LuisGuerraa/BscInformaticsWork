package com.example.drag.lobbies.list

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.drag.DragApp
import com.example.drag.R
import com.example.drag.lobbies.LobbyInfo
import com.example.drag.repository
import java.lang.Exception
import com.example.drag.utils.Result
import com.example.drag.utils.State

class LobbiesViewModel(app: Application) : AndroidViewModel(app) {

    private val app = getApplication<DragApp>()

    val lobbies: LiveData<List<LobbyInfo>> = MutableLiveData()

    var localPlayerUsername = ""


    fun fetchLobbies() {
        app.repository.fetchLobbies(
            onSuccess = {
                (lobbies as MutableLiveData<List<LobbyInfo>>).value = it
            },
            onError = {
                Toast.makeText(app, R.string.error_getting_list, Toast.LENGTH_LONG).show()
            }
        )
    }


    val enrolmentResult: LiveData<Result<LobbyInfo, Exception>> = MutableLiveData()


    fun tryAcceptLobby(lobbyInfo: LobbyInfo, username: String) {
        app.repository.resetRepositoryRound()
        localPlayerUsername = username
        val state = enrolmentResult as MutableLiveData<Result<LobbyInfo, Exception>>
        state.value = Result(State.ONGOING, lobbyInfo)
        lobbyInfo.currentPlayers = (Integer.valueOf(lobbyInfo.currentPlayers) + 1).toString()
        app.repository.updateLobbyInfo(
                lobbyInfo,
                { state.value = Result(State.COMPLETE, lobbyInfo) },
                { error -> state.value = Result(State.COMPLETE, lobbyInfo, error) }
        )

        if(Integer.valueOf(lobbyInfo.currentPlayers) == Integer.valueOf(lobbyInfo.numberOfPlayers)){
            app.repository.unpublishLobby(
                    lobbyInfo.id,
                    { state.value = Result(State.COMPLETE, lobbyInfo) },
                    { error -> state.value = Result(State.COMPLETE, lobbyInfo, error) }
            )
            return
        }
    }

    fun resetEnrolmentResult() {
        (enrolmentResult as MutableLiveData<Result<LobbyInfo, Exception>>).value = Result()
    }

}
