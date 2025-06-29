package nikmax.shopinglist.core.data.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import nikmax.shopinglist.core.Currency

@Entity(
    tableName = "item",
    foreignKeys = [
        //delete items when parent list deleted
        ForeignKey(
            entity = ListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class ItemEntity(
    val name: String,
    val price: Float,
    val amount: Int,
    val isComplete: Boolean,
    val creationDate: Long,
    val listId: Int,
    val currency: Currency,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
