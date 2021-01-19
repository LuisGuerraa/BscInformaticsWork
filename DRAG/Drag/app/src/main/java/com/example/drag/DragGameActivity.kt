package com.example.drag

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.drag.databinding.ActivityGameBinding
import com.example.drag.game.data.Point
import com.example.drag.lobbies.LobbyInfo
import com.example.drag.lobbies.Player
import com.example.drag.repo.RandomWords
import com.example.drag.repo.State
import com.example.drag.utils.runDelayed

const val ACCEPTED_LOBBY_EXTRA = "lobbyInfo"

const val LOCAL_PLAYER = "localPlayer"


class DragGameActivity : AppCompatActivity(){

    private val binding: ActivityGameBinding by lazy { ActivityGameBinding.inflate(layoutInflater) }

    private val localPlayer: Player? by lazy {intent.getParcelableExtra(LOCAL_PLAYER)}
    private val lobbyInfo: LobbyInfo? by lazy{intent.getParcelableExtra(ACCEPTED_LOBBY_EXTRA)}

    private val viewModel: DragViewModel by viewModels {
        @Suppress("UNCHECKED_CAST")
        object: ViewModelProvider.Factory {
            override fun <VM : ViewModel?> create(modelClass: Class<VM>): VM {
                return DragViewModel(
                        localPlayer,
                        lobbyInfo,
                        application,
                        SavedStateHandle()
                ) as VM
            }
        }
    }

