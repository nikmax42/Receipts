package nikmax.shopinglist.list

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class RevealPosition {
    COLLAPSED,
    MOVING,
    LEFT,
    RIGHT
}

/**
 * Left and right [SwipeToRevealContainer] with actions on the both sides
 * @param isLeftRevealed is left actions fully expanded
 * @param isRightRevealed is right actions fully expanded
 * @param leftActions row-scoped menu hidden under the content left side and appears on left-to-right swipe
 * @param rightActions row-scoped menu hidden under the content right side and appears on right-to-left swipe
 * @param onFullyExpanded is called when actions are fully expanded
 * @param onFullyCollapsed is called when actions are fully collapsed
 * @param content item main content
 * */
@Composable
fun SwipeToRevealContainer(
    position: MutableState<RevealPosition>,
    leftActions: @Composable RowScope.() -> Unit,
    rightActions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    onFullyExpanded: () -> Unit = {},
    onFullyCollapsed: () -> Unit = {},
    animationSpec: AnimationSpec<Float> = spring(),
    content: @Composable () -> Unit
) {
    var leftMenuWidth by remember(leftActions) {
        mutableFloatStateOf(0f)
    }
    var rightMenuWidth by remember(rightActions) {
        mutableFloatStateOf(0f)
    }
    val contentOffset = remember {
        Animatable(initialValue = 0f)
    }
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(
        position.value,
    ) {
        when (position.value) {
            RevealPosition.MOVING -> {} //not in use for now
            RevealPosition.COLLAPSED -> if (contentOffset.value != 0f) {
                contentOffset.animateTo(0f)
                onFullyCollapsed()
            }
            RevealPosition.LEFT -> if (contentOffset.value > -rightMenuWidth) {
                contentOffset.animateTo(
                    -rightMenuWidth,
                    animationSpec
                )
                onFullyExpanded()
            }
            RevealPosition.RIGHT -> if (contentOffset.value < leftMenuWidth) {
                contentOffset.animateTo(
                    leftMenuWidth,
                    animationSpec
                )
                onFullyExpanded()
            }
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        //left menu
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .onSizeChanged {
                    leftMenuWidth = it.width.toFloat()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            leftActions()
        }
        //right menu
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .onSizeChanged {
                    rightMenuWidth = it.width.toFloat()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            rightActions()
        }
        //draggable main content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(contentOffset.value.roundToInt(), 0) }
                .pointerInput(leftMenuWidth, rightMenuWidth) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                (contentOffset.value + dragAmount)
                                    .coerceIn(-rightMenuWidth, leftMenuWidth)
                                    .let { newOffset ->
                                        position.value = RevealPosition.MOVING
                                        contentOffset.snapTo(newOffset)
                                    }
                            }
                        },
                        onDragEnd = {
                            //when shifted right to more than half of the left menu width
                            //shift right to fully expand left menu
                            if (contentOffset.value > leftMenuWidth / 2f) {
                                scope.launch {
                                    position.value = RevealPosition.RIGHT
                                    onFullyExpanded()
                                }
                            }
                            //when shifted left to more than half of the right menu width
                            //shift left to fully expand right menu
                            else if (contentOffset.value < -rightMenuWidth / 2f) {
                                scope.launch {
                                    position.value = RevealPosition.LEFT
                                    onFullyExpanded()
                                }
                            }
                            //if shifted only a little bit, shift back to center
                            else {
                                scope.launch {
                                    position.value = RevealPosition.COLLAPSED
                                    onFullyCollapsed()
                                }
                            }
                        }
                    )
                }
        ) {
            content()
        }
    }
}
