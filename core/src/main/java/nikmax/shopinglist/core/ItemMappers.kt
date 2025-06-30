package nikmax.shopinglist.core

import nikmax.shopinglist.core.data.ItemData
import nikmax.shopinglist.core.data.room.ItemEntity
import nikmax.shopinglist.core.ui.ItemUi

object ItemMappers {
    internal fun ItemEntity.mapToData(): ItemData {
        return ItemData(
            name = this.name,
            price = this.price,
            amount = this.amount,
            isComplete = this.isComplete,
            creationDate = this.creationDate,
            listId = this.listId,
            id = this.id,
            currency = this.currency
        )
    }
    
    internal fun ItemData.mapToEntity(): ItemEntity {
        return ItemEntity(
            name = this.name,
            price = this.price,
            amount = this.amount,
            isComplete = this.isComplete,
            creationDate = this.creationDate,
            listId = this.listId,
            id = this.id,
            currency = this.currency
        )
    }
    
    fun ItemData.mapToUi(): ItemUi {
        return ItemUi(
            name = this.name,
            price = this.price,
            amount = this.amount,
            isComplete = this.isComplete,
            creationDate = this.creationDate,
            listId = this.listId,
            id = this.id,
            currency = this.currency
        )
    }
    
    fun ItemUi.mapToData(): ItemData {
        return ItemData(
            name = this.name,
            price = this.price,
            amount = this.amount,
            isComplete = this.isComplete,
            creationDate = this.creationDate,
            listId = this.listId,
            id = this.id,
            currency = this.currency
        )
    }
}