    private fun init() {
        if(localPlayer?.id == viewModel.game.value?.turn || localPlayer == null) {
            viewModel.randomWords.observe(this) {
                handleRandomWordsCompletion(it)
            }
            viewModel.fetchRandomWords()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun notStarted() {
        if(lobbyInfo != null) {
            if (localPlayer?.id != viewModel.game.value?.turn) {
                waitingScreen()
                return
            }
            binding.viewTitle.text = baseContext.getString(R.string.playerTurn, viewModel.getUsername())
        } else
            binding.viewTitle.text = baseContext.getString(R.string.playerTurn, viewModel.getCurrPlayerString())
        binding.wordInput.visibility = VISIBLE
        binding.startButton.visibility = VISIBLE
        binding.startButton.isEnabled = true
        binding.finishButton.visibility = INVISIBLE
        binding.clearButton.visibility = INVISIBLE
        binding.wordInput.isEnabled = false
        binding.dragGameView.setOnTouchListener(null)
        binding.dragGameView.visibility = INVISIBLE
        binding.wordInput.visibility = INVISIBLE
        binding.startButton.setOnClickListener {
            viewModel.startPlaying()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun drawWord() {
        if(lobbyInfo != null) {
            if (localPlayer?.id != viewModel.game.value?.turn) {
                waitingScreen()
                return
            }
            binding.viewTitle.text = baseContext.getString(R.string.playerTurn, viewModel.getUsername())
        } else
            binding.viewTitle.text = baseContext.getString(R.string.playerTurn, viewModel.getCurrPlayerString())
        binding.wordInput.visibility = INVISIBLE
        binding.startButton.visibility = INVISIBLE
        binding.finishButton.visibility = VISIBLE
        binding.clearButton.visibility = VISIBLE
        binding.clearButton.isEnabled = true

        binding.viewTitle.text = baseContext.getString(R.string.write_word, viewModel.game.value?.currWord)
        binding.dragGameView.visibility = VISIBLE


        binding.clearButton.setOnClickListener {
            viewModel.clearDrawing()
            binding.dragGameView.triggerView(viewModel.game.value?.drawing)
        }

        binding.dragGameView.setOnTouchListener { _, event ->
            val x = event.x / binding.dragGameView.width
            val y = event.y / binding.dragGameView.height
            when (event.action) {
                MotionEvent.ACTION_DOWN -> { addPoint(Point(x, y)) }
                MotionEvent.ACTION_MOVE -> { addPoint(Point(x, y)) }
            }
            true
        }
        binding.finishButton.setOnClickListener{
            viewModel.saveDrawing()
        }

        runDelayed(60000) {
            viewModel.saveDrawing()
        }

    }

    private fun guessWord() {
        if(lobbyInfo != null) {
            if (localPlayer?.id != viewModel.game.value?.turn) {
                waitingScreen()
                return
            }
            binding.viewTitle.text = baseContext.getString(R.string.playerTurn, viewModel.getUsername())
        } else
            binding.viewTitle.text = baseContext.getString(R.string.playerTurn, viewModel.getCurrPlayerString())
        binding.startButton.visibility = INVISIBLE
        binding.finishButton.visibility = VISIBLE
        binding.clearButton.visibility = INVISIBLE
        binding.dragGameView.visibility = VISIBLE
        binding.wordInput.isEnabled = true
        binding.wordInput.visibility = VISIBLE
        binding.viewTitle.text = baseContext.getString(R.string.guessWord)

        binding.wordInput.text.clear()
        binding.dragGameView.triggerView(viewModel.game.value?.drawing)

        binding.finishButton.setOnClickListener{
            viewModel.saveWord(binding.wordInput.text.toString())
            binding.dragGameView.triggerView(null)
        }

        runDelayed(60000) {
            viewModel.saveWord(binding.wordInput.text.toString())
            binding.dragGameView.triggerView(null)
        }

    }

    private fun chooseWord() {
        if(lobbyInfo != null) {
            if (localPlayer?.id != viewModel.game.value?.turn) {
                waitingScreen()
                return
            }
            binding.viewTitle.text = baseContext.getString(R.string.playerTurn, viewModel.getUsername())
        } else
            binding.viewTitle.text = baseContext.getString(R.string.playerTurn, viewModel.getCurrPlayerString())
        binding.finishButton.visibility = INVISIBLE
        binding.dragGameView.visibility = INVISIBLE
        binding.clearButton.visibility = INVISIBLE
        binding.wordInput.isEnabled = true
        binding.viewTitle.text = baseContext.getString(R.string.chooseAWord)
        binding.wordInput.visibility = VISIBLE
        binding.startButton.setOnClickListener {
            binding.finishButton.visibility = VISIBLE
            viewModel.saveWord(binding.wordInput.text.toString())
        }
    }

    private fun endRound() {
        val intent : Intent = Intent(this, EndRoundActivity::class.java).apply{
            putExtra("endGame", viewModel.checkEndGame())
            putExtra("localPlayer", localPlayer)
            putExtra("lobbyInfo", lobbyInfo)
        }
        finish()
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // When view  is changed to landscape I have to trigger the view with the drawing that was already created
        binding.dragGameView.triggerView(viewModel.game.value?.drawing)


        viewModel.game.observe(this) {
            when(viewModel.game.value?.state) {
                GameState.State.INIT -> init()
                GameState.State.NOT_STARTED -> notStarted()
                GameState.State.DRAW_WORD -> drawWord()
                GameState.State.GUESS_WORD -> guessWord()
                GameState.State.END_ROUND -> endRound()
                GameState.State.CHOOSE_WORD -> chooseWord()
                GameState.State.WAITING_FOR_PLAYERS -> waitingForPlayers()
            }
        }

    }

    private fun addPoint(point: Point) {
        viewModel.addPoint(point);
        binding.dragGameView.triggerView(viewModel.game.value?.drawing)
    }

    private fun waitingScreen() {
        binding.clearButton.visibility = INVISIBLE
        binding.dragGameView.visibility = INVISIBLE
        binding.wordInput.visibility = INVISIBLE
        binding.finishButton.visibility = INVISIBLE
        binding.startButton.visibility = INVISIBLE
        binding.viewTitle.text = getText(R.string.waiting_Txt)
    }

    private fun waitingForPlayers() {
        binding.clearButton.visibility = INVISIBLE
        binding.dragGameView.visibility = INVISIBLE
        binding.wordInput.visibility = INVISIBLE
        binding.finishButton.visibility = INVISIBLE
        binding.startButton.visibility = INVISIBLE
        binding.viewTitle.text = getText(R.string.waiting_for_other_players)
        viewModel.waitingForPlayers()
    }

    private fun handleRandomWordsCompletion(it: Result<RandomWords>?) {
        if (viewModel.state.value == State.COMPLETE && it?.isSuccess == true) {
            viewModel.startGame(it.getOrNull())
        }
        else
            displayError(it)
    }

    private fun displayError(it: Result<RandomWords>?) {
        Toast.makeText(
            this,
            getString(R.string.error_getting_words),
            Toast.LENGTH_LONG
        ).show()
        viewModel.startGame(it?.getOrNull())
    }

}