package nikmax.shopinglist.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nikmax.shopinglist.core.Currency
import nikmax.shopinglist.core.ui.ListUi

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun ListDetailsTopBar(
    list: ListUi,
    onBackClick: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val isExpanded = scrollBehavior.state.collapsedFraction < 0.5f
    MediumFlexibleTopAppBar(
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        title = {
            Text(list.name)
        },
        subtitle = {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("${list.completedItems}/${list.items}")
                Spacer(Modifier.weight(1f))
                Text("${list.completedPrice}/${list.totalPrice} ${list.currency.name}")
                if (isExpanded) Spacer(Modifier.width(16.dp))
            }
        },
        actions = {
            IconButton(
                onClick = { onRenameClick() }
            ) {
                Icon(Icons.Outlined.Edit, stringResource(R.string.rename_list))
            }
            IconButton(
                onClick = { onDeleteClick() }
            ) {
                Icon(Icons.Outlined.Delete, stringResource(R.string.delete_list))
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun Preview() {
    ListDetailsTopBar(
        list = ListUi(
            name = "List name",
            creationDate = 0,
            id = 0,
            items = 2,
            completedItems = 1,
            totalPrice = 42f,
            completedPrice = 10f,
            currency = Currency.USD
        ),
        onBackClick = {},
        onRenameClick = {},
        onDeleteClick = {},
        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    )
}
