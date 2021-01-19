package com.example.drag;

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.drag.databinding.GameModeBinding
import com.example.drag.lobbies.list.LobbyListActivity

const val RANDOM_WORDS = "randomWords"

class GameModeActivity : AppCompatActivity() {


    private val binding: GameModeBinding by lazy { GameModeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.localButton.setOnClickListener {
            startActivity(Intent(this, DragBaseActivity::class.java))

        }

        binding.multiPlayerButton.setOnClickListener {
            startActivity(Intent(this, LobbyListActivity::class.java))
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.about_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.aboutButton -> {
            startActivity(Intent(this, AboutActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}
