package nikmax.shopinglist.core

import com.google.common.truth.Truth.assertThat
import nikmax.shopinglist.core.ItemMappers.mapToData
import nikmax.shopinglist.core.ItemMappers.mapToEntity
import nikmax.shopinglist.core.ItemMappers.mapToUi
import nikmax.shopinglist.core.data.ItemData
import nikmax.shopinglist.core.data.room.ItemEntity
import nikmax.shopinglist.core.ui.ItemUi
import org.junit.Test

class ItemMappersTest {
    private val entity = ItemEntity(
        name = "name entity",
        price = 1f,
        amount = 1,
        isComplete = true,
        creationDate = 1,
        listId = 1,
        id = 1,
        currency = Currency.USD
    )
    
    private val data = ItemData(
        name = "name data",
        price = 2f,
        amount = 2,
        isComplete = false,
        creationDate = 2,
        listId = 2,
        id = 2,
        currency = Currency.USD
    )
    
    private val ui = ItemUi(
        name = "name ui",
        price = 3f,
        amount = 3,
        isComplete = false,
        creationDate = 3,
        listId = 3,
        id = 3,
        currency = Currency.USD
    )
    
    @Test
    fun entityToDataMapping_allFieldsAreEqual() {
        entity.mapToData().let { data ->
            assertThat(data.name).isEqualTo(entity.name)
            assertThat(data.price).isEqualTo(entity.price)
            assertThat(data.amount).isEqualTo(entity.amount)
            assertThat(data.isComplete).isEqualTo(entity.isComplete)
            assertThat(data.creationDate).isEqualTo(entity.creationDate)
            assertThat(data.listId).isEqualTo(entity.listId)
            assertThat(data.id).isEqualTo(entity.id)
            assertThat(data.currency).isEqualTo(entity.currency)
        }
    }
    
    @Test
    fun dataToEntityMapping_allFieldsAreEqual() {
        data.mapToEntity().let { entity ->
            assertThat(entity.name).isEqualTo(data.name)
            assertThat(entity.price).isEqualTo(data.price)
            assertThat(entity.amount).isEqualTo(data.amount)
            assertThat(entity.isComplete).isEqualTo(data.isComplete)
            assertThat(entity.creationDate).isEqualTo(data.creationDate)
            assertThat(entity.listId).isEqualTo(data.listId)
            assertThat(entity.id).isEqualTo(data.id)
            assertThat(entity.currency).isEqualTo(data.currency)
        }
    }
    
    @Test
    fun dataToUiMapping_allFieldsAreCorrect() {
        data.mapToUi().let { ui ->
            assertThat(ui.name).isEqualTo(data.name)
            assertThat(ui.price).isEqualTo(data.price)
            assertThat(ui.amount).isEqualTo(data.amount)
            assertThat(ui.isComplete).isEqualTo(data.isComplete)
            assertThat(ui.creationDate).isEqualTo(data.creationDate)
            assertThat(ui.listId).isEqualTo(data.listId)
            assertThat(ui.id).isEqualTo(data.id)
            assertThat(ui.currency).isEqualTo(data.currency)
        }
    }
    
    @Test
    fun uiToDataMapping_allFieldsAreCorrect() {
        data.mapToUi().let { data ->
            assertThat(data.name).isEqualTo(data.name)
            assertThat(data.price).isEqualTo(data.price)
            assertThat(data.amount).isEqualTo(data.amount)
            assertThat(data.isComplete).isEqualTo(data.isComplete)
            assertThat(data.creationDate).isEqualTo(data.creationDate)
            assertThat(data.listId).isEqualTo(data.listId)
            assertThat(data.id).isEqualTo(data.id)
            assertThat(data.currency).isEqualTo(data.currency)
        }
    }
}
