package nikmax.shoppinglist

import kotlinx.serialization.Serializable

internal object Routes {
    @Serializable
    data object Home
    @Serializable
    data class List(val listId: Int)
}
