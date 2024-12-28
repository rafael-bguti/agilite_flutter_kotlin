package gerador.cobranca.tfs4

import gerador.cobranca.*
import gerador.cobranca.daos.DAO
import info.agilite.core.extensions.format
import info.agilite.core.extensions.numbersOnly
import info.agilite.core.json.JsonUtils
import info.agilite.core.utils.DateUtils
import info.agilite.integradores.dtos.Cliente
import info.agilite.integradores.dtos.Cobranca
import info.agilite.integradores.dtos.FormaPagamento
import info.agilite.integradores.dtos.ItemCobranca
import java.time.LocalDate
import java.util.stream.Collectors.toSet

lateinit var contratoMultiNFe: Map<String, ContratoMultiNFe>
lateinit var contratosTFSam3: Set<String>

//Processadores customizados
val processadoresCustomizadosPorCliente = mapOf<String, Processador>(
  // Multitec Sistemas
  "67919092000189" to IgnorarCobranca(),
  "64856974000154" to ValorZeradoAte100DocsE1Empresa(),//Suzan Indústria e Comércio de Materiais
  "16937595000146" to ValorZeradoAte100DocsE1Empresa(),//QI Equipamentos para Automação Industrial
  "19065357000186" to ValorFixo(694.0.toBigDecimal()),//MaisVerdes Tecnologia
)


//Adicinoar as cobranças extras aqui
val servicosAdicionaisPorRevenda: MutableMap<Revenda, List<ItemCobranca>> = mutableMapOf(
  Revenda("52104228000125", "ELTech Sistemas") to listOf(
    ItemCobranca(CODIGO_ITEM_MANUTENCAO_NUVEM, "Manutenção da Nuvem AWS", 1, 500.toBigDecimal()),
  ),
)

class GeradorCobrancaTFS4 {
  fun gerar() {
    val consumoDasLicencas = TFS4Repository().localizarConsumos()
    carregarDadosIniciais(consumoDasLicencas)

    for(consumoDaLicenca in consumoDasLicencas) {
      val processador = obterProcessador(consumoDaLicenca)
      processador.processarValores(consumoDaLicenca)
    }
    removerZerados(consumoDasLicencas)

    val faturamentos = mutableListOf<FaturamentoTFS4>()
    gerarFaturamentoDosConsumosDoMultiNfe(consumoDasLicencas, faturamentos)
    gerarFaturamentoVendaDiretaCliente(consumoDasLicencas, faturamentos)
    gerarFaturamentoRevendas(consumoDasLicencas, faturamentos)

    println("Listando documentos emitidos para validar cobrança...")
    validarClientesSemPagar(faturamentos)

    cobrancasGeradas.addAll(faturamentos.map { it.cobranca })
  }

  fun validarClientesSemPagar(faturamentos: List<FaturamentoTFS4>) {
    val firstDay = mesAnoReferencia.withDayOfMonth(1)
    val lastDay = mesAnoReferencia.withDayOfMonth(mesAnoReferencia.lengthOfMonth())

    val rs = DAO.queryTFS4(
  """
      select count(*) as qtd, c.licenca_rs ||' - '||e.rs as rs, e.cnpj as cnpj
      from documentos d 
      inner join empresas e  on e.emp_id  = d.empresa_id
      inner join contratos c on c.cnt_id = e.contrato_id
      inner join revendas r on r.rvn_id = c.revenda_id
      where d.dt_hr_emissao between '${firstDay.format()}' and '${lastDay.format()}'
      group by e.cnpj, c.licenca_rs, e.rs, c.licenca_cnpj, r.cnpj
      """
    )
    val documentosEmitidos = rs.map { DocumentosEmitidosDTO(it, SistemaConsumo.TFS4) }

    documentosEmitidos.forEach {
      val cnpjEmissor = it.cnpj
      faturamentos.any { fat -> fat.cnpjEmissores.contains(cnpjEmissor)  } || documentosNaoPagos.add(it)
    }


  }

