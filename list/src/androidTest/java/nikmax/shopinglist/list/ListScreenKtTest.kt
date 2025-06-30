package nikmax.shopinglist.list

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import nikmax.shopinglist.core.Currency
import nikmax.shopinglist.core.ui.ItemUi
import nikmax.shopinglist.core.ui.ListUi
import org.junit.Rule
import org.junit.Test

class ListScreenKtTest {
    @get:Rule
    val rule = createComposeRule()
    
    private val testList = ListUi(
        name = "test list",
        creationDate = 0,
        id = 0,
        items = 0,
        completedItems = 0,
        totalPrice = 0f,
        completedPrice = 0f,
        currency = Currency.USD
    )
    private val testItem = ItemUi(
        name = "test item",
        price = 0f,
        amount = 0,
        isComplete = false,
        creationDate = 0,
        listId = 0,
        id = 0,
        currency = Currency.USD
    )
    
    private val context = InstrumentationRegistry.getInstrumentation().context
    private val backButtonStr = context.getString(R.string.back)
    private val renameButtonStr = context.getString(R.string.rename_list)
    private val renameDialogTitleStr = context.getString(R.string.rename_list)
    private val renameDialogConfirmStr = context.getString(R.string.rename)
    private val renameDialogDismissStr = context.getString(R.string.cancel)
    private val deleteButtonStr = context.getString(R.string.delete_list)
    private val deletionDialogTitleStr = context.getString(R.string.deletion_dialog_title)
    private val deletionDialogConfirmStr = context.getString(R.string.delete)
    private val deletionDialogDismissStr = context.getString(R.string.cancel)
    private val fabStr = context.getString(R.string.add_item)
    private val itemCreationDialogTitleStr = context.getString(R.string.add_item)
    private val itemCreationDialogConfirmStr = context.getString(R.string.add)
    private val itemCreationDialogDismissStr = context.getString(R.string.cancel)
    private val itemCreationDialogNameLabelStr = context.getString(R.string.name)
    private val itemCreationDialogAmountLabelStr = context.getString(R.string.amount)
    private val itemCreationDialogPriceLabelStr = context.getString(R.string.price)
    private val noItemsStr = context.getString(R.string.no_items_created)
    
    
    @Test
    fun callbackIsCalled_when_backButtonClicked() {
        var isCalled = false
        rule.setContent {
            ListScreen(
                state = UiState(
                    list = testList
                ),
                onAction = {},
                onNavigateBack = { isCalled = true }
            )
        }
        rule.onNodeWithContentDescription(backButtonStr).performClick()
        assertThat(isCalled).isTrue()
    }
    
    
    @Test
    fun renamingDialog_isShown_after_renameButtonClick() {
        rule.setContent {
            ListScreen(
                state = UiState(
                    list = testList
                ),
                onAction = {},
                onNavigateBack = {}
            )
        }
        rule.onNodeWithText(renameDialogTitleStr).assertDoesNotExist()
        rule.onNodeWithContentDescription(renameButtonStr).performClick()
        rule.onNodeWithText(renameDialogTitleStr).assertExists()
    }
    
    @Test
    fun renamingDialog_isHidden_after_confirmed() {
        rule.setContent {
            ListScreen(
                state = UiState(
                    list = testList
                ),
                onAction = {},
                onNavigateBack = {}
            )
        }
        rule.onNodeWithText(renameDialogTitleStr).assertDoesNotExist()
        rule.onNodeWithContentDescription(renameButtonStr).performClick()
        rule.onNodeWithText(renameDialogTitleStr).assertExists()
        rule.onNodeWithText(renameDialogConfirmStr).performClick()
        rule.onNodeWithText(renameDialogTitleStr).assertDoesNotExist()
    }
    
    @Test
    fun renamingDialog_isHidden_after_dismissed() {
        rule.setContent {
            ListScreen(
                state = UiState(
                    list = testList
                ),
                onAction = {},
                onNavigateBack = {}
            )
        }
        rule.onNodeWithText(renameDialogTitleStr).assertDoesNotExist()
        rule.onNodeWithContentDescription(renameButtonStr).performClick()
        rule.onNodeWithText(renameDialogTitleStr).assertExists()
        rule.onNodeWithText(renameDialogDismissStr).performClick()
        rule.onNodeWithText(renameDialogTitleStr).assertDoesNotExist()
    }
    
    @Test
    fun actionIsSent_after_renamingDialog_confirmed() {
        var isSent = false
        rule.setContent {
            ListScreen(
                state = UiState(
                    list = testList
                ),
                onAction = { if (it is Action.RenameList) isSent = true },
                onNavigateBack = {}
            )
        }
        rule.onNodeWithText(renameDialogTitleStr).assertDoesNotExist()
        rule.onNodeWithContentDescription(renameButtonStr).performClick()
        rule.onNodeWithText(renameDialogTitleStr).assertExists()
        rule.onNodeWithText(renameDialogConfirmStr).performClick()
        assertThat(isSent).isTrue()
    }
    
    
    @Test
    fun deletionDialog_isShown_after_deleteButtonClick() {
        rule.setContent {
            ListScreen(
                state = UiState(
                    list = testList
                ),
                onAction = {},
                onNavigateBack = {}
            )
        }
        rule.onNodeWithText(deletionDialogTitleStr).assertDoesNotExist()
        rule.onNodeWithContentDescription(deleteButtonStr).performClick()
        rule.onNodeWithText(deletionDialogTitleStr).assertExists()
    }
    
