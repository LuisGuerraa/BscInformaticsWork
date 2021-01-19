package com.example.drag

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import com.example.drag.GameState.State.*
import com.example.drag.game.data.Drawing
import com.example.drag.game.data.Point
import com.example.drag.lobbies.LobbyInfo
import com.example.drag.lobbies.Player
import com.example.drag.repo.RandomWords
import com.example.drag.repo.State
import com.example.drag.utils.Words
import java.util.concurrent.Executors

private const val SAVED_STATE_KEY = "DragViewModel.SavedState"



class DragViewModel(
    private val localPlayer: Player?,
    private val lobbyInfo: LobbyInfo?,
    application: Application,
    private val savedState: SavedStateHandle
) : AndroidViewModel(application) {


    private val repository = application.repository

    private var nrOfPlayers = repository.numberOfPlayers
    private var nrOfRounds = repository.numberOfRounds
    private var currRound = repository.currRound
    private var isPair = nrOfPlayers % 2 == 0


    private val subscription = getApplication<DragApp>().repository.subscribeTo(
        lobbyInfo?.id,
        onSubscriptionError = { TODO() },
        onStateChanged = {
            game.value = it
        }
    )

    val randomWords: LiveData<Result<RandomWords>?> = MediatorLiveData()
    val state: LiveData<State> = MutableLiveData(State.IDLE)
    val game: MutableLiveData<GameState> by lazy {
        MutableLiveData<GameState>(savedState.get<GameState>(SAVED_STATE_KEY) ?: GameState())
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.remove()
    }

    fun waitingForPlayers() {
        repository.resetRepositoryRound()
        repository.loadLobbyInfo(lobbyInfo)

        if (localPlayer?.id == Integer.valueOf(lobbyInfo?.numberOfPlayers ?: "0")) {
            if (isPair) {
                game.value = GameState(
                    currWord = game.value?.currWord,
                    state = NOT_STARTED
                )
            } else {
                game.value = GameState(
                    state = NOT_STARTED
                )
            }
            updateGameState()
            savedState[SAVED_STATE_KEY] = game.value
        }
    }

    fun startGame(words: RandomWords?) {
        repository.deleteHistory()

        var word = ""
        if(words == null)
            word = Words.getRandomOfflineWord()
        else {
            word = words.words.removeFirst()
            repository.loadRandomWords(words)
        }
        when {
            localPlayer != null -> {
                game.value = GameState(
                    currWord = word,
                    state = WAITING_FOR_PLAYERS
                )
            }
            isPair -> {
                game.value = GameState(
                    currWord = word,
                    state = NOT_STARTED
                )
            }
            else -> {
                game.value = GameState(
                    state = NOT_STARTED
                )
            }
        }
        updateGameState()
        savedState[SAVED_STATE_KEY] = game.value
    }

    fun startPlaying() {
        if (!isPair && game.value?.turn == 1) {
            game.value = GameState(
                turn = game.value?.turn,
                state = CHOOSE_WORD
            )
            savedState[SAVED_STATE_KEY] = game.value
            updateGameState()
            return
        }
        if (isGuessing()) {
            game.value = GameState(
                turn = game.value?.turn,
                drawing = game.value?.drawing ?: Drawing(),
                currWord = game.value?.currWord,
                state = GUESS_WORD
            )
        } else {
            game.value = GameState(
                turn = game.value?.turn,
                drawing = Drawing(),
                currWord = game.value?.currWord,
                state = DRAW_WORD
            )
        }
        updateGameState()
        savedState[SAVED_STATE_KEY] = game.value
    }


    fun saveDrawing() {
        repository.saveDrawing(
            lobbyInfo,
            localPlayer,
            game.value?.turn,
            game.value?.drawing,
            currRound
        )

        game.value = GameState(
            turn = game.value?.turn?.plus(1),
            drawing = game.value?.drawing ?: Drawing(),
            state = NOT_STARTED
        )
        updateGameState()
        savedState[SAVED_STATE_KEY] = game.value
    }

    fun saveWord(currWord: String?) {
        repository.saveWord(
            lobbyInfo,
            localPlayer,
            game.value?.turn,
            currWord,
            currRound
        )

        game.value?.turn = game.value?.turn?.plus(1)
        if (checkEndRound()) {
            game.value = GameState(
                turn = game.value?.turn,
                state = END_ROUND
            )
        } else {
            game.value = GameState(
                turn = game.value?.turn,
                state = NOT_STARTED,
                currWord = currWord
            )
        }
        updateGameState()
        savedState[SAVED_STATE_KEY] = game.value
    }

    fun addPoint(point: Point) {
        game.value?.drawing?.plus(point)
    }

    fun clearDrawing() {
        game.value?.drawing?.clear()
    }

    fun isGuessing(): Boolean {
        if (isPair) {
            return game.value?.turn?.rem(2) == 0
        }
        return game.value?.turn?.rem(2) != 0
    }

    fun fetchRandomWords(){
        if (state.value == State.IN_PROGRESS)
            throw IllegalStateException()

        (state as MutableLiveData<State>).value = State.IN_PROGRESS
        val mediator = randomWords as MediatorLiveData<Result<RandomWords>?>
        val source = repository.fetchRandomWords()

        mediator.addSource(source) {
            state.value = State.COMPLETE
            doWorkInAnotherThread(it) { finalResult ->
                randomWords.value = finalResult
                mediator.removeSource(source)
            }
        }
    }

    fun setToIdle() {
        (state as MutableLiveData<State>).value = State.IDLE
    }

    private fun checkEndRound(): Boolean = (game.value?.turn ?: 0) > nrOfPlayers

    fun checkEndGame(): Boolean {
        game.value?.endGame = currRound == nrOfRounds
        return game.value?.endGame ?: true
    }

    fun getCurrPlayerString(): String = "${game.value?.turn}"

    fun getUsername(): String = "${localPlayer?.username}"

    private fun updateGameState() {
        repository.updateGameState(
            game.value ?: throw IllegalStateException(),
            lobbyInfo,
            onSuccess = { game.value = it },
            onError = {}
        )
    }

}

fun doWorkInAnotherThread(result: Result<RandomWords>?, completion: (Result<RandomWords>?) -> Unit) {
    val mainHandler = Handler(Looper.getMainLooper())
    Executors.newSingleThreadExecutor().execute {
        mainHandler.post { completion(result) }
    }
}