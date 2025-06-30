package nikmax.shopinglist.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import nikmax.shopinglist.core.Currency
import nikmax.shopinglist.core.Validators
import nikmax.shopinglist.core.Validators.validateAmount
import nikmax.shopinglist.core.Validators.validateName
import nikmax.shopinglist.core.Validators.validatePrice
import nikmax.shopinglist.core.ui.ItemUi
import nikmax.shopinglist.core.ui.TextInputDialog

@Composable
internal fun ItemCreationDialog(
    listId: Int,
    listCurrency: Currency,
    onConfirm: (ItemUi) -> Unit,
    onDismiss: () -> Unit
) {
    val name = rememberTextFieldState()
    val amount = rememberTextFieldState("1")
    val price = rememberTextFieldState()
    
    val nameValidity = remember(name.text) { name.text.toString().validateName() }
    val amountValidity = remember(amount.text) { amount.text.toString().validateAmount() }
    val priceValidity = remember(price.text) { price.text.toString().validatePrice() }
    val allFieldsIsValid = remember(nameValidity, amountValidity, priceValidity) {
        nameValidity == Validators.NameValidity.VALID
                && amountValidity == Validators.AmountValidity.VALID
                && priceValidity == Validators.PriceValidity.VALID
    }
    
    val onConfirm = {
        if (allFieldsIsValid) onConfirm(
            ItemUi(
                name = name.text.toString(),
                price = price.text.toString().toFloat(),
                amount = amount.text.toString().toInt(),
                isComplete = false,
                creationDate = System.currentTimeMillis(),
                listId = listId,
                id = 0,
                currency = listCurrency
            )
        )
    }
    
    TextInputDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                enabled = allFieldsIsValid,
                onClick = { onConfirm() }
            ) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(R.string.cancel))
            }
        },
        icon = { Icon(Icons.Outlined.AddShoppingCart, stringResource(R.string.icon)) },
        title = { Text(stringResource(R.string.add_item)) },
        textFields = {
            Column {
                val focusManager = LocalFocusManager.current
                OutlinedTextField(
                    state = name,
                    label = { Text(stringResource(R.string.name)) },
                    lineLimits = TextFieldLineLimits.SingleLine,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    onKeyboardAction = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        state = amount,
                        label = { Text(stringResource(R.string.amount)) },
                        lineLimits = TextFieldLineLimits.SingleLine,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        onKeyboardAction = {
                            focusManager.moveFocus(FocusDirection.Next)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    OutlinedTextField(
                        state = price,
                        label = { Text(stringResource(R.string.price)) },
                        lineLimits = TextFieldLineLimits.SingleLine,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        onKeyboardAction = { onConfirm() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }
        }
    )
}
