package nikmax.shopinglist.core.data

import nikmax.shopinglist.core.Currency

data class ListData(
    val name: String,
    val creationDate: Long,
    val id: Int,
    val items: Int,
    val completedItems: Int,
    val totalPrice: Float,
    val completedPrice: Float,
    val currency: Currency
)
