package nikmax.shopinglist.home

import nikmax.shopinglist.core.ui.ListUi

data class UiState(
    val lists: List<ListUi> = emptyList(),
    val searchResults: List<ListUi> = emptyList(),
    val isLaunching: Boolean = true
)
