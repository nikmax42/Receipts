package nikmax.shopinglist.core.data.room

import nikmax.shopinglist.core.Error

sealed class DatabaseError(override val e: Exception) : Error() {
    data class Unknown(override val e: Exception) : Error()
}
