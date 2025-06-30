package nikmax.shopinglist.list

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class ListRenamingDialogKtTest {
    @get:Rule
    val rule = createComposeRule()
    
    private val context = InstrumentationRegistry.getInstrumentation().context
    private val nameLabelStr = context.getString(R.string.name)
    private val nameErrorStr = context.getString(R.string.invalid_name)
    private val confirmStr = context.getString(R.string.rename)
    private val dismissStr = context.getString(R.string.cancel)
    
    @Test
    fun name_isEqualsTo_initialName_when_launched() {
        val initialName = "initial name"
        rule.setContent {
            ListRenamingDialog(
                initialName = initialName,
                onConfirm = {},
                onDismiss = {}
            )
        }
        rule.onNodeWithText(initialName).assertExists(initialName)
    }
    
    @Test
    fun name_errorIsDisplayed_when_nameIsBlank() {
        rule.setContent {
            ListRenamingDialog(
                initialName = "",
                onConfirm = {},
                onDismiss = {}
            )
        }
        rule.onNodeWithText(nameErrorStr).assertExists()
    }
    
    @Test
    fun confirmationButton_isLocked_when_nameIsInvalid() {
        rule.setContent {
            ListRenamingDialog(
                initialName = "",
                onConfirm = {},
                onDismiss = {}
            )
        }
        rule.onNodeWithText(confirmStr).assertIsNotEnabled()
    }
    
    @Test
    fun dismissCallback_isCalled_after_dismissButtonClicked() {
        var isCalled = false
        rule.setContent {
            ListRenamingDialog(
                initialName = "",
                onConfirm = {},
                onDismiss = { isCalled = true }
            )
        }
        rule.onNodeWithText(dismissStr).performClick()
        assertThat(isCalled).isTrue()
    }
    
    @Test
    fun confirmationCallback_isCalled_after_confirmationButtonClicked_if_nameIsValid() {
        var isCalled = false
        rule.setContent {
            ListRenamingDialog(
                initialName = "valid name",
                onConfirm = { isCalled = true },
                onDismiss = {}
            )
        }
        rule.onNodeWithText(confirmStr).performClick()
        assertThat(isCalled).isTrue()
    }
}
