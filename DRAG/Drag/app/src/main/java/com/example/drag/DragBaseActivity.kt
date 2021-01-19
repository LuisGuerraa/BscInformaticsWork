package com.example.drag

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.drag.databinding.ActivityMainBinding
import com.example.drag.utils.confinedLazy

class DragBaseActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val dragApp by lazy { application as DragApp }
    private val repository by lazy { dragApp.repository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.startButton.setOnClickListener { startGame()}
    }

    private fun startGame() {
        val nrOfPlayers = binding.playerNrText.text.toString()
        val nrOfRounds = binding.roundNrText.text.toString()

        if(nrOfPlayers == "") {
            toast(getString(R.string.error_missing_nrOfPlayers))
            return
        }
        if(nrOfRounds == "") {
            toast(getString(R.string.error_missing_nrOfRounds))
            return
        }

        if(Integer.valueOf(nrOfPlayers) < 5) {
            binding.playerNrText.text.clear()
            toast(getString(R.string.error_minimum_players))
            return
        }
        if(Integer.valueOf(nrOfRounds) < 1) {
            binding.roundNrText.text.clear()
            toast(getString(R.string.error_minimum_rounds))
            return
        }

        repository.loadStartGameInfo(Integer.valueOf(nrOfPlayers), Integer.valueOf(nrOfRounds))

        val i : Intent = Intent(this, DragGameActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun toast(msg: CharSequence) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}