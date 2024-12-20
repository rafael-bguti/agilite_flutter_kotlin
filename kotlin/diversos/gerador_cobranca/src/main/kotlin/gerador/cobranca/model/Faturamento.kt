package gerador.cobranca.model

import info.agilite.integradores.dtos.Cobranca

class Faturamento(
  val cobranca: Cobranca,

  val empresas: List<Empresa> = emptyList(), //Inclur as empresas que estão sendo cobradas (empresa que usou o sistema) para depois fazer a validação entre documentos emitidos e CNPJ cobrados
)

class Empresa(
  val cnpj: String,
  val rs: String,
)