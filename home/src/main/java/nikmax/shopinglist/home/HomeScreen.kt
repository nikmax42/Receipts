package nikmax.shopinglist.home

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import nikmax.shopinglist.core.ui.ListUi
import nikmax.shopinglist.core.ui.PlusFab

@Composable
fun HomeScreen(
    onNavigateToListDetails: (ListUi) -> Unit,
    vm: HomeVm = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    
    LaunchedEffect(Unit) {
        vm.onAction(Action.ShowAllLists)
    }
    
    HomeScreen(
        state = state,
        onAction = vm::onAction,
        onListOpen = { onNavigateToListDetails(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@VisibleForTesting
@Composable
internal fun HomeScreen(
    state: UiState,
    onAction: (Action) -> Unit,
    onListOpen: (ListUi) -> Unit
) {
    val selectedLists by remember(state.lists) {
        derivedStateOf { state.lists.filter { it.isSelected } }
    }
    
    val selectionScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val searchScrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(
            connection = when (selectedLists.isEmpty()) {
                true -> searchScrollBehavior.nestedScrollConnection
                false -> selectionScrollBehavior.nestedScrollConnection
            }
        ),
        topBar = {
            AnimatedContent(selectedLists.isEmpty()) { selectionIsEmpty ->
                when (selectionIsEmpty) {
                    true -> SearchTopBar(
                        results = state.searchResults,
                        onSearch = { query -> onAction(Action.SearchLists(query)) },
                        onListOpen = { onListOpen(it) },
                        scrollBehavior = searchScrollBehavior
                    )
                    false -> {
                        var deletionConfirmationDialogIsShown by remember { mutableStateOf(false) }
                        SelectionTopBar(
                            lists = state.lists,
                            selectedLists = selectedLists,
                            onDelete = {
                                deletionConfirmationDialogIsShown = true
                            },
                            onClear = { onAction(Action.ClearListsSelection) },
                            scrollBehavior = selectionScrollBehavior
                        )
                        if (deletionConfirmationDialogIsShown) {
                            ListsDeletionDialog(
                                onConfirm = {
                                    deletionConfirmationDialogIsShown = false
                                    onAction(Action.DeleteLists(selectedLists))
                                },
                                onDismiss = {
                                    deletionConfirmationDialogIsShown = false
                                }
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = { //todo add show/hide animation on scroll
            var listCreationDialogIsShown by remember { mutableStateOf(false) }
            PlusFab(
                onClick = {
                    listCreationDialogIsShown = true
                },
                contentDescription = stringResource(R.string.create_list)
            )
            if (listCreationDialogIsShown) {
                ListCreationDialog(
                    onConfirm = { newList ->
                        listCreationDialogIsShown = false
                        onAction(Action.CreateList(newList))
                    },
                    onDismiss = {
                        listCreationDialogIsShown = false
                    }
                )
            }
        }
    ) { paddings ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
        ) {
            AnimatedContent(state.lists.isEmpty()) { isEmpty ->
                when (isEmpty) {
                    true -> when (state.isLaunching) {
                        true -> {}
                        false -> NoListsCreated()
                    }
                    false -> LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.TopCenter)
                    ) {
                        items(state.lists) { list ->
                            ListCard(
                                list = list,
                                onClick = {
                                    when (selectedLists.isNotEmpty()) {
                                        true -> onAction(Action.ChangeListSelection(list))
                                        false -> onListOpen(list)
                                    }
                                },
                                onLongClick = {
                                    onAction(Action.ChangeListSelection(list))
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