    @Test
    fun deletionDialog_isHidden_after_confirmed() {
        rule.setContent {
            ListScreen(
                state = UiState(
                    list = testList
                ),
                onAction = {},
                onNavigateBack = {}
            )
        }
        rule.onNodeWithText(deletionDialogTitleStr).assertDoesNotExist()
        rule.onNodeWithContentDescription(deleteButtonStr).performClick()
        rule.onNodeWithText(deletionDialogTitleStr).assertExists()
        rule.onNodeWithText(deletionDialogConfirmStr).performClick()
        rule.onNodeWithText(deletionDialogTitleStr).assertDoesNotExist()
    }
    
    @Test
    fun deletionDialog_isHidden_after_dismissed() {
        rule.setContent {
            ListScreen(
                state = UiState(
                    list = testList
                ),
                onAction = {},
                onNavigateBack = {}
            )
        }
        rule.onNodeWithText(deletionDialogTitleStr).assertDoesNotExist()
        rule.onNodeWithContentDescription(deleteButtonStr).performClick()
        rule.onNodeWithText(deletionDialogTitleStr).assertExists()
        rule.onNodeWithText(deletionDialogDismissStr).performClick()
        rule.onNodeWithText(deletionDialogTitleStr).assertDoesNotExist()
    }
    
    @Test
    fun actionIsSent_after_deletionDialog_confirmed() {
        var isSent = false
        rule.setContent {
            ListScreen(
                state = UiState(
                    list = testList
                ),
                onAction = { if (it is Action.DeleteList) isSent = true },
                onNavigateBack = {}
            )
        }
        rule.onNodeWithText(deletionDialogTitleStr).assertDoesNotExist()
        rule.onNodeWithContentDescription(deleteButtonStr).performClick()
        rule.onNodeWithText(deletionDialogTitleStr).assertExists()
        rule.onNodeWithText(deletionDialogConfirmStr).performClick()
        assertThat(isSent).isTrue()
    }
    
    
    @Test
    fun itemCreationDialog_isShown_after_fabClick() {
        rule.setContent {
            ListScreen(
                state = UiState(
                    list = testList
                ),
                onAction = {},
                onNavigateBack = {}
            )
        }
        rule.onNodeWithText(itemCreationDialogTitleStr).assertDoesNotExist()
        rule.onNodeWithContentDescription(fabStr).performClick()
        rule.onNodeWithText(itemCreationDialogTitleStr).assertExists()
    }
    
    @Test
    fun itemCreationDialog_isHidden_after_confirmed() {
        rule.setContent {
            ListScreen(
                state = UiState(
                    list = testList
                ),
                onAction = {},
                onNavigateBack = {}
            )
        }
        rule.onNodeWithText(itemCreationDialogTitleStr).assertDoesNotExist()
        rule.onNodeWithContentDescription(fabStr).performClick()
        rule.onNodeWithText(itemCreationDialogTitleStr).assertExists()
        rule.onNodeWithText(itemCreationDialogNameLabelStr).performTextInput("valid name")
        rule.onNodeWithText(itemCreationDialogAmountLabelStr).performTextInput("1")
        rule.onNodeWithText(itemCreationDialogPriceLabelStr).performTextInput("1f")
        rule.onNodeWithText(itemCreationDialogConfirmStr).performClick()
        rule.onNodeWithText(itemCreationDialogTitleStr).assertDoesNotExist()
    }
    
    @Test
    fun itemCreationDialog_isHidden_after_dismissed() {
        rule.setContent {
            ListScreen(
                state = UiState(
                    list = testList
                ),
                onAction = {},
                onNavigateBack = {}
            )
        }
        rule.onNodeWithText(itemCreationDialogTitleStr).assertDoesNotExist()
        rule.onNodeWithContentDescription(fabStr).performClick()
        rule.onNodeWithText(itemCreationDialogTitleStr).assertExists()
        rule.onNodeWithText(itemCreationDialogDismissStr).performClick()
        rule.onNodeWithText(itemCreationDialogTitleStr).assertDoesNotExist()
    }
    
    @Test
    fun actionIsSent_after_itemCreationDialog_confirmed() {
        var isSent = false
        rule.setContent {
            ListScreen(
                state = UiState(
                    list = testList
                ),
                onAction = { if (it is Action.AddItem) isSent = true },
                onNavigateBack = {}
            )
        }
        rule.onNodeWithText(itemCreationDialogTitleStr).assertDoesNotExist()
        rule.onNodeWithContentDescription(fabStr).performClick()
        rule.onNodeWithText(itemCreationDialogTitleStr).assertExists()
        rule.onNodeWithText(itemCreationDialogNameLabelStr).performTextInput("valid name")
        rule.onNodeWithText(itemCreationDialogAmountLabelStr).performTextInput("1")
        rule.onNodeWithText(itemCreationDialogPriceLabelStr).performTextInput("1f")
        rule.onNodeWithText(itemCreationDialogConfirmStr).performClick()
        assertThat(isSent).isTrue()
    }
    
    
    @Test
    fun itemsList_isShown_when_itemsIsNotEmpty() {
        rule.setContent {
            ListScreen(
                state = UiState(
                    list = testList,
                    items = listOf(testItem)
                ),
                onAction = {},
                onNavigateBack = {}
            )
        }
        rule.onNodeWithText(testItem.name).assertExists()
        rule.onNodeWithText(noItemsStr).assertDoesNotExist()
    }
    
    @Test
    fun emptyContent_isShown_when_itemsIsEmpty() {
        rule.setContent {
            ListScreen(
                state = UiState(
                    list = testList,
                    items = emptyList()
                ),
                onAction = {},
                onNavigateBack = {}
            )
        }
        rule.onNodeWithText(noItemsStr).assertExists()
        rule.onNodeWithText(testItem.name).assertDoesNotExist()
    }
}
