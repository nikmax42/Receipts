package nikmax.shopinglist.core

import com.google.common.truth.Truth.assertThat
import nikmax.shopinglist.core.Validators.AmountValidity
import nikmax.shopinglist.core.Validators.NameValidity
import nikmax.shopinglist.core.Validators.validateAmount
import nikmax.shopinglist.core.Validators.validateName
import nikmax.shopinglist.core.Validators.validatePrice
import org.junit.Test

class ValidatorsTest {
    @Test
    fun nameValidator_returnBlank_ifStringIsBlank() {
        assertThat("".validateName()).isEqualTo(NameValidity.BLANK)
        assertThat("   ".validateName()).isEqualTo(NameValidity.BLANK)
    }
    
    @Test
    fun nameValidator_returnValid_ifStringIsValid() {
        assertThat("valid name".validateName()).isEqualTo(NameValidity.VALID)
    }
    
    
    @Test
    fun amountValidator_returnBlank_ifStringIsBlank() {
        assertThat("".validateAmount()).isEqualTo(AmountValidity.BLANK)
        assertThat("   ".validateAmount()).isEqualTo(AmountValidity.BLANK)
    }
    
    @Test
    fun amountValidator_returnNotNumber_ifStringIsNotInt() {
        assertThat("13ba1".validateAmount()).isEqualTo(AmountValidity.NOT_NUMBER)
    }
    
    @Test
    fun amountValidator_returnZero_ifStringIsZeroInt() {
        assertThat("0".validateAmount()).isEqualTo(AmountValidity.ZERO)
    }
    
    @Test
    fun amountValidator_returnNegative_ifStringIsNegativeInt() {
        assertThat("-1".validateAmount()).isEqualTo(AmountValidity.NEGATIVE)
    }
    
    @Test
    fun amountValidator_returnValid_ifStringIsValidInt() {
        assertThat("12".validateAmount()).isEqualTo(AmountValidity.VALID)
    }
    
    
    @Test
    fun priceValidator_returnBlank_ifStringIsBlank() {
        assertThat("".validatePrice()).isEqualTo(Validators.PriceValidity.BLANK)
        assertThat("   ".validatePrice()).isEqualTo(Validators.PriceValidity.BLANK)
    }
    
    @Test
    fun priceValidator_returnNotNumber_ifStringIsNotNumber() {
        assertThat("1.3abc".validatePrice()).isEqualTo(Validators.PriceValidity.NOT_NUMBER)
    }
    
    @Test
    fun priceValidator_returnNegative_ifStringIsNegativeFloat() {
        assertThat("-1".validatePrice()).isEqualTo(Validators.PriceValidity.NEGATIVE)
    }
    
    @Test
    fun priceValidator_returnValid_ifStringIsValidFloat() {
        assertThat("-1".validatePrice()).isEqualTo(Validators.PriceValidity.NEGATIVE)
    }
    
}
