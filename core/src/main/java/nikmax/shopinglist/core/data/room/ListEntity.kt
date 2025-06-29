package nikmax.shopinglist.core.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import nikmax.shopinglist.core.Currency

@Entity(tableName = "list")
internal data class ListEntity(
    val name: String,
    val creationDate: Long,
    val currency: Currency,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
