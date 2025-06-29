package nikmax.shopinglist.list

import nikmax.shopinglist.core.ui.ItemUi
import nikmax.shopinglist.core.ui.ListUi

sealed interface Action {
    data class LoadList(val listId: Int) : Action
    data class DeleteList(val list: ListUi) : Action
    data class RenameList(val list: ListUi, val newName: String) : Action
    
    data class AddItem(val item: ItemUi) : Action
    data class UpdateItem(val oldItem: ItemUi, val newItem: ItemUi) : Action
    data class DeleteItem(val item: ItemUi) : Action
}
