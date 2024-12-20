package gerador.cobranca.tfs4

import java.math.BigDecimal


private val faixaDePreco = mapOf<Int, BigDecimal>(
  20 to   BigDecimal("46"),
  100 to  BigDecimal("56"),
  200 to  BigDecimal("106"),
  300 to  BigDecimal("151"),
  400	to  BigDecimal("171"),
  500	to  BigDecimal("204"),
  600	to  BigDecimal("234"),
  700	to  BigDecimal("259"),
  800	to  BigDecimal("281"),
  900	to  BigDecimal("299"),
  1000 to	BigDecimal("313"),
  1500 to	BigDecimal("385"),
  2000 to	BigDecimal("399"),
  3000 to	BigDecimal("456"),
  4000 to	BigDecimal("475"),
  5000 to	BigDecimal("522"),
)
object GeradorValoresTFS4 {
  fun preco(qtd: Int): BigDecimal {
    val faixa = faixaDePreco.keys.sorted().firstOrNull { qtd <= it } ?: 5000
    return faixaDePreco[faixa] ?: BigDecimal.ZERO
  }
}