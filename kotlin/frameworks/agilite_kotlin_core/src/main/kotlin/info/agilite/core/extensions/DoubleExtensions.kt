package info.agilite.core.extensions

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

fun Double.format(format: String = "#.##"): String {
    val df = DecimalFormat(format, DecimalFormatSymbols(Locale("pt", "BR")))
    df.roundingMode = java.math.RoundingMode.HALF_EVEN
    return df.format(this)
}