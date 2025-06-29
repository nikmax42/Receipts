package nikmax.shopinglist.core.data

import nikmax.shopinglist.core.Error
import nikmax.shopinglist.core.Result

interface ShoppingRepo {
    suspend fun getListWithItems(listId: Int): Pair<ListData?, List<ItemData>>
    suspend fun deleteListWithItems(list: ListData): Result<Unit, Error>
    
    suspend fun getAllLists(): List<ListData>
    suspend fun createList(list: ListData): Result<Unit, Error>
    suspend fun updateList(list: ListData): Result<Unit, Error>
    
    suspend fun createItem(item: ItemData): Result<Unit, Error>
    suspend fun updateItem(item: ItemData): Result<Unit, Error>
    suspend fun deleteItem(item: ItemData): Result<Unit, Error>
}
