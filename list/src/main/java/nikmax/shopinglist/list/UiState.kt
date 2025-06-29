package nikmax.shopinglist.list

import nikmax.shopinglist.core.ui.ItemUi
import nikmax.shopinglist.core.ui.ListUi

data class UiState(
    val list: ListUi? = null,
    val items: List<ItemUi> = emptyList(),
    val isDeleted: Boolean = false
)
