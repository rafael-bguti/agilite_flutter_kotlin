package gerador.cobranca.multinfe
import gerador.cobranca.CODIGO_ITEM_MULTINFE
import gerador.cobranca.NATUREZA_MNFE
import gerador.cobranca.NOME_FORMA_PAGAMENTO_BOLETO
import info.agilite.integradores.bancos.dto.Cliente
import info.agilite.integradores.bancos.dto.Cobranca
import info.agilite.integradores.bancos.dto.FormaPagamento
import info.agilite.integradores.bancos.dto.ItemCobranca
import java.math.BigDecimal
import java.time.LocalDate

data class MNFeCobranca(
  val cliente: MnfeCliente,
  val valorCobrado: BigDecimal,
  val valorDaNota: BigDecimal,
  val cobrancaItems: List<MnfeCobrancaItem>,
  val sistema: MnfeSistema,
  val dtVcto: LocalDate,
  val emailCobranca: String
){
  fun toCobranca(): Cobranca {
    val result = Cobranca(
      natureza = NATUREZA_MNFE,
      cliente = cliente.toCliente(),
      itens = cobrancaItems.map {
        ItemCobranca(
          codigo = CODIGO_ITEM_MULTINFE,
          descricao = it.descr,
          quantidade = 1,
          valor = it.total
        )
      }.toMutableList(),
      formasPagamento = listOf(
        FormaPagamento(
          nomeFormaPagamento = NOME_FORMA_PAGAMENTO_BOLETO,
          dataVencimento = dtVcto,
          valor = valorCobrado
        )
      )
    )

    if(emailCobranca.isNotBlank()){
      result.cliente.email = emailCobranca
    }

    return result
  }
}
data class MnfeCliente(
  val bairro: String,
  val cep: String,
  val ie: String,
  val logradouro: String,
  val mail: String,
  val municipio: String,
  val ni: String,
  val nome: String,
  val numero: String,
  val uf: String
){
  fun toCliente(): Cliente {
    return Cliente(
      cnpj = ni,
      nome = nome,
      endereco = logradouro,
      numero = numero,
      complemento = null,
      cidade = municipio,
      estado = uf,
      cep = cep,
      email = mail
    )
  }
}


data class MnfeCobrancaItem(
  val descr: String,
  val qtd: Int,
  val total: BigDecimal,
  val unit: BigDecimal
)
data class MnfeSistema(
  val nome: String
)

