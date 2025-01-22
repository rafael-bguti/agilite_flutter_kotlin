package info.agilite.scf.application

import info.agilite.cgs.adapter.infra.Cgs38Repository
import info.agilite.core.exceptions.ValidationException
import info.agilite.integradores.bancos.IntegradorBancoFactory
import info.agilite.integradores.bancos.models.*
import info.agilite.scf.adapter.infra.Scf021Repository
import info.agilite.scf.adapter.infra.Scf2010Repository
import info.agilite.scf.utils.toBancoConfig
import info.agilite.shared.entities.scf.Scf02
import info.agilite.shared.entities.scf.Scf021
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class Scf2010Service(
  private val scf2010repo: Scf2010Repository,
  private val scf021repo: Scf021Repository,
  private val cgs38repo: Cgs38Repository
) {
  fun enviarBoletosParaBanco(scf02ids: List<Long>) {
    val boletos = scf2010repo.buscarDadosBoletosParaEnviarAoBanco(scf02ids)
    if(boletos.isEmpty())throw ValidationException("Nenhum boleto encontrado para ser enviado ao banco")

    val scf02sByCgs38 = boletos.groupBy { it.scf02forma }

    for (scf02Entry in scf02sByCgs38) {
      val bancoConfig = scf02Entry.key.toBancoConfig()
      val integrador = IntegradorBancoFactory.getIntegradorBoletos(bancoConfig)

      val scf02s = scf02Entry.value
      for(scf02 in scf02s) {
        val boleto = scf02.toBoleto()
        val retorno = integrador.enviarBoleto(boleto)

        val scf021 = Scf021(
          scf021doc = scf02.scf02id,
          scf021remNumero = retorno.codigoSolicitacao,
          scf021conta = scf02Entry.key.cgs38conta!!,
        )

        scf021repo.insertInNewTransaction(scf021)
      }
    }
  }

  private fun Scf02.toBoleto() = Boleto(
    seuNumero = scf02id.toString(),
    valorNominal = scf02valor,
    dataVencimento = scf02dtVenc,
    numDiasAgenda = 60, // TODO - Adicionar no cadastro do Banco para a geração do boleto
    pagador = Pagador(
      cpfCnpj = scf02entidade.cgs80ni ?: "", // TODO Criar um validador que valide os dados obrigatórios antes de emitir o boleto
      nome = scf02entidade.cgs80nome,
      tipoPessoa = TipoPessoa.JURIDICA,
      endereco = scf02entidade.cgs80endereco ?: "", // TODO Criar um validador que valide os dados obrigatórios antes de emitir o boleto
      numero = scf02entidade.cgs80numero ?: "", // TODO Criar um validador que valide os dados obrigatórios antes de emitir o boleto
      bairro = scf02entidade.cgs80bairro,
      cidade = scf02entidade.cgs80municipio ?: "", // TODO Criar um validador que valide os dados obrigatórios antes de emitir o boleto
      uf = scf02entidade.cgs80uf ?: "", // TODO Criar um validador que valide os dados obrigatórios antes de emitir o boleto
      cep = scf02entidade.cgs80cep ?: "", // TODO Criar um validador que valide os dados obrigatórios antes de emitir o boleto
      complemento = scf02entidade.cgs80complemento,
    ),
    multa = MoraMulta(
      taxa = BigDecimal(2),
      codigo = TipoValor.PERCENTUAL,
    ),
    mora = MoraMulta(
      taxa = BigDecimal(1),
      codigo = TipoValor.TAXAMENSAL,
    ),
  )
}