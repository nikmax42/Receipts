package nikmax.shopinglist.core.ui

import nikmax.shopinglist.core.Currency

data class ListUi(
    val name: String,
    val creationDate: Long,
    val id: Int,
    val items: Int,
    val completedItems: Int,
    val totalPrice: Float,
    val completedPrice: Float,
    val currency: Currency,
    val isSelected: Boolean = false
)
