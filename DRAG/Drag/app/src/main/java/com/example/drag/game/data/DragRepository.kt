package com.example.drag.game.data
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.example.drag.*
import com.example.drag.game.history.*
import com.example.drag.lobbies.LobbyInfo
import com.example.drag.lobbies.Move
import com.example.drag.lobbies.Player
import com.example.drag.repo.*
import com.example.drag.repo.RANDOM_WORDS
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.concurrent.Executors

private const val NUMBER_OF_PLAYERS_KEY = "NumberOfPlayersKey"
private const val NUMBER_OF_ROUNDS_KEY = "NumberOfRoundsKey"
private const val CURR_ROUND = "CurrRound"
private const val LOCAL_PLAYER_NAME = "LocalPlayerName"
private const val LOBBIES_COLLECTION = "lobbies"
private const val MOVES_COLLECTION = "moves"
private const val GAMES_COLLECTION = "games"
private const val NAME = "Name"
private const val NR_OF_ROUNDS = "NumberOfRounds"
private const val NR_OF_PLAYERS = "NumberOfPlayers"
private const val CURRENT_PLAYERS = "CurrentPlayers"
private const val PLAYER_NAME = "PlayerName"
private const val DRAWING = "Drawing"
private const val WORD = "Word"
private const val GAME_STATE_KEY = "game"
private const val LOBBY_INFO_KEY = "lobby"


private fun QueryDocumentSnapshot.toLobbyInfo() =
        LobbyInfo(
                id,
                data[NAME] as String,
                data[NR_OF_ROUNDS] as String,
                data[NR_OF_PLAYERS] as String,
                data[CURRENT_PLAYERS] as String
        )

