package nikmax.shopinglist.core.ui

import nikmax.shopinglist.core.Currency

data class ItemUi(
    val name: String,
    val price: Float,
    val amount: Int,
    val isComplete: Boolean,
    val creationDate: Long,
    val listId: Int,
    val id: Int,
    val currency: Currency
)
