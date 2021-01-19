package com.example.drag

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drag.databinding.ActivityEndBinding
import com.example.drag.game.history.DragLocalHistoryAdapter
import com.example.drag.game.history.DragHistoryViewModel
import com.example.drag.game.history.DragOnlineHistoryAdapter
import com.example.drag.lobbies.LobbyInfo
import com.example.drag.lobbies.Player

class EndRoundActivity : AppCompatActivity() {

    private val binding: ActivityEndBinding by lazy { ActivityEndBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(this)
                .get(DragHistoryViewModel::class.java)
    }

    private val localPlayer: Player? by lazy {intent.getParcelableExtra(LOCAL_PLAYER)}
    private val lobbyInfo: LobbyInfo? by lazy{intent.getParcelableExtra(ACCEPTED_LOBBY_EXTRA)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val endGame = intent.getBooleanExtra("endGame", false)
        if (endGame) {
            binding.nextRoundButton.text = baseContext.getString(R.string.gameOver)
        } else {
            binding.nextRoundButton.text = baseContext.getString(R.string.nextRound)
        }
        //observeItems(lobbyInfo)

        if(localPlayer != null && localPlayer?.id == 1 ) {
            viewModel.resetGameState(lobbyInfo)
        }
        val historyAdapter = DragLocalHistoryAdapter(this)
        binding.listView.adapter = historyAdapter
        binding.listView.layoutManager = LinearLayoutManager(this)

        viewModel.history.observe(this, { items ->
            historyAdapter.setItems(items)
            items.forEach { item -> Log.d("Drag::history", "${item.id} : $item") }
        })
        var currRound = viewModel.getCurrRound()
        binding.currRoundText.text = baseContext.getString(R.string.endRound, currRound)

//        if(localPlayer != null && lobbyInfo != null && localPlayer?.id == Integer.valueOf(lobbyInfo?.numberOfPlayers?:"")) {
//            viewModel.loadCurrentRound(++currRound)
//        }
//        if (localPlayer == null) {
//            viewModel.loadCurrentRound(++currRound)
//        }
        viewModel.loadCurrentRound(++currRound)

        binding.nextRoundButton.setOnClickListener {
            viewModel.deleteHistory(localPlayer, lobbyInfo?.id)
            if (endGame) {
                viewModel.resetRepositoryRound()
                val i = Intent(this, GameModeActivity::class.java)
                finish()
                startActivity(i)
            } else {
                val i = Intent(this, DragGameActivity::class.java).apply {
                    putExtra("localPlayer", localPlayer)
                    putExtra("lobbyInfo", lobbyInfo)
                }
                finish()
                startActivity(i);
            }
        }
    }

    private fun observeItems(lobby: LobbyInfo?) {
        if(lobby == null) {
            val historyAdapter = DragLocalHistoryAdapter(this)
            binding.listView.adapter = historyAdapter
            binding.listView.layoutManager = LinearLayoutManager(this)

            viewModel.history.observe(this, { items ->
                historyAdapter.setItems(items)
                items.forEach { item -> Log.d("Drag::history", "${item.id} : $item") }
            })
        } else {
            val historyAdapter = DragOnlineHistoryAdapter(this)
            binding.listView.adapter = historyAdapter
            binding.listView.layoutManager = LinearLayoutManager(this)
            var items = mutableListOf<DragOnlineHistoryAdapter.PlayerMoveItem>()

            //application.repository.getAndConvertPlayerMove()
        }

    }

}