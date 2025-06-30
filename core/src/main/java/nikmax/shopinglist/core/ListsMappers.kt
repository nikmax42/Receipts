package nikmax.shopinglist.core

import nikmax.shopinglist.core.data.ListData
import nikmax.shopinglist.core.data.room.ListEntity
import nikmax.shopinglist.core.data.room.ListWithItems
import nikmax.shopinglist.core.ui.ListUi

object ListsMappers {
    internal fun ListWithItems.mapToData(): ListData {
        return ListData(
            name = this.list.name,
            creationDate = this.list.creationDate,
            id = this.list.id,
            items = this.items.size,
            completedItems = this.items.count { it.isComplete },
            totalPrice = this.items.sumOf { it.price * it.amount },
            completedPrice = this.items.sumOf { if (it.isComplete) it.price * it.amount else 0f },
            currency = this.list.currency
        )
    }
    
    internal fun ListData.mapToEntity(): ListEntity {
        return ListEntity(
            name = this.name,
            creationDate = this.creationDate,
            id = this.id,
            currency = this.currency
        )
    }
    
    fun ListData.mapToUi(): ListUi {
        return ListUi(
            name = this.name,
            creationDate = this.creationDate,
            id = this.id,
            items = this.items,
            completedItems = this.completedItems,
            totalPrice = this.totalPrice,
            completedPrice = this.completedPrice,
            currency = this.currency
        )
    }
    
    fun ListUi.mapToData(): ListData {
        return ListData(
            name = this.name,
            creationDate = this.creationDate,
            id = this.id,
            items = this.items,
            completedItems = this.completedItems,
            totalPrice = this.totalPrice,
            completedPrice = this.completedPrice,
            currency = this.currency
        )
    }
}
