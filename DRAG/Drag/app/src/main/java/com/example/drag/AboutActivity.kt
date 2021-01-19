package com.example.drag

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.drag.databinding.ActivityAboutBinding
import com.example.drag.databinding.GameModeBinding

class AboutActivity : AppCompatActivity(){

    private val binding: ActivityAboutBinding by lazy { ActivityAboutBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