  private fun gerarFaturamentoVendaDiretaCliente(
    consumoDasLicencas: List<ConsumoTFS4Licenca>,
    result: MutableList<FaturamentoTFS4>
  ) {
    val consumoDiretoCliente = consumoDasLicencas.filter { it.cobrarDiretamenteDoCliente }
    for (licenca in consumoDiretoCliente) {
      val cnpjs = licenca.consumos.map { it.empresaCNPJ }.toSet()
      val cobranca = Cobranca(
        natureza = NATUREZA_TFS4_CLI,
        cliente = Cliente(licenca.cnpjCobrancaSAM4DiretoCliente ?: licenca.licencaCNPJ, licenca.licencaRs),
        obs = licenca.getHistorico(),
        itens = listOf(
          ItemCobranca(
            codigo = CODIGO_ITEM_TFS4_CLI,
            descricao = "Uso do TFS4 cobrado diretamente do cliente",
            quantidade = 1,
            valor = licenca.total
          )
        ),
        formasPagamento = listOf(
          FormaPagamento(
            nomeFormaPagamento = NOME_FORMA_PAGAMENTO_BOLETO,
            valor = licenca.total,
            dataVencimento = DateUtils.lastUtilDayOfMonth()
          )
        )
      )
      result.add(FaturamentoTFS4(cobranca, cnpjs))
      licenca.faturado = true
    }
  }

  private fun gerarFaturamentoRevendas(
    consumoDasLicencas: List<ConsumoTFS4Licenca>,
    result: MutableList<FaturamentoTFS4>
  ) {
    val consumoByRevendas = consumoDasLicencas.filter { !it.faturado }.groupBy { it.revenda }

    for ((revenda, consumos) in consumoByRevendas) {
      var total = consumos.sumOf { it.total }
      val historicos = consumos.joinToString("\n\n ", ) { "Licença: ${it.licencaCNPJ}-${it.licencaRs}\n${it.getHistorico()}" }

      val itens = mutableListOf(ItemCobranca(
        codigo = CODIGO_ITEM_TFS4,
        descricao = "Uso do TFS4 cobrado pelo contrato da revenda",
        quantidade = 1,
        valor = total
      ))
      if(servicosAdicionaisPorRevenda.containsKey(revenda)){
        itens.addAll(servicosAdicionaisPorRevenda[revenda]!!)
        total += servicosAdicionaisPorRevenda[revenda]!!.sumOf { it.valor }
      }


      val cobranca = Cobranca(
        natureza = NATUREZA_TFS4_REV,
        cliente = Cliente(revenda.revendaCnpj, revenda.revendaRs),
        obs = historicos,
        itens = itens,
        formasPagamento = listOf(
          FormaPagamento(
            nomeFormaPagamento = NOME_FORMA_PAGAMENTO_BOLETO,
            valor = total,
            dataVencimento = DateUtils.lastUtilDayOfMonth()
          )
        )
      )
      val cnpjs = consumos.map { lic -> lic.consumos.map { it.empresaCNPJ  } }.flatten().toSet()
      result.add(FaturamentoTFS4(cobranca, cnpjs))
      consumos.forEach { it.faturado = true }
    }
  }

  private fun gerarFaturamentoDosConsumosDoMultiNfe(
    consumoDasLicencas: List<ConsumoTFS4Licenca>,
    result: MutableList<FaturamentoTFS4>
  ) {
    for (licenca in consumoDasLicencas) {
      if (licenca.cnpjCobrancaMultinfe != null) {
        alterarValoresDoMultiNFe(licenca)?.also { result.add(it) }
      }
    }
  }

  private fun buildFaturamentoMultinfeInSAM4(licenca: ConsumoTFS4Licenca): Cobranca {
    val cobranca = Cobranca(
      natureza = NATUREZA_MNFE_IN_SAM4,
      cliente = Cliente(licenca.cnpjCobrancaMultinfe ?: licenca.licencaCNPJ, licenca.licencaRs),
      obs = licenca.getHistorico(),
      itens = listOf(
        ItemCobranca(
          codigo = CODIGO_ITEM_MULTINFE,
          descricao = "Uso do TFS4 cobrado pelo contrato do MultiNFe",
          quantidade = 1,
          valor = licenca.total
        )
      ),
      formasPagamento = listOf(
        FormaPagamento(
          nomeFormaPagamento = NOME_FORMA_PAGAMENTO_BOLETO,
          valor = licenca.total,
          dataVencimento = LocalDate.now().withDayOfMonth(20)
        )
      )
    )
    licenca.faturado = true
    return (cobranca)
  }

