package nikmax.shopinglist.list

import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import nikmax.shopinglist.core.ui.IllustrationWithSupportingText

@Composable
internal fun NoItemsCreated(modifier: Modifier = Modifier) {
    IllustrationWithSupportingText(
        illustration = {
            Image(
                painterResource(nikmax.shopinglist.core.R.drawable.product_hunt_bro),
                null
            )
        },
        supportingText = { Text(stringResource(R.string.no_items_created)) },
        modifier = modifier
    )
}
