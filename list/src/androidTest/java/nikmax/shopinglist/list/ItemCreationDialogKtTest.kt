package nikmax.shopinglist.list

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import nikmax.shopinglist.core.Currency
import org.junit.Rule
import org.junit.Test

class ItemCreationDialogKtTest {
    @get:Rule
    val rule = createComposeRule()
    
    private val context = InstrumentationRegistry.getInstrumentation().context
    private val confirmStr = context.getString(R.string.add)
    private val dismissStr = context.getString(R.string.cancel)
    private val nameLabelStr = context.getString(R.string.name)
    private val amountLabelStr = context.getString(R.string.amount)
    private val priceLabelStr = context.getString(R.string.price)
    private val nameErrorStr = context.getString(R.string.invalid_name)
    private val amountErrorStr = context.getString(R.string.positive_number_required)
    private val priceErrorStr = context.getString(R.string.number_required)
    
    @Test
    fun confirmationButton_isLocked_when_someFieldsAreNotValid() {
        rule.setContent {
            ItemCreationDialog(
                listId = 0,
                listCurrency = Currency.USD,
                onConfirm = {},
                onDismiss = {}
            )
        }
        rule.onNodeWithText(confirmStr).assertIsNotEnabled()
    }
    
    @Test
    fun confirmationCallback_isCalled_after_confirmationButtonClicked_if_allFieldsAreValid() {
        rule.setContent {
            ItemCreationDialog(
                listId = 0,
                listCurrency = Currency.USD,
                onConfirm = {},
                onDismiss = {}
            )
        }
        rule.onNodeWithText(nameLabelStr).performTextInput("valid name")
        rule.onNodeWithText(amountLabelStr).performTextInput("2")
        rule.onNodeWithText(priceLabelStr).performTextInput("10.0")
    }
    
    @Test
    fun dismissCallback_isCalled_after_dismissButtonClicked() {
        var isCalled = false
        rule.setContent {
            ItemCreationDialog(
                listId = 0,
                listCurrency = Currency.USD,
                onConfirm = {},
                onDismiss = { isCalled = true }
            )
        }
        rule.onNodeWithText(dismissStr).performClick()
        assertThat(isCalled).isTrue()
    }
}
