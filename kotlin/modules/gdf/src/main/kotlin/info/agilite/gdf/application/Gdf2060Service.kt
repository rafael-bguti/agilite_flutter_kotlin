package info.agilite.gdf.application

import info.agilite.boot.orm.annotations.DbTable
import info.agilite.boot.security.UserContext
import info.agilite.cas.adapter.infra.Cas65Repository
import info.agilite.core.exceptions.ValidationException
import info.agilite.core.xml.ElementXml
import info.agilite.core.xml.ElementXmlConverter
import info.agilite.shared.entities.cas.Cas65
import org.springframework.stereotype.Service

@Service
class Gdf2060Service(
  private val cas65repo: Cas65Repository,
) {

  fun processarRetornoLoteNFse(gdf20id: Long, bytes: ByteArray, contentType: String?){
    val cas65 = cas65repo.findById(UserContext.safeUser.empId)
    if(cas65.cas65municipio == null || cas65.cas65uf == null){
      throw RuntimeException("Município ou UF não configurados para a empresa logada")
    }
    val municipioEstado = "${cas65.cas65municipio}-${cas65.cas65uf}".uppercase()
    val dadosRps: List<DadosRps>
    if(municipioEstado == "ITATIBA-SP") {
      dadosRps = extrairDadosRetornoItatiba(bytes, contentType)
    }else{
      throw RuntimeException("Não é possível processar retorno de lotes de NFSe para o município de ${cas65.cas65municipio}-${cas65.cas65uf}")
    }

    if(dadosRps.isEmpty())throw ValidationException("Nenhum RPS encontrado no retorno")


  }

  fun extrairDadosRetornoItatiba(bytes: ByteArray, contentType: String?): List<DadosRps>{
    if(!contentType.equals("application/xml", true)){
      throw RuntimeException("O conteúdo do retorno deve ser um XML")
    }

    val xml = String(bytes)
    val xmlRoot = ElementXmlConverter.string2Element(xml)
    val nfses = xmlRoot.findChildNodes("Nfse") ?: return emptyList()

    val result = mutableListOf<DadosRps>()
    for(index in nfses.indices){
      val nfse = nfses[index]
      val numeroNFSe = nfse.findChildValue("InfNfse.Numero") ?: throw ValidationException("Número da NFSe não encontrado na nota $index")
      val serieRps = nfse.findChildValue("InfDeclaracaoPrestacaoServico.Rps.IdentificacaoRps.Serie") ?: throw ValidationException("Série do RPS não encontrado na nota $index")
      val numeroRps = nfse.findChildValue("InfDeclaracaoPrestacaoServico.Rps.IdentificacaoRps.Numero") ?: throw ValidationException("Número do RPS não encontrado na nota $index")

      result.add(DadosRps(numeroRps, serieRps, numeroNFSe))
    }

    return result
  }
}

data class DadosRps(
  val numeroRps: String,
  val serieRps: String,
  val numeroNFSe: String,
)
