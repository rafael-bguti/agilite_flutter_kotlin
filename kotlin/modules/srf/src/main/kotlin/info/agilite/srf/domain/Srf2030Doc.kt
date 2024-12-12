package info.agilite.srf.domain

import java.math.BigDecimal
import java.time.LocalDate

//Documento para importar
class SRF2030Doc(
  val linha: Int,
  val tipo: Int,
  val nomeNatureza: String,
  val niEntidade: String,
  val observacoes: String?,

  val itens: MutableList<SRF2030DocItem> = mutableListOf(),
  val formasRecebimento: MutableList<SRF2030DocFormaRecebimento> = mutableListOf(),
)

//Item para importar
class SRF2030DocItem(
  val codigoItem: String,
  val descrItem: String,
  val quantidade: Int,
  val unitario: BigDecimal,
)

//Forma de recebimento para importar
class SRF2030DocFormaRecebimento(
  val nomeFormaRecebimento: String,
  val dataVencimento: LocalDate,
  val valor: BigDecimal,
)

