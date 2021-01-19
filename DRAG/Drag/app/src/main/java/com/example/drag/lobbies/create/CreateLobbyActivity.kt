package com.example.drag.lobbies.create

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.drag.R
import com.example.drag.databinding.ActivityCreateLobbyBinding
import com.example.drag.lobbies.Player
import com.example.drag.utils.State


const val RESULT_EXTRA = "CCA.Result"

class CreateLobbyActivity : AppCompatActivity() {

    private val binding: ActivityCreateLobbyBinding by lazy {
        ActivityCreateLobbyBinding.inflate(layoutInflater)
    }

    private val viewModel: CreateLobbyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.result.observe(this) {
            if (it.state == State.COMPLETE) {
                if (it.result != null) {
                    setResult(Activity.RESULT_OK, Intent().putExtra(RESULT_EXTRA, it.result))
                    finish()
                } else {
                    Toast.makeText(this, R.string.error_creating_lobby, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.create.setOnClickListener { startGame() }
    }

    private fun startGame() {
        val nrOfRounds = binding.nrOfRounds.text.toString()
        val nrOfPlayers = binding.nrOfPlayers.text.toString()

        if(nrOfPlayers == "") {
            toast(getString(R.string.error_missing_nrOfPlayers))
            return
        }

        if(nrOfRounds == "") {
            toast(getString(R.string.error_missing_nrOfRounds))
            return
        }

        if(Integer.valueOf(nrOfPlayers) < 5) {
            binding.nrOfPlayers.text.clear()
            toast(getString(R.string.error_minimum_players))
            return
        }

        if(Integer.valueOf(nrOfRounds) < 1) {
            binding.nrOfRounds.text.clear()
            toast(getString(R.string.error_minimum_rounds))
            return
        }
        viewModel.createLobby(
                binding.name.text.toString(),
                binding.nrOfRounds.text.toString(),
                binding.nrOfPlayers.text.toString(),
                "1"
        )
    }

    private fun toast(msg: CharSequence) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}