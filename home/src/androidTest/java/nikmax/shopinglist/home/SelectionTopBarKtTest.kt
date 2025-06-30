package nikmax.shopinglist.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import nikmax.shopinglist.core.Currency
import nikmax.shopinglist.core.ui.ListUi
import org.junit.Rule
import org.junit.Test

class SelectionTopBarKtTest {
    @get:Rule
    val rule = createComposeRule()
    
    private val list1 = ListUi(
        name = "first",
        creationDate = 0,
        id = 0,
        items = 2,
        completedItems = 1,
        totalPrice = 100f,
        completedPrice = 20f,
        currency = Currency.USD
    )
    private val list2 = ListUi(
        name = "first",
        creationDate = 0,
        id = 0,
        items = 2,
        completedItems = 1,
        totalPrice = 100f,
        completedPrice = 20f,
        currency = Currency.USD
    )
    
    private val lists = listOf(
        list1, list2
    )
    private val selectedLists = listOf(
        list1
    )
    
    private val context = InstrumentationRegistry.getInstrumentation().context
    private val titleStr = context.getString(R.string.x_from_y_lists_selected, selectedLists.size, lists.size)
    private val clearStr = context.getString(R.string.clear_selection)
    private val deleteStr = context.getString(R.string.delete_selected_lists)
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun titleText_isCorrect() {
        rule.setContent {
            SelectionTopBar(
                lists = lists,
                selectedLists = selectedLists,
                onDelete = {},
                onClear = {}
            )
        }
        rule.onNodeWithText(titleStr).assertTextEquals("${selectedLists.size}/${lists.size} lists selected")
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun callbackIsCalled_when_clearButtonClicked() {
        var isCalled = false
        rule.setContent {
            SelectionTopBar(
                lists = lists,
                selectedLists = selectedLists,
                onDelete = {},
                onClear = { isCalled = true }
            )
        }
        rule.onNodeWithContentDescription(clearStr).performClick()
        assertThat(isCalled).isTrue()
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun callbackIsCalled_when_deleteButtonClicked() {
        var isCalled = false
        rule.setContent {
            SelectionTopBar(
                lists = lists,
                selectedLists = selectedLists,
                onDelete = { isCalled = true },
                onClear = {}
            )
        }
        rule.onNodeWithContentDescription(deleteStr).performClick()
        assertThat(isCalled).isTrue()
    }
}
