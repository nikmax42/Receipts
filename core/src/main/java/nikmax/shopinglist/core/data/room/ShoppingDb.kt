package nikmax.shopinglist.core.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ListEntity::class,
        ItemEntity::class
    ],
    version = 1
)
internal abstract class ShoppingDb : RoomDatabase() {
    abstract fun listsDao(): ListsDao
    abstract fun itemsDao(): ItemsDao
}
