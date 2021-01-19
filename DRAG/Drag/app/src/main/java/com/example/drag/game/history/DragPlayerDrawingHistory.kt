package com.example.drag.game.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.drag.game.data.Drawing
import com.example.drag.game.data.Point

@Entity(tableName = "Drawing")

// table with the player, his drawing or word and the number of the round
data class DragPlayerDrawingHistoryItem(var playerID: String, var drawing: Drawing?, var wordGuessed : String?, var round : Int) {
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}

@Dao
interface DragPlayerDrawingDao {

    @Insert
    fun insertItem(item: DragPlayerDrawingHistoryItem)

    @Query("DELETE FROM Drawing")
    fun deleteAllItems()

    @Query("SELECT * FROM Drawing")
    fun getAllItems() : LiveData<List<DragPlayerDrawingHistoryItem>>
}

@Database(entities = [DragPlayerDrawingHistoryItem::class], version = 2)
@TypeConverters(Converters::class)
abstract class DragPlayerDrawing : RoomDatabase() {
    abstract fun dragPlayerDrawingDao() : DragPlayerDrawingDao
}

class Converters {
    @TypeConverter
    fun toDrawing(points: String) : Drawing {
        if (points != "") {
            var res: MutableList<Point> = mutableListOf()
            val pointsAsString: List<String> = points.split(";")
            Log.d("pointsAsString", pointsAsString.toString())
            val iterator: Iterator<String> = pointsAsString.iterator()
            var currString: String
            var coordinates: List<String>
            while (iterator.hasNext()) {
                currString = iterator.next()
                coordinates = currString.split(",")
                Log.d("StringPrint", coordinates.toString())
                res.add(
                    Point(
                        coordinates[0].toFloat(),
                        coordinates[1].toFloat()
                    )
                )

            }
            return Drawing(res)
        }
        return Drawing()
    }

    @TypeConverter
    fun fromDrawing(drawing: Drawing) : String {
        var res = ""
        if (!drawing.isEmpty()) {
            val iterator = drawing.iterator()
            var curr: Point
            while (iterator.hasNext()) {
                curr = iterator.next()
                Log.d("PointPrint", curr.coordinatesToString())
                res = res.plus((curr.coordinatesToString() + ";"))
                Log.d("CurrResValue", res)
            }
            res = res.substring(0, res.length - 1)
        }
        return res
    }
}