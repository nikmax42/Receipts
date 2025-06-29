package nikmax.shopinglist.list

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import nikmax.shopinglist.core.Currency
import nikmax.shopinglist.core.sumOf
import nikmax.shopinglist.core.ui.ItemUi
import nikmax.shopinglist.core.ui.ListUi
import nikmax.shopinglist.core.ui.PlusFab

@Composable
fun ListScreen(
    listId: Int,
    onNavigateBack: () -> Unit,
    vm: ListVm = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    
    LaunchedEffect(Unit) {
        vm.onAction(Action.LoadList(listId))
    }
    
    LaunchedEffect(state) {
        if (state.isDeleted) onNavigateBack()
    }
    
    ListScreen(
        state = state,
        onAction = vm::onAction,
        onNavigateBack = onNavigateBack
    )
}

@VisibleForTesting
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ListScreen(
    state: UiState,
    onAction: (Action) -> Unit,
    onNavigateBack: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()
    val fabVisible by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
    
    Scaffold(
        topBar = {
            if (state.list != null) {
                var listRenamingDialogIsShown by remember { mutableStateOf(false) }
                var listDeletionDialogIsShown by remember { mutableStateOf(false) }
                ListDetailsTopBar(
                    list = state.list,
                    onBackClick = { onNavigateBack() },
                    onRenameClick = {
                        listRenamingDialogIsShown = true
                    },
                    onDeleteClick = {
                        listDeletionDialogIsShown = true
                    },
                    scrollBehavior = scrollBehavior
                )
                if (listRenamingDialogIsShown)
                    ListRenamingDialog(
                        initialName = state.list.name,
                        onConfirm = { newName ->
                            listRenamingDialogIsShown = false
                            onAction(Action.RenameList(state.list, newName))
                        },
                        onDismiss = {
                            listRenamingDialogIsShown = false
                        }
                    )
                if (listDeletionDialogIsShown)
                    ListDeletionDialog(
                        onConfirm = {
                            listDeletionDialogIsShown = false
                            onAction(Action.DeleteList(state.list))
                        },
                        onDismiss = {
                            listDeletionDialogIsShown = false
                        }
                    )
            }
        },
        floatingActionButton = {
            if (state.list != null) {
                var itemCreationDialogIsVisible by remember { mutableStateOf(false) }
                PlusFab(
                    onClick = {
                        itemCreationDialogIsVisible = true
                    },
                    contentDescription = stringResource(R.string.add_item),
                    modifier = Modifier.animateFloatingActionButton(
                        visible = fabVisible,
                        alignment = Alignment.BottomEnd
                    )
                )
                if (itemCreationDialogIsVisible)
                    ItemCreationDialog(
                        listId = state.list.id,
                        listCurrency = state.list.currency,
                        onConfirm = { newItem ->
                            itemCreationDialogIsVisible = false
                            onAction(Action.AddItem(newItem))
                        },
                        onDismiss = {
                            itemCreationDialogIsVisible = false
                        }
                    )
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddings ->
        Box(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(state.items.isEmpty()) { isEmpty ->
                when (isEmpty) {
                    true -> if (state.list != null) NoItemsCreated()
                    false -> LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState
                    ) {
                        items(state.items) { item ->
                            Item(
                                item = item,
                                onUpdate = { updatedItem ->
                                    onAction(Action.UpdateItem(item, updatedItem))
                                },
                                onDelete = { onAction(Action.DeleteItem(item)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem()
                            )
                            HorizontalDivider(Modifier.fillMaxWidth())
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ListPreview() {
    val items = listOf(
        ItemUi(
            name = "Some item",
            price = 100f,
            amount = 1,
            isComplete = false,
            creationDate = 0,
            listId = 0,
            id = 0,
            currency = Currency.USD
        )
    )
    ListScreen(
        state = UiState(
            list = ListUi(
                name = "List name",
                creationDate = 0,
                id = 0,
                items = items.size,
                completedItems = items.count { it.isComplete },
                totalPrice = items.sumOf { it.price },
                completedPrice = items.sumOf { if (it.isComplete) it.price else 0f },
                currency = Currency.USD
            ),
            items = items
        ),
        onAction = {},
        onNavigateBack = {}
    )
}
