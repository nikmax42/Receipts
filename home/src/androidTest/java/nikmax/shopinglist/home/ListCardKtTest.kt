package nikmax.shopinglist.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import com.google.common.truth.Truth.assertThat
import nikmax.shopinglist.core.Currency
import nikmax.shopinglist.core.ui.ListUi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ListCardKtTest {
    @get:Rule
    val rule = createComposeRule()
    
    private val testList = ListUi(
        name = "fake",
        creationDate = 0,
        id = 0,
        items = 0,
        completedItems = 0,
        totalPrice = 0f,
        completedPrice = 0f,
        currency = Currency.USD
    )
    
    @Test
    fun callBackIsCalled_when_clicked() {
        var isCalled = false
        rule.setContent {
            ListCard(
                list = testList,
                onClick = { isCalled = true },
                onLongClick = {},
            )
        }
        rule.onNodeWithText(testList.name).performClick()
        assertThat(isCalled).isTrue()
    }
    
    @Test
    fun callBackIsCalled_when_longClicked() {
        var isCalled = false
        rule.setContent {
            ListCard(
                list = testList,
                onClick = { },
                onLongClick = { isCalled = true },
            )
        }
        rule.onNodeWithText(testList.name).performTouchInput { longClick() }
        assertThat(isCalled).isTrue()
    }
    
    @Test
    fun isSelectionDesigned_when_selected() {
        var selectionColor: Color = Color.Unspecified
        rule.setContent {
            selectionColor = ListCardTokens.selectedColors().containerColor
            ListCard(
                list = testList.copy(
                    isSelected = true
                ),
                onClick = {},
                onLongClick = {},
            )
        }
        rule.onNodeWithText(testList.name).assertBackgroundColor(selectionColor)
    }
    
    @Test
    fun isCompleteDesigned_when_complete() {
        var completeColor: Color = Color.Unspecified
        rule.setContent {
            completeColor = ListCardTokens.completedColors().containerColor
            ListCard(
                list = testList.copy(
                    items = 1,
                    completedItems = 1
                ),
                onClick = {},
                onLongClick = {},
            )
        }
        rule.onNodeWithText(testList.name).assertBackgroundColor(completeColor)
    }
    
    @Test
    fun isBasicDesigned_when_norSelectedNorComplete() {
        var basicColor: Color = Color.Unspecified
        rule.setContent {
            basicColor = ListCardTokens.normalColors().containerColor
            ListCard(
                list = testList.copy(
                    items = 1,
                    completedItems = 0
                ),
                onClick = {},
                onLongClick = {},
            )
        }
        rule.onNodeWithText(testList.name).assertBackgroundColor(basicColor)
    }
    
    
    private fun SemanticsNodeInteraction.assertBackgroundColor(expectedBackground: Color) {
        val array = IntArray(20)
        captureToImage()
            .readPixels(
                array,
                startY = 10,
                startX = 10,
                width = 5,
                height = 4
            )
        array.forEach {
            assertEquals(
                expectedBackground.convert(ColorSpaces.Srgb).hashCode(),
                it
            )
        }
    }
}
