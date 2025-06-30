package nikmax.shopinglist.core.data.room

import androidx.room.Embedded
import androidx.room.Relation

internal data class ListWithItems(
    @Embedded
    val list: ListEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "listId"
    )
    val items: List<ItemEntity>
)
