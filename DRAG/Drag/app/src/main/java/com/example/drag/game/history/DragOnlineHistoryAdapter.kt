package com.example.drag.game.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.drag.DragGameView
import com.example.drag.R
import com.example.drag.game.data.Drawing

class DragOnlineHistoryAdapter internal constructor(private val context: Context) :
    RecyclerView.Adapter<DragOnlineHistoryAdapter.ItemViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var items = emptyList<PlayerMoveItem>()

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerIdView = itemView.findViewById<TextView>(R.id.player)
        var wordView = itemView.findViewById<TextView>(R.id.player_word)
        var drawView = itemView.findViewById<DragGameView>(R.id.player_drawing)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = inflater.inflate(R.layout.word_draw_list_layout, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.playerIdView.text = item.name
        if (item.word == null) {
            holder.drawView.triggerView(item.drawing)
            holder.wordView = null
        } else {
            holder.wordView.text = item.word
            holder.drawView.visibility = INVISIBLE
        }
    }

    override fun getItemCount() = items.size

    internal fun setItems(items: List<PlayerMoveItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    data class PlayerMoveItem(var name:String, var word: String?, var drawing: Drawing?)

}
