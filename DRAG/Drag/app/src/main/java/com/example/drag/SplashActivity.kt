package com.example.drag

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.drag.utils.runDelayed

class SplashViewModel : ViewModel() {

        val scheduleComplete: MutableLiveData<Boolean> by lazy {
            MutableLiveData<Boolean>(false)
        }

        private var isScheduled = false

        fun scheduleTransition(millis: Long) {
            if (!isScheduled) {
                isScheduled = true
                runDelayed(millis) {
                    scheduleComplete.value = true
                }
            }
        }
    }


class SplashActivity : AppCompatActivity() {

        private val contentView by lazy { findViewById<View>(R.id.root) }
        private val viewModel: SplashViewModel by viewModels()

        private fun navigateToFirstActivity() {
            startActivity(Intent(this, GameModeActivity::class.java))
            finish()
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_splash)

            contentView.setOnClickListener {
                navigateToFirstActivity()
            }

            viewModel.scheduleComplete.observe(this) { shouldNavigate ->
                if (shouldNavigate) {
                    navigateToFirstActivity()
                }
            }

            viewModel.scheduleTransition(30000)
        }
    }

