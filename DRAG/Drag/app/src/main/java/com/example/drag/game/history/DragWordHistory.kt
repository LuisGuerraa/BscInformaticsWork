package com.example.drag.game.history

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "Words")

// table with the game words
data class DragWordHistoryItem(var word: String) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

@Dao
interface DragWordDao {

    @Insert
    fun insertItem(item: DragWordHistoryItem)

    @Query("SELECT * FROM Words")
    fun getAllItems(): LiveData<List<DragWordHistoryItem>>
}

@Database(entities = [DragWordHistoryItem::class], version = 2)
@TypeConverters(Converters::class)
abstract class DragWord : RoomDatabase() {
    abstract fun dragWordDao(): DragWordDao
}