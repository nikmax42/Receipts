package nikmax.shopinglist.list

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import nikmax.shopinglist.core.Validators
import nikmax.shopinglist.core.Validators.validateName
import nikmax.shopinglist.core.ui.TextInputDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ListRenamingDialog(
    initialName: String,
    onConfirm: (newName: String) -> Unit,
    onDismiss: () -> Unit,
) {
    val name = rememberTextFieldState(initialName)
    val nameValidity = remember(name.text) {
        name.text.toString().validateName()
    }
    
    val onConfirmClick = {
        if (nameValidity == Validators.NameValidity.VALID) {
            onConfirm(name.text.toString())
        }
    }
    
    TextInputDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                enabled = nameValidity == Validators.NameValidity.VALID,
                onClick = { onConfirmClick() }
            ) {
                Text(stringResource(R.string.rename))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(R.string.cancel))
            }
        },
        icon = {
            Icon(
                Icons.Outlined.EditNote,
                contentDescription = stringResource(R.string.icon)
            )
        },
        title = { Text(stringResource(R.string.rename_list)) },
        textFields = {
            OutlinedTextField(
                state = name,
                lineLimits = TextFieldLineLimits.SingleLine,
                label = { Text(stringResource(R.string.name)) },
                isError = nameValidity != Validators.NameValidity.VALID,
                supportingText = {
                    when (nameValidity) {
                        Validators.NameValidity.VALID -> null
                        Validators.NameValidity.BLANK -> Text(stringResource(R.string.invalid_name))
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                onKeyboardAction = { onConfirmClick() }
            )
        }
    )
}

@Preview
@Composable
private fun Preview() {
    ListRenamingDialog(
        initialName = "initial name",
        onConfirm = {},
        onDismiss = {}
    )
}