  private fun alterarValoresDoMultiNFe(licenca: ConsumoTFS4Licenca): FaturamentoTFS4? {
    val cobrancasSam3 = findCobrancaMNFeTFS3ByCnpjs(licenca.licencaCNPJ, licenca.cnpjCobrancaMultinfe!!)
    val cnpjs = licenca.consumos.map { it.empresaCNPJ }.toSet()
    if (cobrancasSam3.isNullOrEmpty()) {
      return FaturamentoTFS4(buildFaturamentoMultinfeInSAM4(licenca), cnpjs)
    }
    println("\n\n\n-------------------------------------------------------------------------------------")
    println("* * * * * COBRANÇA SISTEMAS ANTIGOS * * * * *")
    println(JsonUtils.toFormatedJson(cobrancasSam3))

    println("* * * * * CONTRATO MULTINFE * * * * *")
    if(licenca.contratoMultiNFe != null){
      println(JsonUtils.toFormatedJson(licenca.contratoMultiNFe!!))
    }

    println("* * * * * VALORES NOVOS * * * * *")
    println(JsonUtils.toFormatedJson(licenca))
    println(licenca.licencaCNPJ+"-"+licenca.licencaRs)

    val valorTotalSAM3 = cobrancasSam3.map { it.itens.sumOf { item -> item.valor } }.sumOf { it }
    println("Valor total SAM3: $valorTotalSAM3")
    println("Valor total SAM4: ${licenca.total}")
    //TODO imprimir aqui ultimas 6 cobranças do sistema de cobranca
    println("O que fazer? [1] - Usar só o novo, [2] - Usar só o antigo, [3] - Usar os 2, [4] - Só o novo com outro valor")
    val resposta = readlnOrNull()

    if (resposta == "1") {
      cobrancasSam3.forEach{ cobrancasGeradas.remove(it) }
      return FaturamentoTFS4(buildFaturamentoMultinfeInSAM4(licenca), cnpjs)
    } else if (resposta == "2") {
      return null
    } else if (resposta == "3") {
      return FaturamentoTFS4(buildFaturamentoMultinfeInSAM4(licenca), cnpjs)
    } else if (resposta == "4") {
      println("Digite o novo valor")
      val novoValor = readlnOrNull()?.toBigDecimal()
      if (novoValor != null) {
        cobrancasSam3.forEach{ cobrancasGeradas.remove(it) }
        licenca.total = novoValor
        return FaturamentoTFS4(buildFaturamentoMultinfeInSAM4(licenca), cnpjs)
      }else{
        throw RuntimeException("Valor inválido")
      }
    }

    throw RuntimeException("Resposta inválida")
  }

  private fun removerZerados(consumoDasLicencas: MutableList<ConsumoTFS4Licenca>) {
    for (indice in consumoDasLicencas.size - 1 downTo 0) {
      val licenca = consumoDasLicencas[indice]
      if (licenca.total == 0.toBigDecimal()) {
        println("*** ATENÇÃO *** Licença ${licenca.licencaCNPJ}-${licenca.licencaRs} com valor 0,00 - QtdTotalDocs - ${licenca.qtdTotalDocs()} - Já usou o sistema ${licenca.jaProcessouAlgumDoc()}")
        consumoDasLicencas.removeAt(indice)
      }
    }
  }

  private fun obterProcessador(consumoDaLicenca: ConsumoTFS4Licenca): Processador{
    val processadorCustom = processadoresCustomizadosPorCliente[consumoDaLicenca.licencaCNPJ]
    if(processadorCustom != null){
      return processadorCustom
    }

    return ProcessadorDefaultTFS4()
  }

  private fun findCobrancaMNFeTFS3ByCnpjs(cnpjLicencaSAM4: String, cnpjContratoMultiNFe: String): List<Cobranca>{
    return cobrancasGeradas
      .filter {
        (it.cliente.cnpj == cnpjContratoMultiNFe || it.cliente.cnpj == cnpjLicencaSAM4) &&
        (it.natureza == NATUREZA_MNFE || it.natureza == NATUREZA_TFS3)
      }

  }

  private fun carregarDadosIniciais(consumosTFS4Licenca: List<ConsumoTFS4Licenca>) {
    val cnpjsLicencas = consumosTFS4Licenca.map { it.licencaCNPJ }
    println("Buscando contratos no MultiNFe")
    contratoMultiNFe = MultiNFeRepository().localizarContratosMultiNFe(cnpjsLicencas)
    println("Buscando contratos no TFS3")
    contratosTFSam3 = TfSam3Repository().localizarContratosTFSam3(cnpjsLicencas)
    definirCnpjMultiNFe(consumosTFS4Licenca)
  }

  private fun definirCnpjMultiNFe(consumosTFS4Licenca: List<ConsumoTFS4Licenca>){
    consumosTFS4Licenca.forEach { licenca ->
      val contrato = contratoMultiNFe[licenca.licencaCNPJ]
      if(contrato != null){
        licenca.cnpjCobrancaMultinfe = contrato.cnpjCobranca
      }
    }
  }
}

data class FaturamentoTFS4 (
  val cobranca: Cobranca,
  val cnpjEmissores: Set<String>,
)