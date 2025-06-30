package nikmax.shopinglist.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nikmax.shopinglist.core.ListsMappers.mapToData
import nikmax.shopinglist.core.ListsMappers.mapToUi
import nikmax.shopinglist.core.Result
import nikmax.shopinglist.core.data.ShoppingRepo
import nikmax.shopinglist.core.replace
import nikmax.shopinglist.core.ui.ListUi
import javax.inject.Inject

@HiltViewModel
class HomeVm
@Inject
constructor(private val repo: ShoppingRepo) : ViewModel() {
    
    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()
    
    fun onAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                Action.ShowAllLists -> showAllLists()
                is Action.ChangeListSelection -> onListSelectionChange(action.list)
                Action.ClearListsSelection -> onClearSelection()
                is Action.CreateList -> onCreateList(action.list)
                is Action.DeleteLists -> onDeleteLists(action.lists)
                is Action.SearchLists -> onSearchLists(action.query)
            }
        }
    }
    
    private suspend fun showAllLists() {
        val lists = repo
            .getAllLists()
            .map { it.mapToUi() }
        _state.update {
            it.copy(
                lists = lists,
                isLaunching = false
            )
        }
    }
    
    private fun onListSelectionChange(list: ListUi) {
        _state.update {
            it.copy(
                lists = it.lists.replace(
                    list,
                    list.copy(isSelected = !list.isSelected)
                )
            )
        }
    }
    
    private fun onClearSelection() {
        _state.update {
            it.copy(
                lists = it.lists.map {
                    if (it.isSelected) it.copy(isSelected = false)
                    else it
                }
            )
        }
    }
    
    private suspend fun onCreateList(list: ListUi) {
        repo
            .createList(list.mapToData())
            .let { result ->
                when (result) {
                    is Result.Error -> {}
                    is Result.Success -> showAllLists()
                }
            }
    }
    
    private suspend fun onDeleteLists(lists: List<ListUi>) {
        lists.map { listToDelete ->
            repo
                .deleteListWithItems(listToDelete.mapToData())
                .also { result ->
                    when (result) {
                        is Result.Error -> {}
                        is Result.Success -> showAllLists()
                    }
                }
        }.let { results ->
            when (results.all { it is Result.Success }) {
                true -> {} // TODO("show snack")
                false -> {} // TODO("show snack")
            }
        }
    }
    
    private suspend fun onSearchLists(query: String) {
        val results = repo
            .getAllLists()
            .filter { it.name.contains(query, ignoreCase = true) }
            .map { it.mapToUi() }
        _state.update {
            it.copy(
                searchResults = results
            )
        }
    }
}
