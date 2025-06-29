package nikmax.shopinglist.list

import nikmax.shopinglist.core.Error
import nikmax.shopinglist.core.Result
import nikmax.shopinglist.core.data.ItemData
import nikmax.shopinglist.core.data.ListData
import nikmax.shopinglist.core.data.ShoppingRepo

internal class ShoppingRepoFakeImpl : ShoppingRepo {
    
    var lists = listOf<ListData>()
    var items = listOf<ItemData>()
    
    override suspend fun getListWithItems(listId: Int): Pair<ListData?, List<ItemData>> {
        val list = lists.find { it.id == listId }
        val items = items.filter { it.listId == listId }
        return Pair(list, items)
    }
    
    override suspend fun deleteListWithItems(list: ListData): Result<Unit, Error> {
        lists = lists - list
        items = items - items.filter { it.listId == list.id }
        return Result.Success(Unit)
    }
    
    override suspend fun getAllLists(): List<ListData> {
        TODO("Not yet implemented")
    }
    
    override suspend fun createList(list: ListData): Result<Unit, Error> {
        lists = lists + list
        return Result.Success(Unit)
    }
    
    override suspend fun updateList(list: ListData): Result<Unit, Error> {
        lists = lists - lists.filter { it.id == list.id }
        lists = lists + list
        return Result.Success(Unit)
    }
    
    override suspend fun createItem(item: ItemData): Result<Unit, Error> {
        items = items + item
        return Result.Success(Unit)
    }
    
    override suspend fun updateItem(item: ItemData): Result<Unit, Error> {
        items = items - items.filter { it.id == item.id }
        items = items + item
        return Result.Success(Unit)
    }
    
    override suspend fun deleteItem(item: ItemData): Result<Unit, Error> {
        items = items - item
        return Result.Success(Unit)
    }
}
