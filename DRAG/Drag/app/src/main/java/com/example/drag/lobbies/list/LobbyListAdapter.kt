package com.example.drag.lobbies.list

import android.animation.ValueAnimator
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.drag.R
import com.example.drag.lobbies.LobbyInfo


class LobbyViewHolder(private val view: ViewGroup) : RecyclerView.ViewHolder(view) {

    private val lobbyPlayerName: TextView = view.findViewById(R.id.lobby_player_name)
    private val lobbyNrOfPlayers: TextView = view.findViewById(R.id.lobby_nr_of_players)
    private val lobbyNrOfRounds: TextView = view.findViewById(R.id.lobby_nr_of_rounds)

    private fun startAnimation(onAnimationEnd: () -> Unit) {

        val animation = ValueAnimator.ofArgb(
            ContextCompat.getColor(view.context, R.color.list_item_background),
            ContextCompat.getColor(view.context, R.color.list_item_background_selected),
            ContextCompat.getColor(view.context, R.color.list_item_background)
        )

        animation.addUpdateListener { animator ->
            val background = view.background as GradientDrawable
            background.setColor(animator.animatedValue as Int)
        }

        animation.duration = 400
        animation.start()

        animation.doOnEnd { onAnimationEnd() }
    }


    fun bindTo(lobby: LobbyInfo?, itemSelectedListener: (LobbyInfo) -> Unit) {

        lobbyPlayerName.append(lobby?.lobbyPlayerName ?: "")
        lobbyNrOfPlayers.append(lobby?.numberOfPlayers.toString())
        lobbyNrOfRounds.append(lobby?.numberOfRounds.toString())

        if (lobby != null)
            view.setOnClickListener {
                startAnimation {
                    itemSelectedListener(lobby)
                }
            }
    }
}


class LobbiesListAdapter(
    private val contents: List<LobbyInfo>,
    private val itemSelectedListener: (LobbyInfo) -> Unit) :
    RecyclerView.Adapter<LobbyViewHolder>() {

    override fun onBindViewHolder(holder: LobbyViewHolder, position: Int) {
        holder.bindTo(contents[position], itemSelectedListener)
    }

    override fun getItemCount(): Int = contents.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LobbyViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.lobby_list, parent, false) as ViewGroup

        return LobbyViewHolder(view)
    }
}