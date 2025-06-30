package nikmax.shopinglist.home

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import nikmax.shopinglist.core.ui.ListUi
import java.text.DateFormat
import java.util.Date

@Composable
internal fun ListCard(
    list: ListUi,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    val isCompleted = list.items == list.completedItems
    
    val cardColors: CardColors =
        if (list.isSelected) ListCardTokens.selectedColors()
        else if (isCompleted) ListCardTokens.completedColors()
        else ListCardTokens.normalColors()
    
    val cardElevation: CardElevation =
        if (list.isSelected) ListCardTokens.selectedElevation()
        else if (isCompleted) ListCardTokens.completedElevation()
        else ListCardTokens.normalElevation()
    
    val textColorStyle: TextStyle =
        if (list.isSelected) ListCardTokens.selectedTextStyle()
        else if (isCompleted) ListCardTokens.completedTextStyle()
        else ListCardTokens.normalTextStyle()
    
    
    Card(
        colors = cardColors,
        elevation = cardElevation,
        modifier = Modifier
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            )
            .then(modifier)
    ) {
        ProvideTextStyle(textColorStyle) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        list.name,
                        style = MaterialTheme.typography.titleMedium.merge(textColorStyle),
                        maxLines = 1,
                        overflow = TextOverflow.MiddleEllipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        list.creationDate.toLocalDateTimeString(),
                        style = MaterialTheme.typography.labelLarge.merge(textColorStyle),
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        when (list.completedItems > 0 && !isCompleted) {
                            true -> stringResource(R.string.x_of_y_items, list.completedItems, list.items)
                            false -> stringResource(R.string.x_items, list.items)
                        },
                        style = MaterialTheme.typography.titleSmall.merge(textColorStyle),
                        maxLines = 1,
                        overflow = TextOverflow.MiddleEllipsis,
                    )
                    Text(
                        when (list.completedPrice > 0 && !isCompleted) {
                            true -> "${list.completedPrice}/${list.totalPrice} ${list.currency.name}"
                            false -> "${list.totalPrice} ${list.currency.name}"
                        },
                        style = MaterialTheme.typography.titleSmall.merge(textColorStyle),
                        textAlign = TextAlign.End,
                    )
                }
            }
        }
    }
}

internal object ListCardTokens {
    @Composable fun normalColors() = CardDefaults.elevatedCardColors()
    @Composable fun normalElevation() = CardDefaults.elevatedCardElevation()
    @Composable fun normalTextStyle() = TextStyle(color = MaterialTheme.colorScheme.onSurface)
    
    
    @Composable fun completedColors() = CardDefaults.cardColors()
    @Composable fun completedElevation() = CardDefaults.cardElevation()
    @Composable fun completedTextStyle() = TextStyle(color = MaterialTheme.colorScheme.onSurface)
    
    
    @Composable fun selectedColors() = CardDefaults.cardColors().copy(
        containerColor = MaterialTheme.colorScheme.secondary
    )
    
    @Composable fun selectedElevation() = CardDefaults.cardElevation()
    @Composable fun selectedTextStyle() = TextStyle(color = MaterialTheme.colorScheme.onSecondary)
}

private fun Long.toLocalDateTimeString(): String {
    val date = Date(this * 1000L)
    val format = DateFormat.getDateTimeInstance()
    return format.format(date)
}
