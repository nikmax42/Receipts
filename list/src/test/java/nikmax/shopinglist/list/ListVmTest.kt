package nikmax.shopinglist.list

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import nikmax.shopinglist.core.Currency
import nikmax.shopinglist.core.ItemMappers.mapToData
import nikmax.shopinglist.core.ListsMappers.mapToData
import nikmax.shopinglist.core.ui.ItemUi
import nikmax.shopinglist.core.ui.ListUi
import org.junit.Before
import org.junit.Test

class ListVmTest {
    
    private lateinit var vm: ListVm
    private lateinit var repo: ShoppingRepoFakeImpl
    
    private val testList = ListUi(
        name = "list",
        creationDate = 0,
        id = 0,
        items = 0,
        completedItems = 0,
        totalPrice = 0f,
        completedPrice = 0f,
        currency = Currency.USD,
        isSelected = false
    )
    private val testItem = ItemUi(
        name = "item",
        price = 0f,
        amount = 0,
        isComplete = false,
        creationDate = 0,
        listId = 0,
        id = 0,
        currency = Currency.USD
    )
    
    @Before
    fun setup() {
        repo = ShoppingRepoFakeImpl()
        vm = ListVm(repo)
    }
    
    @Test
    fun onListLoad_setIsDeleteToTrue_ifListNotFound() = runTest {
        vm.state.test {
            vm.onAction(Action.LoadList(testList.id))
            skipItems(1)
            awaitItem().let {
                assertThat(it.isDeleted).isTrue()
            }
        }
    }
    
    @Test
    fun onListLoad_setListAndItems_ifListFound() = runTest {
        repo.lists = listOf(testList.mapToData())
        repo.items = listOf(testItem.mapToData())
        vm.state.test {
            vm.onAction(Action.LoadList(testList.id))
            skipItems(1)
            awaitItem().let {
                assertThat(it.list).isNotNull()
                assertThat(it.items).isNotEmpty()
            }
        }
    }
    
    @Test
    fun onListRename_updatesList_ifSuccess() = runTest {
        repo.lists = listOf(testList.mapToData())
        vm.state.test {
            vm.onAction(Action.RenameList(testList, "new name"))
            skipItems(1)
            awaitItem().let {
                assertThat(it.list!!.name).isEqualTo("new name")
            }
        }
    }
    
    @Test
    fun onListDelete_setIsDeleteToTrue_ifSuccess() = runTest {
        repo.lists = listOf(testList.mapToData())
        vm.state.test {
            vm.onAction(Action.DeleteList(testList))
            skipItems(1)
            awaitItem().let {
                assertThat(it.isDeleted).isTrue()
            }
        }
    }
    
    @Test
    fun onItemAdd_addsNewItem_ifSuccess() = runTest {
        vm.state.test {
            vm.onAction(Action.AddItem(testItem))
            skipItems(1)
            awaitItem().let { state ->
                assertThat(state.items).contains(testItem)
                assertThat(repo.items).contains(testItem.mapToData())
            }
        }
    }
    
    @Test
    fun onItemUpdate_replaceItemWithUpdated_ifSuccess() = runTest {
        val updatedItem = testItem.copy(
            isComplete = true
        )
        vm.state.test {
            vm.onAction(Action.UpdateItem(testItem, updatedItem))
            skipItems(1)
            awaitItem().let { state ->
                assertThat(state.items).contains(updatedItem)
                assertThat(state.items).doesNotContain(testItem)
                assertThat(repo.items).contains(updatedItem.mapToData())
                assertThat(repo.items).doesNotContain(testItem.mapToData())
            }
        }
    }
    
    @Test
    fun onItemDelete_removesItem_ifSuccess() = runTest {
        repo.items = listOf(testItem.mapToData())
        vm.state.test {
            skipItems(1)
            vm.onAction(Action.LoadList(testList.id))
            vm.onAction(Action.DeleteItem(testItem))
            awaitItem().let { state ->
                assertThat(state.items).doesNotContain(testItem)
                assertThat(repo.items).doesNotContain(testItem.mapToData())
            }
        }
    }
}
