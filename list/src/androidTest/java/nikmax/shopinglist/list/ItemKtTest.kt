package nikmax.shopinglist.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import nikmax.shopinglist.core.Currency
import nikmax.shopinglist.core.ui.ItemUi
import org.junit.Rule
import org.junit.Test

class ItemKtTest {
    @get:Rule
    val rule = createComposeRule()
    
    private val testItem = ItemUi(
        name = "name",
        price = 12f,
        amount = 1,
        isComplete = false,
        creationDate = 0,
        listId = 0,
        id = 0,
        currency = Currency.USD
    )
    
    private val context = InstrumentationRegistry.getInstrumentation().context
    private val completedStr = context.getString(R.string.is_completed)
    private val notCompletedStr = context.getString(R.string.is_not_completed)
    private val amountErrorStr = context.getString(R.string.positive_number_required)
    private val priceErrorStr = context.getString(R.string.number_required)
    private val deleteStr = context.getString(R.string.delete_item)
    
    @Test
    fun name_isEqualsToItemName_when_launched() {
        rule.setContent {
            Item(
                item = testItem,
                onUpdate = {},
                onDelete = {}
            )
        }
        rule.onNodeWithText(testItem.name).assertExists()
    }
    
    @Test
    fun amount_isEqualsToItemName_when_launched() {
        rule.setContent {
            Item(
                item = testItem,
                onUpdate = {},
                onDelete = {}
            )
        }
        rule.onNodeWithText(testItem.amount.toString()).assertExists()
    }
    
    @Test
    fun price_isEqualsToItemName_when_launched() {
        rule.setContent {
            Item(
                item = testItem,
                onUpdate = {},
                onDelete = {}
            )
        }
        rule.onNodeWithText(testItem.price.toString()).assertExists()
    }
    
    @Test
    fun checkboxValue_isEqualsTo_itemCompletion_when_launched() {
        rule.setContent {
            Item(
                item = testItem,
                onUpdate = {},
                onDelete = {}
            )
        }
        when (testItem.isComplete) {
            true -> rule.onNodeWithContentDescription(completedStr).assertExists()
            false -> rule.onNodeWithContentDescription(notCompletedStr).assertExists()
        }
    }
    
    @Test
    fun amount_errorIsDisplayed_when_amountIsInvalid() {
        rule.setContent {
            Item(
                item = testItem.copy(
                    amount = -42
                ),
                onUpdate = {},
                onDelete = {}
            )
        }
        rule.onNodeWithText(amountErrorStr).assertExists()
    }
    
    @Test
    fun price_errorIsDisplayed_when_priceIsInvalid() {
        rule.setContent {
            Item(
                item = testItem.copy(
                    price = -42f
                ),
                onUpdate = {},
                onDelete = {}
            )
        }
        rule.onNodeWithText(priceErrorStr).assertExists()
    }
    
    @Test
    fun update_isPerformed_when_checkboxClicked() {
        var isCalled = false
        rule.setContent {
            var item by remember { mutableStateOf(testItem) }
            Item(
                item = item,
                onUpdate = {
                    item = it
                    isCalled = true
                },
                onDelete = {}
            )
        }
        rule.onNodeWithContentDescription(notCompletedStr).performClick()
        assertThat(isCalled).isTrue()
        rule.onNodeWithContentDescription(completedStr).assertExists()
    }
    
    @Test
    fun update_isPerformed_when_correctAmountEntered() {
        var isCalled = false
        rule.setContent {
            var item by remember { mutableStateOf(testItem) }
            Item(
                item = item,
                onUpdate = {
                    item = it
                    isCalled = true
                },
                onDelete = {}
            )
        }
        rule.onNodeWithText(testItem.amount.toString()).performTextInput("10")
        assertThat(isCalled).isTrue()
        rule.onNodeWithText("10" + testItem.amount.toString()).assertExists()
    }
    
    @Test
    fun update_isPerformed_when_correctPriceEntered() {
        var isCalled = false
        rule.setContent {
            var item by remember { mutableStateOf(testItem) }
            Item(
                item = item,
                onUpdate = {
                    item = it
                    isCalled = true
                },
                onDelete = {}
            )
        }
        rule.onNodeWithText(testItem.price.toString()).performTextInput("10")
        assertThat(isCalled).isTrue()
        rule.onNodeWithText("10" + testItem.price.toString()).assertExists()
    }
    
    @Test
    fun delete_isPerformed_when_swiped_and_deleteButtonClicked() {
        var isCalled = false
        rule.setContent {
            Item(
                item = testItem,
                onUpdate = {},
                onDelete = { isCalled = true }
            )
        }
        rule.onNodeWithText(testItem.name).performTouchInput {
            swipeLeft()
        }
        rule.onNodeWithContentDescription(deleteStr).performClick()
        assertThat(isCalled).isTrue()
    }
}
