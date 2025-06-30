package nikmax.shopinglist.core.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
internal interface ItemsDao {
    @Query("SELECT * from item")
    suspend fun getAll(): List<ItemEntity>
    
    @Insert
    suspend fun insert(item: ItemEntity)
    
    @Delete
    suspend fun delete(item: ItemEntity)
    
    @Update
    suspend fun update(item: ItemEntity)
}