class DragRepository(
        private val sharedPreferences: SharedPreferences,
        private val drawingDatabase: DragPlayerDrawing,
        private val wordsDatabase: DragWord,
        private val mapper: ObjectMapper,
        private val queue: RequestQueue
    ) {

    private val executor = Executors.newSingleThreadExecutor()

    private val dragPlayerDrawingDao = drawingDatabase.dragPlayerDrawingDao()
    private val dragWordDao = wordsDatabase.dragWordDao()

    /*
    * DragDrawingHistory methods
    */
    val allHistoryItems
        get() = dragPlayerDrawingDao.getAllItems()

    fun insertHistoryItem(item: DragPlayerDrawingHistoryItem) {
        executor.submit { dragPlayerDrawingDao.insertItem(item) }
    }

    fun deleteHistory() {
        executor.submit { dragPlayerDrawingDao.deleteAllItems() }
    }

    /*
    * DragWordHistory methods
    */
    val allWordItems
        get() = dragWordDao.getAllItems()

    fun loadRandomWords(words: RandomWords?) {
        words?.words?.forEach { word ->
            executor.submit{
                dragWordDao.insertItem(DragWordHistoryItem(word))
            }
        }
    }

    fun resetRepositoryRound() {
        currRound = 1
    }

    fun resetRepositoryPlayer() {
        localPlayerUsername = null
    }

    var numberOfPlayers: Int

        get() = sharedPreferences.getInt(NUMBER_OF_PLAYERS_KEY, 1)
        private set(value) {
            sharedPreferences
                .edit()
                .putInt(NUMBER_OF_PLAYERS_KEY, value)
                .apply()
        }

    var numberOfRounds: Int
        get() = sharedPreferences.getInt(NUMBER_OF_ROUNDS_KEY, 1)
        private set(value) {
            sharedPreferences
                .edit()
                .putInt(NUMBER_OF_ROUNDS_KEY, value)
                .apply()
        }

    var currRound: Int
        get() = sharedPreferences.getInt(CURR_ROUND, 1)
        private set(value) {
            sharedPreferences
                    .edit()
                    .putInt(CURR_ROUND, value)
                    .apply()
        }

    var localPlayerUsername: String?
        get() = sharedPreferences.getString(LOCAL_PLAYER_NAME, "")
        private set(username) {
            sharedPreferences
                    .edit()
                    .putString(LOCAL_PLAYER_NAME, username)
                    .apply()
        }

    fun loadStartGameInfo(numberOfPlayers: Int, numberOfRounds: Int) {
        this.numberOfPlayers = numberOfPlayers
        this.numberOfRounds = numberOfRounds
    }

    fun loadCurrentRound(currentRound: Int) {
        this.currRound = currentRound
    }

    fun loadLocalPlayerUsername(username: String) {
        this.localPlayerUsername = username
    }

    fun loadLobbyInfo(lobbyInfo: LobbyInfo?) {
        numberOfPlayers = Integer.valueOf(lobbyInfo?.numberOfPlayers ?: "")
        numberOfRounds = Integer.valueOf(lobbyInfo?.numberOfRounds ?: "")
    }

    fun fetchLobbies(onSuccess: (List<LobbyInfo>) -> Unit, onError: (Exception) -> Unit) {
        Firebase.firestore.collection(LOBBIES_COLLECTION)
                .get()
                .addOnSuccessListener { result ->
                    onSuccess(result.map { it.toLobbyInfo() }.toList())
                }
                .addOnFailureListener {
                    onError(it)
                }
    }

    fun publishLobby(
            name: String,
            nrOfRounds: String,
            nrOfPlayers: String,
            currentPlayers: String,
            onSuccess: (LobbyInfo) -> Unit,
            onError: (Exception) -> Unit) {

        Firebase.firestore.collection(LOBBIES_COLLECTION)
                .add(hashMapOf(NAME to name, NR_OF_ROUNDS to nrOfRounds, NR_OF_PLAYERS to nrOfPlayers, CURRENT_PLAYERS to currentPlayers))
                .addOnSuccessListener {
                    Log.d("DocumentID", it.id)
                    onSuccess(LobbyInfo(it.id, name, nrOfRounds, nrOfPlayers, currentPlayers))
                }
                .addOnFailureListener { onError(it) }
    }

    fun unpublishLobby(lobbyId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        Firebase.firestore
                .collection(LOBBIES_COLLECTION)
                .document(lobbyId)
                .delete()
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener(onError)
    }

    fun unpublishMoves(lobbyId: String?) {
        if (lobbyId != null) {
            Firebase.firestore
                .collection(MOVES_COLLECTION)
                .document(lobbyId)
                .delete()
        }
    }

    fun subscribeTo(
            lobbyId: String?,
            onSubscriptionError: (Exception) -> Unit,
            onStateChanged: (GameState) -> Unit
    ): ListenerRegistration? {
        if(lobbyId == null){
            return null
        }
        return Firebase.firestore
                .collection(GAMES_COLLECTION)
                .document(lobbyId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        onSubscriptionError(error)
                        return@addSnapshotListener
                    }

                    if (snapshot?.exists() == true) {
                        val gameDTO = mapper.readValue(
                                snapshot.get(GAME_STATE_KEY) as String,
                                GameStateDTO::class.java
                        )
                        onStateChanged(gameDTO.toGameState())
                    }
                }
    }

    fun getAndConvertPlayerMove(
        lobbyId: String?,
        ): Task<DocumentSnapshot> {
            return Firebase.firestore
                .collection(MOVES_COLLECTION)
                .document(lobbyId?: "")
                .get()
                .addOnSuccessListener { snapshot ->
                    val playerMoveInfoDTO = PlayerMoveInfoDTO(
                        snapshot.get(DRAWING) as List<String>,
                        snapshot.get(PLAYER_NAME) as String,
                        snapshot.get(WORD) as String
                    )

                }


    }

    fun updateGameState(
            game: GameState?,
            lobby: LobbyInfo?,
            onSuccess: (GameState?) -> Unit,
            onError: (Exception) -> Unit
    ) {
        if(lobby == null) return
        val gameStateBlob = mapper.writeValueAsString(game?.toGameStateDTO())
        val lobbyBlob = mapper.writeValueAsString(lobby)

        Firebase.firestore.collection(GAMES_COLLECTION)
                .document(lobby.id)
                .set(hashMapOf(
                        GAME_STATE_KEY to gameStateBlob,
                        LOBBY_INFO_KEY to lobbyBlob
                ))
                .addOnSuccessListener { onSuccess(game) }
                .addOnFailureListener { onError(it) }
    }

    fun updateLobbyInfo(
            lobby: LobbyInfo?,
            onSuccess: (LobbyInfo?) -> Unit,
            onError: (Exception) -> Unit
    ) {

        if(lobby == null) return

        Firebase.firestore.collection(LOBBIES_COLLECTION)
                .document(lobby.id)
                .set(hashMapOf(
                        NAME to lobby.lobbyPlayerName,
                        NR_OF_ROUNDS to lobby.numberOfRounds,
                        NR_OF_PLAYERS to lobby.numberOfPlayers,
                        CURRENT_PLAYERS to lobby.currentPlayers
                ))
                .addOnSuccessListener { onSuccess(lobby) }
                .addOnFailureListener { onError(it) }
    }


    fun fetchRandomWords(): LiveData<Result<RandomWords>?> {
        val result = MutableLiveData<Result<RandomWords>?>()

        if(allWordItems.value?.isNotEmpty() == true) {
            result.value = Result.success(mapper(allWordItems.value?: mutableListOf()))
        } else {
            val request = GetRandomWordsRequest(
                "$BASE_URL$RANDOM_WORDS$PARAMETERS${BuildConfig.api_key}",
                mapper,
                {
                    result.value = Result.success(modelFromDTO(it))
                },
                {
                    result.value = Result.failure(it)
                }
            )

            queue.add(request)
        }
        return result
    }

    private fun updateMove(lobby: LobbyInfo?, localPlayer: Player, drawing: Drawing?, word: String?) {
        if(lobby == null) return
        Firebase.firestore.collection(MOVES_COLLECTION)
            .document(lobby.id)
            .collection("moves")
            .add(
                Move(
                    lobby.id,
                    localPlayer.username,
                    currRound,
                    drawing?.toPointsDTO()?.points ?: emptyList(),
                    word ?: ""
                )
            )
    }

    fun saveDrawing(lobby: LobbyInfo?, localPlayer: Player?, turn: Int?, drawing: Drawing?, currRound: Int) {
        if(localPlayer == null) {
            insertHistoryItem(
                DragPlayerDrawingHistoryItem(
                    "Player${turn}",
                    drawing,
                    null,
                    currRound
                )
            )
        } else {
            updateMove(lobby, localPlayer, drawing, null)
        }
    }

    fun saveWord(
        lobbyInfo: LobbyInfo?,
        localPlayer: Player?,
        turn: Int?,
        currWord: String?,
        currRound: Int
    ) {
        if(localPlayer == null) {
            insertHistoryItem(
                DragPlayerDrawingHistoryItem(
                    "Player${turn}",
                    Drawing(),
                    currWord,
                    currRound
                )
            )
        } else {
            updateMove(lobbyInfo, localPlayer, Drawing(), currWord)
        }
    }

    private fun mapper(list: List<DragWordHistoryItem>): RandomWords {
        return RandomWords(list.map { item -> item.word }.toMutableList())
    }

}


