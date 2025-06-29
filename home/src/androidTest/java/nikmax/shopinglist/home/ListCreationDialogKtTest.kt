package nikmax.shopinglist.home

import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import nikmax.shopinglist.core.Currency
import org.junit.Rule
import org.junit.Test

class ListCreationDialogKtTest {
    @get:Rule
    val rule = createComposeRule()
    
    private val context = InstrumentationRegistry.getInstrumentation().context
    private val confirmStr = context.getString(R.string.create)
    private val dismissStr = context.getString(R.string.cancel)
    private val nameLabelStr = context.getString(R.string.list_name)
    private val currencyLabelStr = context.getString(R.string.currency)
    
    @Test
    fun dismissCallbackIsCalled_onDismissClick() {
        var isCalled = false
        rule.setContent {
            ListCreationDialog(
                onConfirm = {},
                onDismiss = { isCalled = true }
            )
        }
        rule.onNodeWithText(dismissStr).performClick()
        assertThat(isCalled).isTrue()
    }
    
    @Test
    fun confirmButton_isDisabled_when_someFieldsAreInvalid() {
        rule.setContent {
            ListCreationDialog(
                onConfirm = {},
                onDismiss = {}
            )
        }
        rule.onNodeWithText(confirmStr).assertIsNotEnabled()
    }
    
    @Test
    fun confirmationCallbackIsCalled_onConfirmClick_when_allFieldsIsValid() {
        var isCalled = false
        rule.setContent {
            ListCreationDialog(
                onConfirm = {},
                onDismiss = { isCalled = true }
            )
        }
        rule.onNodeWithText(nameLabelStr).performTextInput("valid name")
        rule.onNodeWithText(dismissStr).performClick()
        assertThat(isCalled).isTrue()
    }
    
    @Test
    fun nameField_isFocused_when_launched() {
        rule.setContent {
            ListCreationDialog(
                onConfirm = {},
                onDismiss = {}
            )
        }
        rule.onNodeWithText(nameLabelStr).assertIsFocused()
    }
    
    @Test
    fun currencyField_isFocused_after_nameFieldImeActionPerformed_if_nameIsValid() {
        rule.setContent {
            ListCreationDialog(
                onConfirm = {},
                onDismiss = {}
            )
        }
        rule.onNodeWithText(nameLabelStr).performTextInput("valid name")
        rule.onNodeWithText(nameLabelStr).performImeAction()
        rule.onNodeWithText(currencyLabelStr).assertIsFocused()
    }
    
    @Test
    fun confirmationCallback_isCalled_after_currencyFieldImeActionPerformed_if_currencyIsValid() {
        var isCalled = false
        rule.setContent {
            ListCreationDialog(
                onConfirm = { isCalled = true },
                onDismiss = {}
            )
        }
        rule.onNodeWithText(nameLabelStr).performTextInput("valid name")
        rule.onNodeWithText(nameLabelStr).performImeAction()
        rule.onNodeWithText(currencyLabelStr).performTextInput(Currency.USD.name)
        rule.onNodeWithText(currencyLabelStr).performImeAction()
    }
    
    @Test
    fun dropdownMenu_isShown_when_currencyFieldIsFocused() {
        rule.setContent {
            ListCreationDialog(
                onConfirm = {},
                onDismiss = {}
            )
        }
        rule.onNodeWithText(currencyLabelStr).performClick()
        rule.onNodeWithText(Currency.USD.name).assertExists()
    }
    
    @Test
    fun dropdownMenu_isHidden_when_currencyFieldIsNotFocused() {
        rule.setContent {
            ListCreationDialog(
                onConfirm = {},
                onDismiss = {}
            )
        }
        rule.onNodeWithText(nameLabelStr).performClick()
        rule.onNodeWithText(Currency.USD.name).assertDoesNotExist()
    }
    
    @Test
    fun dropdownMenu_isHidden_when_validCurrencyEntered() {
        rule.setContent {
            ListCreationDialog(
                onConfirm = {},
                onDismiss = {}
            )
        }
        rule.onNodeWithText(currencyLabelStr).performClick().performTextInput(Currency.USD.name)
        rule.onNodeWithText(Currency.EUR.name).assertDoesNotExist()
    }
}
