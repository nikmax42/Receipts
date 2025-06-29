package nikmax.shopinglist.home

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import nikmax.shopinglist.core.Currency
import nikmax.shopinglist.core.ListsMappers.mapToData
import nikmax.shopinglist.core.ListsMappers.mapToUi
import nikmax.shopinglist.core.ui.ListUi
import org.junit.Before
import org.junit.Test

class HomeVmTest {
    private lateinit var vm: HomeVm
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
    
    @Before
    fun setup() {
        repo = ShoppingRepoFakeImpl()
        vm = HomeVm(repo)
    }
    
    @Test
    fun onShowAllLists_setsAllListsFromRepo() = runTest {
        repo.lists = listOf(testList.mapToData())
        vm.state.test {
            vm.onAction(Action.ShowAllLists)
            skipItems(1)
            awaitItem().let {
                assertThat(it.lists).isEqualTo(repo.lists.map { it.mapToUi() })
            }
        }
    }
    
    @Test
    fun onSelectionChange_selectItem_WHEN_itemNotSelected() = runTest {
        repo.lists = listOf(testList.mapToData())
        vm.state.test {
            vm.onAction(Action.ShowAllLists)
            vm.onAction(Action.ChangeListSelection(testList))
            skipItems(2)
            awaitItem().let {
                assertThat(it.lists.first().isSelected).isTrue()
            }
        }
    }
    
    @Test
    fun onSelectionChange_unselectItem_WHEN_itemSelected() = runTest {
        val selectedList = testList.copy(
            isSelected = true
        )
        repo.lists = listOf(selectedList.mapToData())
        vm.state.test {
            skipItems(1)
            vm.onAction(Action.ShowAllLists)
            vm.onAction(Action.ChangeListSelection(selectedList))
            awaitItem().let {
                assertThat(it.lists.first().isSelected).isFalse()
            }
        }
    }
    
    @Test
    fun onClearSelection_unselectAllItems() = runTest {
        val selectedList = testList.copy(
            isSelected = true
        )
        repo.lists = listOf(selectedList.mapToData())
        vm.state.test {
            skipItems(1)
            vm.onAction(Action.ShowAllLists)
            vm.onAction(Action.ClearListsSelection)
            awaitItem().let {
                assertThat(it.lists.first().isSelected).isFalse()
            }
        }
    }
    
    @Test
    fun onCreateList_addsProvidedList() = runTest {
        vm.state.test {
            skipItems(1)
            vm.onAction(Action.CreateList(testList))
            vm.onAction(Action.ShowAllLists)
            skipItems(1)
            awaitItem().let { state ->
                assertThat(state.lists.first()).isEqualTo(testList)
            }
        }
    }
    
    @Test
    fun onDeleteLists_removesProvidedLists() = runTest {
        repo.lists = listOf(testList.mapToData())
        vm.state.test {
            skipItems(1)
            vm.onAction(Action.ShowAllLists)
            assertThat(awaitItem().lists).contains(testList)
            vm.onAction(Action.DeleteLists(listOf(testList)))
            awaitItem().let { state ->
                assertThat(repo.lists).doesNotContain(testList.mapToData())
                assertThat(state.lists).doesNotContain(testList)
            }
        }
    }
    
    @Test
    fun onSearchLists_setsResults() = runTest {
        repo.lists = listOf(testList.mapToData())
        vm.state.test {
            skipItems(1)
            vm.onAction(Action.SearchLists(testList.name))
            awaitItem().let { state ->
                assertThat(state.searchResults).contains(testList)
            }
        }
    }
}
