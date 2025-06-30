package nikmax.shopinglist.home

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import nikmax.shopinglist.core.Currency
import nikmax.shopinglist.core.ui.ListUi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenKtTest {
    @get:Rule
    val rule = createComposeRule()
    
    private val context = InstrumentationRegistry.getInstrumentation().context
    private val searchBarPlaceholderStr = context.getString(R.string.search_placeholder)
    private val selectionBarTitleStr = context.getString(R.string.x_from_y_lists_selected, 1, 2)
    private val emptyListsContentStr = context.getString(R.string.no_lists_created)
    private val nothingFoundContentStr = context.getString(R.string.nothing_found)
    private val deleteButtonStr = context.getString(R.string.delete_selected_lists)
    private val deleteDialogTitleStr = context.getString(R.string.deletion_confirmation_title)
    private val deleteDialogConfirmStr = context.getString(R.string.delete)
    private val deleteDialogDismissStr = context.getString(R.string.cancel)
    private val fabTextStr = context.getString(R.string.create_list)
    private val listCreationDialogTitleStr = context.getString(R.string.create_list)
    private val listCreationDialogNameLabelStr = context.getString(R.string.list_name)
    private val listCreationDialogCurrencyLabelStr = context.getString(R.string.currency)
    private val listCreationDialogConfirmStr = context.getString(R.string.create)
    private val listCreationDialogDismissStr = context.getString(R.string.cancel)
    
    private val list1 = ListUi(
        name = "first",
        creationDate = 0,
        id = 0,
        items = 0,
        completedItems = 0,
        totalPrice = 0f,
        completedPrice = 0f,
        currency = Currency.USD
    )
    private val list2 = ListUi(
        name = "second",
        creationDate = 0,
        id = 0,
        items = 0,
        completedItems = 0,
        totalPrice = 0f,
        completedPrice = 0f,
        currency = Currency.USD
    )
    
    @Test
    fun searchTopBar_isDisplayed_when_selectionIsEmpty() {
        rule.setContent {
            HomeScreen(
                state = UiState(
                    lists = emptyList(),
                    searchResults = emptyList()
                ),
                onAction = {},
                onListOpen = {}
            )
        }
        rule.onNodeWithText(searchBarPlaceholderStr).assertExists()
    }
    
    @Test
    fun selectionTopBar_isDisplayed_IF_atLeastOneListSelected() {
        rule.setContent {
            HomeScreen(
                state = UiState(
                    lists = listOf(
                        list1.copy(
                            isSelected = true
                        ),
                        list2
                    ),
                    searchResults = emptyList()
                ),
                onAction = {},
                onListOpen = {}
            )
        }
        rule.onNodeWithText(selectionBarTitleStr).assertExists()
    }
    
    @Test
    fun emptyContent_isDisplayed_WHEN_listsIsEmpty_AND_notLoading() {
        rule.setContent {
            HomeScreen(
                state = UiState(
                    lists = emptyList(),
                    searchResults = emptyList(),
                    isLaunching = false
                ),
                onAction = {},
                onListOpen = {}
            )
        }
        rule.onNodeWithText(emptyListsContentStr).assertExists()
    }
    
    @Test
    fun normalContent_isDisplayed_WHEN_lists_isNotEmpty() {
        rule.setContent {
            HomeScreen(
                state = UiState(
                    lists = listOf(list1, list2),
                    searchResults = emptyList()
                ),
                onAction = {},
                onListOpen = {}
            )
        }
        rule.onNodeWithText(list1.name).assertExists()
        rule.onNodeWithText(emptyListsContentStr).assertDoesNotExist()
    }
    
    @Test
    fun listOpenCallback_isCalled_AFTER_shortClicked_IF_selection_isEmpty() {
        var isCalled = false
        rule.setContent {
            HomeScreen(
                state = UiState(
                    lists = listOf(list1, list2),
                    searchResults = emptyList()
                ),
                onAction = {},
                onListOpen = { isCalled = true }
            )
        }
        rule.onNodeWithText(selectionBarTitleStr).assertDoesNotExist()
        rule.onNodeWithText(list1.name).performClick()
        assertThat(isCalled).isTrue()
    }
    
    @Test
    fun selectionActionIsCalled_AFTER_listLongClick() {
        var isCalled = false
        rule.setContent {
            HomeScreen(
                state = UiState(
                    lists = listOf(list1, list2),
                    searchResults = emptyList()
                ),
                onAction = { action ->
                    if (action is Action.ChangeListSelection) isCalled = true
                },
                onListOpen = {}
            )
        }
        rule.onNodeWithText(list1.name).performTouchInput { longClick() }
        assertThat(isCalled).isTrue()
    }
    
    @Test
    fun selectionActionIsCalled_AFTER_listShortClick_IF_atLeastOneListSelected() {
        var isCalled = false
        rule.setContent {
            HomeScreen(
                state = UiState(
                    lists = listOf(
                        list1.copy(
                            isSelected = true
                        ),
                        list2
                    ),
                    searchResults = emptyList()
                ),
                onAction = { action ->
                    if (action is Action.ChangeListSelection) isCalled = true
                },
                onListOpen = {}
            )
        }
        rule.onNodeWithText(list1.name).performClick()
        assertThat(isCalled).isTrue()
    }
    
    
    @Test
    fun searchResultsEmptyContent_isShown_WHEN_searchResultsIsEmpty_AND_searchbarIsExpanded() {
        rule.setContent {
            HomeScreen(
                state = UiState(
                    lists = emptyList(),
                    searchResults = emptyList()
                ),
                onAction = {},
                onListOpen = {}
            )
        }
        rule.onNodeWithText(searchBarPlaceholderStr).performClick()
        rule.onNodeWithText(nothingFoundContentStr).assertExists()
    }
    
    @Test
    fun searchResults_isShown_WHEN_searchResultsIsNotEmpty_AND_searchbarIsExpanded() {
        rule.setContent {
            HomeScreen(
                state = UiState(
                    lists = emptyList(),
                    searchResults = listOf(list1)
                ),
                onAction = {},
                onListOpen = {}
            )
        }
        rule.onNodeWithText(searchBarPlaceholderStr).performClick()
        rule.onNodeWithText(list1.name).assertExists()
    }
    
    
    @Test
    fun deletionConfirmationDialog_isShown_AFTER_deleteButton_clicked() {
        rule.setContent {
            HomeScreen(
                state = UiState(
                    lists = listOf(
                        list1.copy(
                            isSelected = true
                        )
                    ),
                    searchResults = listOf(list1)
                ),
                onAction = {},
                onListOpen = {}
            )
        }
        rule.onNodeWithContentDescription(deleteButtonStr).performClick()
        rule.onNodeWithText(deleteDialogTitleStr).assertExists()
    }
    
    @Test
    fun deletionConfirmationDialog_isHidden_AFTER_confirmed() {
        rule.setContent {
            HomeScreen(
                state = UiState(
                    lists = listOf(
                        list1.copy(
                            isSelected = true
                        )
                    ),
                    searchResults = listOf(list1)
                ),
                onAction = {},
                onListOpen = {}
            )
        }
        rule.onNodeWithContentDescription(deleteButtonStr).performClick()
        rule.onNodeWithText(deleteDialogTitleStr).assertExists()
        rule.onNodeWithText(deleteDialogConfirmStr).performClick()
        rule.onNodeWithText(deleteDialogTitleStr).assertDoesNotExist()
    }
    
    @Test
    fun deletionConfirmationDialog_isHidden_AFTER_dismissed() {
        rule.setContent {
            HomeScreen(
                state = UiState(
                    lists = listOf(
                        list1.copy(
                            isSelected = true
                        )
                    ),
                    searchResults = listOf(list1)
                ),
                onAction = {},
                onListOpen = {}
            )
        }
        rule.onNodeWithContentDescription(deleteButtonStr).performClick()
        rule.onNodeWithText(deleteDialogTitleStr).assertExists()
        rule.onNodeWithText(deleteDialogDismissStr).performClick()
        rule.onNodeWithText(deleteDialogTitleStr).assertDoesNotExist()
    }
    
    fun deletionAction_isCalled_AFTER_DeletionConfirmationDialog_confirmed() {
        var isCalled = false
        rule.setContent {
            HomeScreen(
                state = UiState(
                    lists = listOf(
                        list1.copy(
                            isSelected = true
                        )
                    ),
                    searchResults = listOf(list1)
                ),
                onAction = { if (it is Action.DeleteLists) isCalled = true },
                onListOpen = {}
            )
        }
        rule.onNodeWithContentDescription(deleteButtonStr).performClick()
        rule.onNodeWithText(deleteDialogTitleStr).assertExists()
        rule.onNodeWithText(deleteDialogDismissStr).performClick()
        assertThat(isCalled).isTrue()
    }
    
    
    @Test
    fun listCreationDialog_isShown_AFTER_fab_clicked() {
        rule.setContent {
            HomeScreen(
                state = UiState(
                    lists = listOf(list1),
                    searchResults = emptyList()
                ),
                onAction = {},
                onListOpen = {}
            )
        }
        rule.onNodeWithContentDescription(fabTextStr).performClick()
        rule.onNodeWithText(listCreationDialogTitleStr).assertExists()
    }
    
    @Test
    fun listCreationDialog_isHidden_AFTER_confirmed() {
        rule.setContent {
            HomeScreen(
                state = UiState(
                    lists = listOf(list1),
                    searchResults = emptyList()
                ),
                onAction = {},
                onListOpen = {}
            )
        }
        rule.onNodeWithContentDescription(fabTextStr).performClick()
        rule.onNodeWithText(listCreationDialogTitleStr).assertExists()
        rule.onNodeWithText(listCreationDialogNameLabelStr).performTextInput("name")
        rule.onNodeWithText(listCreationDialogCurrencyLabelStr).performTextInput(Currency.USD.name)
        rule.onNodeWithText(listCreationDialogConfirmStr).performClick()
        rule.onNodeWithText(listCreationDialogTitleStr).assertDoesNotExist()
    }
    
    @Test
    fun listCreationDialog_isHidden_AFTER_dismissed() {
        rule.setContent {
            HomeScreen(
                state = UiState(
                    lists = listOf(list1),
                    searchResults = emptyList()
                ),
                onAction = {},
                onListOpen = {}
            )
        }
        rule.onNodeWithContentDescription(fabTextStr).performClick()
        rule.onNodeWithText(listCreationDialogTitleStr).assertExists()
        rule.onNodeWithText(listCreationDialogDismissStr).performClick()
        rule.onNodeWithText(listCreationDialogTitleStr).assertDoesNotExist()
    }
}
