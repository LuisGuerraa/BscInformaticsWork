package com.example.drag.game.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class PointsDTO(val points: List<Point>) {

    //toDrawing() = Drawing
}

@Parcelize
data class Point(val x: Float, val y: Float) : Parcelable {
    fun coordinatesToString() : String{
        return "$x,$y"
    }
}

@Parcelize
data class Drawing(private val drawing: MutableList<Point> = mutableListOf()) : Iterable<Point>, Parcelable {

    override fun iterator(): Iterator<Point> = drawing.iterator()

    operator fun plus(point: Point) = drawing.add(point)

    fun clear() {
        drawing.clear()
    }

    fun isEmpty() = drawing.size == 0

    fun toPointsDTO() = PointsDTO(drawing)

}