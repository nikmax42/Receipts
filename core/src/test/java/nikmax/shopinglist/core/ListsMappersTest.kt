package nikmax.shopinglist.core

import com.google.common.truth.Truth.assertThat
import nikmax.shopinglist.core.ListsMappers.mapToData
import nikmax.shopinglist.core.ListsMappers.mapToEntity
import nikmax.shopinglist.core.ListsMappers.mapToUi
import nikmax.shopinglist.core.data.ListData
import nikmax.shopinglist.core.data.room.ItemEntity
import nikmax.shopinglist.core.data.room.ListEntity
import nikmax.shopinglist.core.data.room.ListWithItems
import nikmax.shopinglist.core.ui.ListUi
import org.junit.Test

class ListsMappersTest {
    private val listWithItems = ListWithItems(
        list = ListEntity(
            name = "name entity",
            creationDate = 0,
            id = 0,
            currency = Currency.USD
        ),
        items = listOf(
            ItemEntity(
                name = "name entity",
                price = 0f,
                amount = 0,
                isComplete = false,
                creationDate = 0,
                listId = 0,
                id = 0,
                currency = Currency.USD
            )
        )
    )
    
    private val data = ListData(
        name = "name data",
        creationDate = 1,
        id = 1,
        items = 1,
        completedItems = 1,
        totalPrice = 1f,
        completedPrice = 1f,
        currency = Currency.USD
    )
    
    private val ui = ListUi(
        name = "name ui",
        creationDate = 2,
        id = 2,
        items = 2,
        completedItems = 2,
        totalPrice = 2f,
        completedPrice = 2f,
        currency = Currency.USD
    )
    
    @Test
    fun listWithItemsToDataMapping_allFieldsAreCorrect() {
        listWithItems.mapToData().let { data ->
            assertThat(data.name).isEqualTo(listWithItems.list.name)
            assertThat(data.creationDate).isEqualTo(listWithItems.list.creationDate)
            assertThat(data.id).isEqualTo(listWithItems.list.id)
            assertThat(data.items).isEqualTo(listWithItems.items.size)
            assertThat(data.completedItems).isEqualTo(listWithItems.items.count { it.isComplete })
            assertThat(data.totalPrice).isEqualTo(listWithItems.items.sumOf { it.price })
            assertThat(data.completedPrice).isEqualTo(listWithItems.items.sumOf { if (it.isComplete) it.price else 0f })
            assertThat(data.currency).isEqualTo(listWithItems.list.currency)
        }
    }
    
    @Test
    fun dataToEntityMapping_allFieldsAreCorrect() {
        data.mapToEntity().let { entity ->
            assertThat(entity.name).isEqualTo(data.name)
            assertThat(entity.creationDate).isEqualTo(data.creationDate)
            assertThat(entity.id).isEqualTo(data.id)
            assertThat(entity.currency).isEqualTo(data.currency)
        }
    }
    
    @Test
    fun dataToUiMapping_allFieldsAreCorrect() {
        data.mapToUi().let { ui ->
            assertThat(ui.name).isEqualTo(data.name)
            assertThat(ui.creationDate).isEqualTo(data.creationDate)
            assertThat(ui.id).isEqualTo(data.id)
            assertThat(ui.items).isEqualTo(data.items)
            assertThat(ui.completedItems).isEqualTo(data.completedItems)
            assertThat(ui.totalPrice).isEqualTo(data.totalPrice)
            assertThat(ui.completedPrice).isEqualTo(data.completedPrice)
            assertThat(ui.currency).isEqualTo(data.currency)
        }
    }
    
    @Test
    fun uiToDataMapping_allFieldsAreCorrect() {
        ui.mapToData().let { data ->
            assertThat(data.name).isEqualTo(ui.name)
            assertThat(data.creationDate).isEqualTo(ui.creationDate)
            assertThat(data.id).isEqualTo(ui.id)
            assertThat(data.items).isEqualTo(ui.items)
            assertThat(data.completedItems).isEqualTo(ui.completedItems)
            assertThat(data.totalPrice).isEqualTo(ui.totalPrice)
            assertThat(data.completedPrice).isEqualTo(ui.completedPrice)
            assertThat(data.currency).isEqualTo(ui.currency)
        }
    }
}
