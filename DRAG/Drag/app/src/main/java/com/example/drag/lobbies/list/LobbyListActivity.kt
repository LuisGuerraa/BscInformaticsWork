package com.example.drag.lobbies.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drag.DragGameActivity
import com.example.drag.R
import com.example.drag.databinding.ActivityListLobbyBinding
import com.example.drag.lobbies.LobbyInfo
import com.example.drag.lobbies.Player
import com.example.drag.lobbies.create.CreateLobbyActivity
import com.example.drag.lobbies.create.RESULT_EXTRA
import com.example.drag.utils.State


private const val CREATE_CODE = 10001

private const val LOBBY_INFO = "lobbyInfo"

private const val LOCAL_PLAYER = "localPlayer"


class LobbyListActivity : AppCompatActivity() {

    private val binding: ActivityListLobbyBinding by lazy {
        ActivityListLobbyBinding.inflate(layoutInflater)
    }

    private val viewModel: LobbiesViewModel by viewModels()

    private fun lobbySelected(lobby: LobbyInfo) {
        val inflater: LayoutInflater  = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.input_name, null)
        val edt = dialogView.findViewById<EditText>(R.id.user_name)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.enter_lobby_dialog_title, lobby.lobbyPlayerName))
            .setPositiveButton(R.string.enter_lobby_dialog_ok) { _, _ ->
                AlertDialog.Builder(this)
                        .setTitle(R.string.insert_name)
                        .setView(dialogView)
                        .setPositiveButton(R.string.enter_lobby_dialog_ok) { _, _ ->
                            val username : String = edt.text.toString()
                            viewModel.tryAcceptLobby(lobby, username) }
                        .setNegativeButton(R.string.enter_lobby_dialog_cancel, null)
                        .create()
                        .show()
            }
            .setNegativeButton(R.string.enter_lobby_dialog_cancel, null)
            .create()
            .show()
    }

    override fun onStart() {
        super.onStart()
        updateLobbiesList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.lobbiesList.setHasFixedSize(true)
        binding.lobbiesList.layoutManager = LinearLayoutManager(this)

        viewModel.lobbies.observe(this) {
            binding.lobbiesList.adapter = LobbiesListAdapter(it, ::lobbySelected)
            binding.refreshLayout.isRefreshing = false
        }

        binding.refreshLayout.setOnRefreshListener {
            updateLobbiesList()
        }

        binding.createLobbyButton.setOnClickListener {
            startActivityForResult(
                    Intent(this, CreateLobbyActivity::class.java),
                    CREATE_CODE
            )
        }

        viewModel.enrolmentResult.observe(this) {
            if (it.state == State.COMPLETE) {
                if (it.result != null) {
                    startActivity(Intent(this, DragGameActivity::class.java).apply {
                        putExtra(LOBBY_INFO, it.result)
                        putExtra(LOCAL_PLAYER,
                                Player(Integer.valueOf(viewModel.enrolmentResult.value?.result?.currentPlayers ?: ""),
                                        viewModel.localPlayerUsername,
                                        0))
                    })
                } else {
                    Toast.makeText(this, R.string.error_accepting_lobby, Toast.LENGTH_LONG).show()
                }
                viewModel.resetEnrolmentResult()
                binding.lobbiesList.isEnabled = true
            } else {
                binding.lobbiesList.isEnabled = false
            }
        }

    }

    private fun updateLobbiesList() {
        binding.refreshLayout.isRefreshing = true
        viewModel.fetchLobbies()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.lobbies_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.lobbies_list_update -> {
            updateLobbiesList()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CREATE_CODE -> if (resultCode == Activity.RESULT_OK) {
                updateLobbiesList()
                val createdLobby = data?.getParcelableExtra<LobbyInfo>(RESULT_EXTRA)
                startActivity(Intent(this, DragGameActivity::class.java).apply {
                    putExtra(LOCAL_PLAYER, Player(1, createdLobby?.lobbyPlayerName, 0))
                    putExtra(LOBBY_INFO, createdLobby)
                })
                finish()
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}