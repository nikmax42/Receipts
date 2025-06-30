package nikmax.shopinglist.core.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
internal interface ListsDao {
    @Query("SELECT * from list")
    suspend fun getAllLists(): List<ListEntity>
    
    @Insert
    suspend fun insert(list: ListEntity)
    
    @Delete
    suspend fun delete(list: ListEntity)
    
    @Update
    suspend fun update(list: ListEntity)
    
    @Transaction
    @Query("SELECT * FROM list")
    suspend fun getAllListsWithItems(): List<ListWithItems>
    
    //@Delete
    //suspend fun deleteWithItems(listWithItems: ListWithItems)
    
}
