package nikmax.shopinglist.core

object Validators {
    enum class NameValidity {
        VALID,
        BLANK
    }
    
    enum class AmountValidity {
        VALID,
        BLANK,
        NOT_NUMBER,
        ZERO,
        NEGATIVE
    }
    
    enum class PriceValidity {
        VALID,
        BLANK,
        NOT_NUMBER,
        NEGATIVE
    }
    
    fun String.validateName(): NameValidity {
        return if (this.isBlank()) NameValidity.BLANK
        else NameValidity.VALID
    }
    
    fun String.validateAmount(): AmountValidity {
        return if (this.isBlank()) AmountValidity.BLANK
        else if (this.toIntOrNull() == null) AmountValidity.NOT_NUMBER
        else if (this.toInt() == 0) AmountValidity.ZERO
        else if (this.toInt() < 0) AmountValidity.NEGATIVE
        else AmountValidity.VALID
    }
    
    fun String.validatePrice(): PriceValidity {
        return if (this.isBlank()) PriceValidity.BLANK
        else if (this.toFloatOrNull() == null) PriceValidity.NOT_NUMBER
        else if (this.toFloat() < 0) PriceValidity.NEGATIVE
        else PriceValidity.VALID
    }
}
