package info.agilite.shared.extensions

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.roud(scale: Int = 2): BigDecimal {
    return this.setScale(scale, RoundingMode.HALF_UP)
}

fun BigDecimal.trunc(scale: Int = 2): BigDecimal {
    return this.setScale(scale, RoundingMode.DOWN)
}