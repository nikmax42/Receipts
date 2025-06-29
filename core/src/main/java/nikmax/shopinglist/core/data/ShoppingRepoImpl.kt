package nikmax.shopinglist.core.data

import nikmax.shopinglist.core.Error
import nikmax.shopinglist.core.ItemMappers.mapToData
import nikmax.shopinglist.core.ItemMappers.mapToEntity
import nikmax.shopinglist.core.ListsMappers.mapToData
import nikmax.shopinglist.core.ListsMappers.mapToEntity
import nikmax.shopinglist.core.Result
import nikmax.shopinglist.core.data.room.DatabaseError
import nikmax.shopinglist.core.data.room.ItemsDao
import nikmax.shopinglist.core.data.room.ListsDao

internal class ShoppingRepoImpl(
    private val listsDao: ListsDao,
    private val itemsDao: ItemsDao
) : ShoppingRepo {
    override suspend fun getListWithItems(listId: Int): Pair<ListData?, List<ItemData>> {
        return listsDao.getAllListsWithItems().find { it.list.id == listId }
            .let { listWithItems ->
                val listData = listWithItems?.mapToData()
                val itemsData = listWithItems?.items?.map { it.mapToData() } ?: emptyList()
                Pair(listData, itemsData)
            }
    }
    
    override suspend fun deleteListWithItems(list: ListData): Result<Unit, Error> {
        return try {
            Result.Success(listsDao.delete(list.mapToEntity()))
        }
        catch (e: Exception) {
            Result.Error(DatabaseError.Unknown(e))
        }
    }
    
    override suspend fun getAllLists(): List<ListData> {
        return listsDao.getAllListsWithItems().map {
            it.mapToData()
        }
    }
    
    override suspend fun createList(list: ListData): Result<Unit, Error> {
        return try {
            Result.Success(listsDao.insert(list.mapToEntity()))
        }
        catch (e: Exception) {
            Result.Error(DatabaseError.Unknown(e))
        }
    }
    
    override suspend fun updateList(list: ListData): Result<Unit, Error> {
        return try {
            Result.Success(listsDao.update(list.mapToEntity()))
        }
        catch (e: Exception) {
            Result.Error(DatabaseError.Unknown(e))
        }
    }
    
    override suspend fun createItem(item: ItemData): Result<Unit, Error> {
        return try {
            Result.Success(itemsDao.insert(item.mapToEntity()))
        }
        catch (e: Exception) {
            Result.Error(DatabaseError.Unknown(e))
        }
    }
    
    override suspend fun updateItem(item: ItemData): Result<Unit, Error> {
        return try {
            Result.Success(itemsDao.update(item.mapToEntity()))
        }
        catch (e: Exception) {
            Result.Error(DatabaseError.Unknown(e))
        }
    }
    
    override suspend fun deleteItem(item: ItemData): Result<Unit, Error> {
        return try {
            Result.Success(itemsDao.delete(item.mapToEntity()))
        }
        catch (e: Exception) {
            Result.Error(DatabaseError.Unknown(e))
        }
    }
}
