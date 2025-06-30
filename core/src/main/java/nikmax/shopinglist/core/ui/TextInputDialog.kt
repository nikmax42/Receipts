package nikmax.shopinglist.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInputDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)?,
    icon: @Composable (() -> Unit)?,
    title: @Composable (() -> Unit)?,
    textFields: @Composable (() -> Unit)?,
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    iconContentColor: Color = AlertDialogDefaults.iconContentColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    properties: DialogProperties = DialogProperties()
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        properties = properties
    ) {
        TextInputDialogContent(
            icon = icon,
            title = title,
            textFields = textFields,
            buttons = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    dismissButton?.invoke()
                    confirmButton()
                }
            },
            buttonContentColor = MaterialTheme.colorScheme.primary,
            shape = shape,
            containerColor = containerColor,
            iconContentColor = iconContentColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            tonalElevation = tonalElevation,
        )
    }
}

@Composable
private fun TextInputDialogContent(
    buttons: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)?,
    title: (@Composable () -> Unit)?,
    textFields: @Composable (() -> Unit)?,
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color,
    iconContentColor: Color,
    titleContentColor: Color,
    textContentColor: Color,
    buttonContentColor: Color,
    tonalElevation: Dp
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = containerColor,
        tonalElevation = tonalElevation,
    ) {
        Column(modifier = Modifier.padding(DialogPadding)) {
            icon?.let {
                CompositionLocalProvider(LocalContentColor provides iconContentColor) {
                    Box(
                        Modifier
                            .padding(IconPadding)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        icon()
                    }
                }
            }
            title?.let {
                ProvideContentColorTextStyle(
                    contentColor = titleContentColor,
                    textStyle = MaterialTheme.typography.headlineSmall
                ) {
                    Box(
                        // Align the title to the center when an icon is present.
                        Modifier
                            .padding(TitlePadding)
                            .align(
                                if (icon == null) {
                                    Alignment.Start
                                }
                                else {
                                    Alignment.CenterHorizontally
                                }
                            )
                    ) {
                        title()
                    }
                }
            }
            textFields?.let {
                val textStyle = MaterialTheme.typography.bodyLarge
                ProvideContentColorTextStyle(
                    contentColor = textContentColor,
                    textStyle = textStyle
                ) {
                    Box(
                        Modifier
                            .weight(weight = 1f, fill = false)
                            .padding(TextPadding)
                            .align(Alignment.Start)
                    ) {
                        textFields()
                    }
                }
            }
            Box(modifier = Modifier.align(Alignment.End)) {
                val textStyle = MaterialTheme.typography.labelLarge
                ProvideContentColorTextStyle(
                    contentColor = buttonContentColor,
                    textStyle = textStyle,
                    content = buttons
                )
            }
        }
    }
}

/**
 * ProvideContentColorTextStyle
 *
 * A convenience method to provide values to both LocalContentColor and LocalTextStyle in one call.
 * This is less expensive than nesting calls to CompositionLocalProvider.
 *
 * Text styles will be merged with the current value of LocalTextStyle.
 */
@Composable
private fun ProvideContentColorTextStyle(
    contentColor: Color,
    textStyle: TextStyle,
    content: @Composable () -> Unit
) {
    val mergedStyle = LocalTextStyle.current.merge(textStyle)
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides mergedStyle,
        content = content
    )
}

// Paddings for each of the dialog's parts.
private val DialogPadding = PaddingValues(all = 24.dp)
private val IconPadding = PaddingValues(bottom = 16.dp)
private val TitlePadding = PaddingValues(bottom = 16.dp)
private val TextPadding = PaddingValues(bottom = 24.dp)
