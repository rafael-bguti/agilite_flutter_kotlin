package info.agilite.srf.application

import info.agilite.core.exceptions.ValidationException
import info.agilite.core.extensions.parseDate
import info.agilite.core.utils.CsvUtils
import info.agilite.srf.domain.SRF2030Doc
import info.agilite.srf.domain.SRF2030DocFormaRecebimento
import info.agilite.srf.domain.SRF2030DocItem

object Srf2030CsvParser {
  /***
   * Colunas do CSV linha documento: Tipo(2, 3),Nome da Natureza,Ni Entidade,Observações
   * Colunas do CSV linha Item: Tipo (10),Código item,Descrição,Quantidade,Unitário
   * Colunas do CSV linha Forma recebimento: Tipo (30),Código forma de recebimento,Data Vencimento,valor

   * onde Tipo: 2-Nota de Produto, 3-Nota de Serviço, 10-Item, 30-Forma de recebimento
   */
  fun parseFromCsv(content: String):List<SRF2030Doc> {
    val matrix = CsvUtils.parse(content)

    val docs = mutableListOf<SRF2030Doc>()

    for(i in matrix.indices) {
      val line = matrix[i]

      val tipo = line[0].toInt()
      when(tipo) {
        2, 3 -> {
          if(line.size < 3) {
            throw ValidationException("Linha: $i Linha inválida, quantidade de colunas deve ser 3 ou 4. ($line)")
          }
          docs.add(SRF2030Doc(i, tipo, line[1], line[2], line.getOrNull(3)))
        }
        10 -> {
          if(line.size != 5) {
            throw ValidationException("Linha: $i Linha inválida, quantidade de colunas deve ser 5. ($line)")
          }
          if(docs.isEmpty()){
            throw ValidationException("Item da linha $i sem documento.")
          }

          docs.last().itens.add(SRF2030DocItem(line[1], line[2], line[3].toInt(), line[4].toBigDecimal()))
        }
        30 -> {
          if(line.size != 4) {
            throw ValidationException("Linha: $i Linha inválida, quantidade de colunas deve ser 4. ($line)")
          }
          if(docs.isEmpty()){
            throw ValidationException("Forma de recebimento da linha $i sem documento.")
          }
          docs.last().formasRecebimento.add(SRF2030DocFormaRecebimento(line[1], line[2].parseDate(), line[3].toBigDecimal()))
        }
        else -> {
          throw ValidationException("Linha: $i. Tipo de linha inválido.")
        }
      }
    }

    return docs
  }
}