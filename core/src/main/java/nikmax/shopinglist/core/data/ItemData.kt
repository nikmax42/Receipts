package nikmax.shopinglist.core.data

import nikmax.shopinglist.core.Currency

data class ItemData(
    val name: String,
    val price: Float,
    val amount: Int,
    val isComplete: Boolean,
    val creationDate: Long,
    val listId: Int,
    val id: Int,
    val currency: Currency
)
