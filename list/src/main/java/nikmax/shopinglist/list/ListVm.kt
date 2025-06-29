package nikmax.shopinglist.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nikmax.shopinglist.core.ItemMappers.mapToData
import nikmax.shopinglist.core.ItemMappers.mapToUi
import nikmax.shopinglist.core.ListsMappers.mapToData
import nikmax.shopinglist.core.ListsMappers.mapToUi
import nikmax.shopinglist.core.Result
import nikmax.shopinglist.core.data.ShoppingRepo
import nikmax.shopinglist.core.ui.ItemUi
import nikmax.shopinglist.core.ui.ListUi
import javax.inject.Inject

@HiltViewModel
class ListVm
@Inject
constructor(private val repo: ShoppingRepo) : ViewModel() {
    
    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()
    
    fun onAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                is Action.LoadList -> onLoadList(action.listId)
                is Action.RenameList -> onListRename(action.list, action.newName)
                is Action.DeleteList -> onListDelete(action.list)
                is Action.AddItem -> onItemAdd(action.item)
                is Action.UpdateItem -> onItemUpdate(action.newItem)
                is Action.DeleteItem -> onItemDelete(action.item)
            }
        }
    }
    
    private suspend fun onLoadList(listId: Int) {
        repo
            .getListWithItems(listId)
            .let { (list, items) ->
                when (list == null) {
                    true -> _state.update {
                        it.copy(isDeleted = true)
                    }
                    false -> _state.update {
                        it.copy(
                            list = list.mapToUi(),
                            items = items.map { it.mapToUi() }
                        )
                    }
                }
            }
    }
    
    private suspend fun onListRename(list: ListUi, newName: String) {
        val renamedList = list.copy(name = newName)
        repo.updateList(
            renamedList.mapToData()
        ).let { result ->
            when (result) {
                is Result.Error -> {}
                is Result.Success -> onLoadList(list.id)
            }
        }
    }
    
    private suspend fun onListDelete(list: ListUi) {
        repo
            .deleteListWithItems(list.mapToData())
            .let { result ->
                when (result) {
                    is Result.Error -> {}
                    is Result.Success -> _state.update {
                        it.copy(
                            isDeleted = true,
                            list = null,
                            items = emptyList()
                        )
                    }
                }
            }
    }
    
    private suspend fun onItemAdd(item: ItemUi) {
        repo
            .createItem(item.mapToData())
            .let { result ->
                when (result) {
                    is Result.Error -> {}
                    is Result.Success -> onLoadList(item.listId)
                }
            }
    }
    
    private suspend fun onItemUpdate(newItem: ItemUi) {
        repo
            .updateItem(newItem.mapToData())
            .let { result ->
                when (result) {
                    is Result.Error -> {}
                    is Result.Success -> onLoadList(newItem.listId)
                }
            }
    }
    
    private suspend fun onItemDelete(item: ItemUi) {
        repo
            .deleteItem(item.mapToData())
            .let { result ->
                when (result) {
                    is Result.Error -> {}
                    is Result.Success -> onLoadList(item.listId)
                }
            }
    }
}
