package nikmax.shopinglist.core.data.room

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import nikmax.shopinglist.core.Currency
import org.junit.Before
import org.junit.Test

class ShoppingDbTest {
    private lateinit var db: ShoppingDb
    private lateinit var context: Context
    
    private val testList = ListEntity(
        name = "list name",
        creationDate = 0,
        id = 1,
        currency = Currency.USD
    )
    private val testItem = ItemEntity(
        name = "name",
        price = 42f,
        amount = 13,
        isComplete = false,
        creationDate = 0,
        listId = testList.id,
        id = 1,
        currency = Currency.USD
    )
    private val testListWithItem = ListWithItems(
        list = testList,
        items = listOf(testItem)
    )
    
    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder<ShoppingDb>(context).build()
    }
    
    @Test
    fun item_insert_adsCorrectItemToDb() = runTest {
        db.listsDao().insert(testList)
        db.itemsDao().insert(testItem)
        db.itemsDao().getAll().let { result ->
            Truth.assertThat(result).contains(testItem)
        }
    }
    
    @Test
    fun item_delete_deletesCorrectItemFromDb() = runTest {
        db.listsDao().insert(testList)
        db.itemsDao().insert(testItem)
        db.itemsDao().delete(testItem)
        db.itemsDao().getAll().let { result ->
            Truth.assertThat(result).doesNotContain(testItem)
        }
    }
    
    @Test
    fun item_update_updatesCorrectItemInDb() = runTest {
        db.listsDao().insert(testList)
        db.itemsDao().insert(testItem)
        val updatedItem = testItem.copy(isComplete = true)
        db.itemsDao().update(updatedItem)
        db.itemsDao().getAll().let { result ->
            Truth.assertThat(result).contains(updatedItem)
            Truth.assertThat(result).doesNotContain(testItem)
        }
    }
    
    @Test
    fun item_getAll_returnsAllItemsFromDb() = runTest {
        db.listsDao().insert(testList)
        db.itemsDao().insert(testItem)
        db.itemsDao().getAll().let { result ->
            Truth.assertThat(result).containsExactly(testItem)
        }
    }
    
    @Test
    fun list_insert_adsCorrectListToDb() = runTest {
        db.listsDao().insert(testList)
        db.listsDao().getAllLists().let { result ->
            Truth.assertThat(result).contains(testList)
        }
    }
    
    @Test
    fun list_delete_deletesCorrectListFromDb() = runTest {
        db.listsDao().insert(testList)
        db.listsDao().delete(testList)
        db.listsDao().getAllLists().let { result ->
            Truth.assertThat(result).doesNotContain(testList)
        }
    }
    
    @Test
    fun list_update_updatesCorrectListInDb() = runTest {
        db.listsDao().insert(testList)
        val updatedList = testList.copy(name = "new name")
        db.listsDao().update(updatedList)
        db.listsDao().getAllLists().let { result ->
            Truth.assertThat(result).doesNotContain(testList)
            Truth.assertThat(result).contains(updatedList)
        }
    }
    
    @Test
    fun list_getAll_returnsAllLists() = runTest {
        db.listsDao().insert(testList)
        db.listsDao().getAllLists().let { result ->
            Truth.assertThat(result).containsExactly(testList)
        }
    }
    
    @Test
    fun list_getAllWithItems_returnsAllListsWithTheirsItems() = runTest {
        db.listsDao().insert(testList)
        db.itemsDao().insert(testItem)
        db.listsDao().getAllListsWithItems().let { result ->
            Truth.assertThat(result).containsExactly(testListWithItem)
        }
    }
    
    @Test
    fun list_deleteWithItems_deletesListWithAllItsItems() = runTest {
        db.listsDao().insert(testList)
        db.itemsDao().insert(testItem)
        db.listsDao().delete(testList)
        db.listsDao().getAllLists().let { result ->
            Truth.assertThat(result).doesNotContain(testList)
        }
        db.itemsDao().getAll().let { result ->
            Truth.assertThat(result).doesNotContain(testItem)
        }
    }
}
