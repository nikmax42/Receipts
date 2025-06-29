package nikmax.shopinglist.core

fun <E> List<E>.replace(oldItem: E, newItem: E): List<E> {
    val targetIndex = this.indexOf(oldItem)
    return slice(0 until targetIndex)
        .plus(newItem)
        .plus(slice(targetIndex + 1 until lastIndex))
}
