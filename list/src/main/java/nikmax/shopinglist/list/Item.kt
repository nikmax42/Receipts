package nikmax.shopinglist.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nikmax.shopinglist.core.Currency
import nikmax.shopinglist.core.Validators
import nikmax.shopinglist.core.Validators.validateAmount
import nikmax.shopinglist.core.Validators.validatePrice
import nikmax.shopinglist.core.ui.ItemUi

@Composable
internal fun Item(
    item: ItemUi,
    onUpdate: (ItemUi) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var revealPosition = remember { mutableStateOf(RevealPosition.COLLAPSED) }
    SwipeToRevealContainer(
        position = revealPosition,
        leftActions = {},
        rightActions = {
            IconButton(
                onClick = {
                    revealPosition.value = RevealPosition.COLLAPSED
                    onDelete()
                },
                shape = RectangleShape,
                colors = IconButtonDefaults.iconButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                modifier = Modifier
                    .width(64.dp)
                    .fillMaxHeight()
            ) {
                Icon(Icons.Outlined.Delete, stringResource(R.string.delete_item))
            }
        },
        modifier = modifier
    ) {
        val focusManager = LocalFocusManager.current
        LaunchedEffect(Unit) {
            focusManager.clearFocus()
        }
        Surface(
            modifier = Modifier
                .clickable(
                    onClick = { focusManager.clearFocus() },
                    indication = null,
                    interactionSource = null
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                var checkboxIsChecked by remember(item) {
                    mutableStateOf(item.isComplete)
                }
                val checkboxDescription = when (checkboxIsChecked) {
                    true -> stringResource(R.string.is_completed)
                    false -> stringResource(R.string.is_not_completed)
                }
                var amount by remember {
                    mutableStateOf(
                        TextFieldValue(item.amount.toString())
                    )
                }
                var price by remember {
                    mutableStateOf(
                        TextFieldValue(item.price.toString())
                    )
                }
                val amountValidity = remember(amount.text) {
                    amount.text.validateAmount()
                }
                val priceValidity = remember(price.text) {
                    price.text.validatePrice()
                }
                
                LaunchedEffect(checkboxIsChecked, amount.text, price.text) {
                    if (amountValidity == Validators.AmountValidity.VALID && priceValidity == Validators.PriceValidity.VALID) {
                        onUpdate(
                            item.copy(
                                isComplete = checkboxIsChecked,
                                amount = amount.text.toInt(),
                                price = price.text.toFloat()
                            )
                        )
                    }
                }
                Checkbox(
                    checked = checkboxIsChecked,
                    onCheckedChange = {
                        checkboxIsChecked = it
                    },
                    modifier = Modifier.semantics {
                        contentDescription = checkboxDescription
                    }
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.headlineSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${item.price * item.amount} ${item.currency.name}",
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            textAlign = TextAlign.End,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        OutlinedTextField(
                            value = amount,
                            onValueChange = {
                                amount = it
                            },
                            label = { Text(stringResource(R.string.amount)) },
                            isError = amountValidity != Validators.AmountValidity.VALID,
                            supportingText = when (amountValidity) {
                                Validators.AmountValidity.VALID -> null
                                else -> {
                                    { Text(stringResource(R.string.positive_number_required)) }
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.NumberPassword
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                        OutlinedTextField(
                            value = price,
                            onValueChange = {
                                price = it
                            },
                            label = { Text(stringResource(R.string.price)) },
                            isError = priceValidity != Validators.PriceValidity.VALID,
                            supportingText = when (priceValidity) {
                                Validators.PriceValidity.VALID -> null
                                else -> {
                                    { Text(stringResource(R.string.number_required)) }
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    var item by remember {
        mutableStateOf(
            ItemUi(
                name = "Item name is way tooooo loooong",
                price = 42f,
                amount = -1,
                isComplete = false,
                creationDate = 0,
                listId = 0,
                id = 0,
                currency = Currency.USD
            )
        )
    }
    Item(
        item = item,
        onUpdate = { item = it },
        onDelete = {},
        modifier = Modifier.fillMaxWidth()
    )
}
