package nikmax.shopinglist.home

import nikmax.shopinglist.core.ui.ListUi

sealed interface Action {
    data object ShowAllLists : Action
    data class CreateList(val list: ListUi) : Action
    data class DeleteLists(val lists: List<ListUi>) : Action
    data class SearchLists(val query: String) : Action
    data class ChangeListSelection(val list: ListUi) : Action
    data object ClearListsSelection : Action
}
