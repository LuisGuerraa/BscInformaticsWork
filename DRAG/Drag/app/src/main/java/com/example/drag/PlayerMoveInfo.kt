package com.example.drag

import com.example.drag.game.data.Drawing
import com.example.drag.game.data.Point
import com.example.drag.game.history.DragOnlineHistoryAdapter


data class PlayerMoveInfoDTO(val points: List<String>, val playerName: String, val word: String) {

    fun toPlayerMove() = DragOnlineHistoryAdapter.PlayerMoveItem(playerName, word, Drawing(convertStringToPoint(points).toMutableList()))
}

fun convertStringToPoint(points: List<String>) : List<Point>{
    if (points.isNotEmpty()) {
        var res: MutableList<Point> = mutableListOf()
        val iterator: Iterator<String> = points.iterator()
        var currString: String
        var coordinates: List<String>
        var x : List<String>
        var y : List<String>
        while (iterator.hasNext()) {
            currString = iterator.next()
            coordinates = currString.split(",")
            x = coordinates[0].split(":")
            y = coordinates[0].split(":")
            res.add(
                Point(
                    x[1].toFloat(),
                    y[1].toFloat()
                )
            )

        }
        return res
    }
    return emptyList()
}