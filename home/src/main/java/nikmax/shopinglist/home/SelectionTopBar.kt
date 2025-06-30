package nikmax.shopinglist.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import nikmax.shopinglist.core.ui.ListUi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun SelectionTopBar(
    lists: List<ListUi>,
    selectedLists: List<ListUi>,
    onDelete: () -> Unit,
    onClear: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { onClear() }) {
                Icon(Icons.Outlined.Clear, stringResource(R.string.clear_selection))
            }
        },
        title = {
            Text(stringResource(R.string.x_from_y_lists_selected, selectedLists.size, lists.size))
        },
        // todo count selected lists total price, grouped by currency
        /* subtitle = {
            Text("${selectedLists.sumOf { it.totalPrice }}")
        }, */
        actions = {
            IconButton(onClick = { onDelete() }) {
                Icon(Icons.Outlined.Delete, stringResource(R.string.delete_selected_lists))
            }
        },
        scrollBehavior = scrollBehavior
    )
}
