package nikmax.shopinglist.core.ui

import androidx.compose.foundation.Indication
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Modifier.repeatingClickable(
    interactionSource: MutableInteractionSource,
    enabled: Boolean,
    indication: Indication? = null,
    maxDelayMillis: Long = 1000,
    minDelayMillis: Long = 5,
    delayDecayFactor: Float = .20f,
    onClick: () -> Unit
): Modifier = composed {
    val currentClickListener by rememberUpdatedState(onClick)
    val scope = rememberCoroutineScope()
    
    pointerInput(interactionSource, enabled) {
        scope.launch {
            awaitEachGesture {
                val down = awaitFirstDown(requireUnconsumed = false)
                // Create a down press interaction
                val downPress = PressInteraction.Press(down.position)
                val heldButtonJob = launch {
                    // Send the press through the interaction source
                    interactionSource.emit(downPress)
                    var currentDelayMillis = maxDelayMillis
                    while (enabled && down.pressed) {
                        currentClickListener()
                        delay(currentDelayMillis)
                        val nextMillis = currentDelayMillis - (currentDelayMillis * delayDecayFactor)
                        currentDelayMillis = nextMillis.toLong().coerceAtLeast(minDelayMillis)
                    }
                }
                val up = waitForUpOrCancellation()
                heldButtonJob.cancel()
                // Determine whether a cancel or release occurred, and create the interaction
                val releaseOrCancel = when (up) {
                    null -> PressInteraction.Cancel(downPress)
                    else -> PressInteraction.Release(downPress)
                }
                launch {
                    // Send the result through the interaction source
                    interactionSource.emit(releaseOrCancel)
                }
            }
        }
    }.indication(
        interactionSource = interactionSource,
        indication = indication
    )
}
