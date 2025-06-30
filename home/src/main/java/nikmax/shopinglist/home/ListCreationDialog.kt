package nikmax.shopinglist.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import nikmax.shopinglist.core.Currency
import nikmax.shopinglist.core.Validators
import nikmax.shopinglist.core.Validators.validateName
import nikmax.shopinglist.core.ui.ListUi
import nikmax.shopinglist.core.ui.TextInputDialog

@Composable
internal fun ListCreationDialog(
    onConfirm: (list: ListUi) -> Unit,
    onDismiss: () -> Unit
) {
    var nameState = rememberTextFieldState()
    val nameValidity = remember(nameState.text) {
        nameState.text.toString().validateName()
    }
    
    var currencyState = rememberTextFieldState()
    val currencyIsValid = remember(currencyState.text) {
        Currency.entries.any { it.name == currencyState.text }
    }
    
    val allFieldsIsValid = remember(nameValidity, currencyIsValid) {
        nameValidity == Validators.NameValidity.VALID && currencyIsValid
    }
    
    fun onConfirmClick() {
        if (allFieldsIsValid) {
            onConfirm(
                ListUi(
                    name = nameState.text.toString(),
                    creationDate = System.currentTimeMillis(),
                    id = 0,
                    items = 0,
                    completedItems = 0,
                    totalPrice = 0f,
                    completedPrice = 0f,
                    currency = Currency.valueOf(currencyState.text.toString())
                )
            )
        }
    }
    
    TextInputDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                enabled = allFieldsIsValid,
                onClick = { onConfirmClick() }
            ) {
                Text(stringResource(R.string.create))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(R.string.cancel))
            }
        },
        icon = { Icon(Icons.Outlined.Receipt, stringResource(R.string.icon)) },
        title = { Text(stringResource(R.string.create_list)) },
        textFields = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val focusManager = LocalFocusManager.current
                OutlinedTextField(
                    state = nameState,
                    label = { Text(stringResource(R.string.list_name)) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    onKeyboardAction = {
                        if (nameValidity == Validators.NameValidity.VALID)
                            focusManager.moveFocus(FocusDirection.Next)
                    }
                )
                Box(Modifier.fillMaxWidth()) {
                    var textFieldWidth by remember { mutableStateOf(0) }
                    var textFieldIsFocused by remember { mutableStateOf(false) }
                    // show tips while text field is focused and valid currency is not yet entered
                    var currencyTips by remember(textFieldIsFocused, currencyState.text) {
                        val foundCurrencies = when (textFieldIsFocused) {
                            true -> when (currencyIsValid) {
                                true -> emptyList()
                                false -> Currency.entries.filter {
                                    it.name.contains(currencyState.text, ignoreCase = true)
                                }
                            }
                            false -> emptyList()
                        }
                        mutableStateOf(foundCurrencies)
                    }
                    OutlinedTextField(
                        state = currencyState,
                        label = { Text(stringResource(R.string.currency)) },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    currencyState.clearText()
                                    currencyTips = emptyList()
                                }
                            ) {
                                Icon(Icons.Outlined.Cancel, stringResource(R.string.clear))
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        onKeyboardAction = { onConfirmClick() },
                        modifier = Modifier
                            .onGloballyPositioned {
                                textFieldWidth = it.size.width
                            }
                            .onFocusChanged {
                                textFieldIsFocused = it.isFocused
                            }
                    )
                    var dropdownItemHeight by remember { mutableStateOf(0) }
                    DropdownMenu(
                        expanded = currencyTips.isNotEmpty(),
                        onDismissRequest = { currencyTips = emptyList() },
                        properties = PopupProperties(
                            focusable = false
                        ),
                        modifier = Modifier
                            .width(
                                with(LocalDensity.current) { textFieldWidth.toDp() }
                            )
                            .heightIn(
                                min = with(LocalDensity.current) { dropdownItemHeight.toDp() },
                                max = with(LocalDensity.current) { (dropdownItemHeight * 3).toDp() }
                            )
                    ) {
                        currencyTips.forEach { currency ->
                            DropdownMenuItem(
                                text = { Text(currency.name) },
                                onClick = {
                                    currencyState.setTextAndPlaceCursorAtEnd(currency.name)
                                },
                                modifier = Modifier.onGloballyPositioned {
                                    dropdownItemHeight = it.size.height
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    ListCreationDialog(
        onConfirm = { },
        onDismiss = { }
    )
}
