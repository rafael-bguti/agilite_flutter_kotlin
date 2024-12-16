package info.agilite.scf.application

import info.agilite.cgs.adapter.infra.Cgs38Repository
import info.agilite.core.exceptions.ValidationException
import info.agilite.integradores.bancos.IntegradorBancoFactory
import info.agilite.integradores.bancos.models.*
import info.agilite.scf.adapter.infra.Scf02Repository
import info.agilite.scf.utils.toBancoConfig
import info.agilite.shared.entities.cgs.Cgs38
import info.agilite.shared.entities.scf.Scf02
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class Scf2010Service(
  private val scf02repo: Scf02Repository,
  private val cgs38repo: Cgs38Repository
) {

  fun enviarBoletosParaBanco() {
    val scf02s = scf02repo.buscarDadosBoletosParaEnviarAoBanco()
    if(scf02s.isNullOrEmpty())throw ValidationException("Nenhum boleto encontrado para ser enviado ao banco")

    val boletos = scf02s.map { scf02 ->
      scf02.toBoleto()
    }

    //TODO receber o ID do CGS38 do cliente
    val bancoConfig = cgs38repo.findById(Cgs38::class, 1L)!!.toBancoConfig()

    val integrador = IntegradorBancoFactory.getIntegradorBoletos(bancoConfig)
    integrador.enviarBoletos(boletos)
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
      valor = BigDecimal(2),
      tipo = TipoValor.PERCENTUAL,
    ),
    mora = MoraMulta(
      valor = BigDecimal(1),
      tipo = TipoValor.PERCENTUAL,
    ),
  )
}