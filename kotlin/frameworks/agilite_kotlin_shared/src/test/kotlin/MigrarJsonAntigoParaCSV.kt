import info.agilite.core.json.JsonUtils
import java.io.StringWriter

val headers = listOf(
  "etc", "nome", "descricao", "req", "tipo", "tamanho", "versao", "options", "uniqueKeys",
  "exibirNaLista", "exibirAutocomplete", "fk", "cascade", "default", "validacoes", "observacao"
)

fun getFieldValue(field: String, item: Map<String, Any?>): String {
  var result = when (field) {
    "req", "exibirNaLista", "exibirAutocomplete" -> {
      val value = item[field]
      if (value is Boolean) if (value) "S" else "N" else ""
    }
    "options" -> {
      val options = item[field] as? List<Map<String, String>>
      options?.joinToString(";") { "${it["value"]}-${it["label"]}" } ?: ""
    }
    else -> item[field]?.toString() ?: ""
  }

  if(result.contains(",")) {
    result = "\"$result\""
  }
  return result
}

fun main() {
  // StringWriter para armazenar o CSV
  val outputFull = StringWriter()

  // Adiciona cabeçalhos ao CSV
  outputFull.append(headers.joinToString(",")).append("\n")

  val tabelas = JsonUtils.fromJson(json, object : com.fasterxml.jackson.core.type.TypeReference<List<Tabela>>() {})

  // Adiciona linhas ao CSV
  for (tabela in tabelas) {
    outputFull.append(tabela.nome).append(",,${tabela.descricao},,,,2,\n")

    for (coluna in tabela.colunas!!) {
      val row = headers.map { header -> getFieldValue(header, coluna) }
      outputFull.append(row.joinToString(",")).append("\n")
    }
  }

  // Converte o conteúdo do StringWriter para String
  val csvContentFull = outputFull.toString()

  // Imprime o conteúdo do CSV
  println(csvContentFull)
}


class Tabela {
  var colunas: List<Map<String, Any>>? = null
  var nome: String? = null
  var descricao: String? = ""
}

val json = """
  [
    {
        "colunas": [
            {
                "versao": 2,
                "nome": "nome",
                "descricao": "Nome",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 50.0,
                "req": true,
                "uniqueKeys": "uk"
            }
        ],
        "versao": 2,
        "nome": "Cas29",
        "descricao": "Credencial",
        "sistema": "CAS",
        "tarefa": "CAS1029"
    },
    {
        "colunas": [
            {
                "versao": 2,
                "nome": "autenticacao",
                "descricao": "Autenticação (Rot10id)",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "req": true
            },
            {
                "versao": 2,
                "nome": "nome",
                "descricao": "Nome",
                "tipo": "str",
                "tamanho": 30.0,
                "req": true,
                "exibirNaLista": false,
                "exibirAutocomplete": false
            },
            {
                "versao": 2,
                "nome": "empAtiva",
                "descricao": "Empresa ativa",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cas65",
                "fk_label": "Cas65-Empresa",
                "req": true,
                "cascade": "N"
            },
            {
                "versao": 2,
                "nome": "credencial",
                "descricao": "Credencial",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cas29",
                "fk_label": "Cas29-Credenciais",
                "cascade": "N",
                "req": true
            },
            {
                "versao": 2,
                "nome": "interno",
                "descricao": "Usuário interno do sistema, não é exibido para os clientes",
                "tipo": "bol",
                "tipo_label": "Boolean",
                "tamanho": 1.0
            }
        ],
        "versao": 2,
        "nome": "Cas30",
        "descricao": "Usuário",
        "sistema": "CAS",
        "tarefa": "CAS1030"
    },
    {
        "colunas": [
            {
                "versao": 2,
                "nome": "cnpj",
                "descricao": "CNPJ",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 14.0,
                "req": true,
                "uniqueKeys": "uk",
                "exibirNaLista": true,
                "exibirAutocomplete": true,
                "fk": null,
                "cascade": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "nome",
                "descricao": "Nome",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 100.0,
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": true,
                "fk": null,
                "cascade": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "validacoes": "email",
                "versao": 2,
                "nome": "email",
                "descricao": "Email",
                "tipo": "str",
                "tamanho": 100.0,
                "req": false,
                "exibirNaLista": false,
                "exibirAutocomplete": false,
                "fk": null,
                "cascade": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "im",
                "descricao": "Inscrição Municipal",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 18.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 5,
                "nome": "municipioIbge",
                "descricao": "Código IBGE do Municipio",
                "tipo": "str",
                "tamanho": 7.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "validacoes": null,
                "observacao": "com90grupo = 02",
                "options": null,
                "versao": 5,
                "nome": "cnae",
                "descricao": "Código CNAE",
                "tipo": "str",
                "tamanho": 10.0,
                "fk": null,
                "cascade": null,
                "req": false,
                "exibirNaLista": false,
                "exibirAutocomplete": false,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            }
        ],
        "versao": 2,
        "nome": "Cas65",
        "descricao": "Empresa",
        "sistema": "CAS",
        "tarefa": "CAS1065"
    },
    {
        "colunas": [
            {
                "validacoes": null,
                "options": null,
                "versao": 2,
                "nome": "codigo",
                "descricao": "Código",
                "tipo": "str",
                "tamanho": 100.0,
                "fk": null,
                "cascade": null,
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": true,
                "uniqueKeys": "uk"
            },
            {
                "validacoes": null,
                "options": null,
                "versao": 2,
                "nome": "descr",
                "descricao": "Descrição do campo",
                "tipo": "str",
                "tamanho": 0.0,
                "fk": null,
                "cascade": null,
                "req": false,
                "exibirNaLista": true,
                "exibirAutocomplete": true,
                "uniqueKeys": null,
                "tipo_label": "String"
            },
            {
                "validacoes": null,
                "observacao": null,
                "options": [
                    {
                        "value": "1",
                        "label": "Códigos de serviço"
                    },
                    {
                        "value": "2",
                        "label": "CNAE"
                    }
                ],
                "versao": 2,
                "nome": "grupo",
                "descricao": "Grupo",
                "tipo": "int",
                "tamanho": 2.0,
                "fk": null,
                "cascade": null,
                "req": true,
                "exibirNaLista": false,
                "exibirAutocomplete": false,
                "uniqueKeys": "uk"
            }
        ],
        "versao": 2,
        "nome": "Cas90",
        "descricao": "Repositório de dados",
        "sistema": "CAS",
        "tarefa": null
    },
    {
        "colunas": [
            {
                "versao": 3,
                "nome": "empresa",
                "descricao": "Empresa",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cas65",
                "fk_label": "Cas65-Empresa",
                "req": false,
                "cascade": "N",
                "uniqueKeys": "uk"
            },
            {
                "versao": 3,
                "nome": "nome",
                "descricao": "Nome",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 30.0,
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": true,
                "uniqueKeys": "uk"
            },
            {
                "versao": 3,
                "options": [
                    {
                        "value": "0",
                        "label": "Orçamento"
                    },
                    {
                        "value": "1",
                        "label": "Pedido"
                    },
                    {
                        "value": "2",
                        "label": "Nota Fiscal Produto"
                    },
                    {
                        "value": "3",
                        "label": "Nota Fiscal Serviço"
                    }
                ],
                "nome": "tipo",
                "descricao": "Tipo de Operação",
                "tipo": "int",
                "tamanho": 1.0,
                "req": true,
                "uniqueKeys": null
            },
            {
                "options": [
                    {
                        "value": "0",
                        "label": "Entrada"
                    },
                    {
                        "value": "1",
                        "label": "Saída"
                    }
                ],
                "versao": 3,
                "nome": "es",
                "descricao": "Entrada/Saida",
                "tipo": "int",
                "tamanho": 1.0,
                "req": true
            },
            {
                "versao": 3,
                "nome": "serie",
                "descricao": "Série",
                "tipo": "int",
                "tipo_label": "Integer",
                "tamanho": 3.0
            },
            {
                "versao": 3,
                "nome": "emitirDoc",
                "descricao": "Emitir documento fiscal",
                "tipo": "bol",
                "tamanho": 1.0,
                "req": true,
                "uniqueKeys": null
            },
            {
                "versao": 3,
                "nome": "alqIss",
                "descricao": "Aliquota ISS",
                "tipo": "dec",
                "tipo_label": "Decimal",
                "tamanho": 4.2,
                "req": false
            }
        ],
        "versao": 3,
        "nome": "Cgs18",
        "descricao": "Natureza da Operação",
        "sistema": "CGS",
        "tarefa": null
    },
    {
        "colunas": [
            {
                "versao": 3,
                "nome": "empresa",
                "descricao": "Empresa",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cas65",
                "fk_label": "Cas65-Empresa",
                "req": false,
                "cascade": "N",
                "uniqueKeys": "uk",
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "nome",
                "descricao": "Nome",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 30.0,
                "req": true,
                "uniqueKeys": "uk",
                "exibirAutocomplete": true,
                "exibirNaLista": true,
                "fk": null,
                "cascade": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "options": [
                    {
                        "value": "0",
                        "label": "Recebimento"
                    },
                    {
                        "value": "1",
                        "label": "Pagamento"
                    }
                ],
                "versao": 3,
                "nome": "tipo",
                "descricao": "Tipo",
                "tipo": "int",
                "tamanho": 1.0,
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": false,
                "uniqueKeys": null,
                "fk": null,
                "cascade": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "validacoes": null,
                "observacao": null,
                "options": [
                    {
                        "value": "0",
                        "label": "Não gerar"
                    },
                    {
                        "value": "1",
                        "label": "Gerar em aberto"
                    },
                    {
                        "value": "2",
                        "label": "Gerar quitado"
                    }
                ],
                "versao": 3,
                "nome": "gerar",
                "descricao": "Geração do documento financeiro",
                "tipo": "int",
                "tamanho": 1.0,
                "fk": null,
                "cascade": null,
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": false,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "validacoes": null,
                "observacao": null,
                "options": [
                    {
                        "value": "0",
                        "label": "Dinheiro"
                    },
                    {
                        "value": "1",
                        "label": "Boleto"
                    }
                ],
                "versao": 3,
                "nome": "forma",
                "descricao": "Forma",
                "tipo": "int",
                "tamanho": 2.0,
                "fk": null,
                "cascade": "",
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": false,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "recBcoBoleto",
                "descricao": "Rec-Banco para emissão do boleto",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cgs45",
                "fk_label": "Cgs45-Contas de bancos",
                "req": false,
                "cascade": "N",
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "validCount": null,
                "optionCount": null,
                "uniqueKeys": null
            },
            {
                "versao": 4,
                "nome": "contaCorrente",
                "descricao": "Conta corrente para o lançamento financeiro",
                "tipo": "lng",
                "tamanho": 10.0,
                "fk": "Scf10",
                "cascade": "N",
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            }
        ],
        "versao": 3,
        "nome": "Cgs38",
        "descricao": "Forma de Pagamento/Recebimento",
        "sistema": "CGS",
        "tarefa": null
    },
    {
        "colunas": [
            {
                "versao": 2,
                "nome": "empresa",
                "descricao": "Empresa",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cas65",
                "fk_label": "Cas65-Empresa",
                "req": false,
                "cascade": "N",
                "uniqueKeys": "uk"
            },
            {
                "versao": 2,
                "nome": "descr",
                "descricao": "Descrição",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 30.0,
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": true,
                "uniqueKeys": "uk"
            },
            {
                "validacoes": null,
                "observacao": null,
                "options": [
                    {
                        "value": "0",
                        "label": "Receita"
                    },
                    {
                        "value": "1",
                        "label": "Despesas"
                    }
                ],
                "versao": 2,
                "nome": "tipo",
                "descricao": "Tipo",
                "tipo": "int",
                "tamanho": 1.0,
                "fk": null,
                "cascade": null,
                "req": true,
                "exibirNaLista": false,
                "exibirAutocomplete": false,
                "uniqueKeys": null
            },
            {
                "versao": 2,
                "nome": "sup",
                "descricao": "Categoria Superior",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cgs40",
                "fk_label": "Cgs40-Categorias financeiras",
                "cascade": "U"
            },
            {
                "versao": 2,
                "nome": "grupo",
                "descricao": "É grupo?",
                "tipo": "bol",
                "tipo_label": "Boolean",
                "tamanho": 1.0,
                "req": true
            }
        ],
        "versao": 2,
        "nome": "Cgs40",
        "descricao": "Categorias financeiras",
        "sistema": "CGS",
        "tarefa": "CGS1040"
    },
    {
        "colunas": [
            {
                "versao": 2,
                "nome": "empresa",
                "descricao": "Empresa",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cas65",
                "fk_label": "Cas65-Empresa",
                "req": false,
                "cascade": "N",
                "uniqueKeys": "uk"
            },
            {
                "versao": 2,
                "nome": "nome",
                "descricao": "Descrição",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 30.0,
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": true,
                "uniqueKeys": "uk"
            }
        ],
        "versao": 2,
        "nome": "Cgs45",
        "descricao": "Contas de bancos",
        "sistema": "CGS",
        "tarefa": null
    },
    {
        "colunas": [
            {
                "versao": 2,
                "nome": "empresa",
                "descricao": "Empresa",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cas65",
                "fk_label": "Cas65-Empresa",
                "req": false,
                "cascade": "N",
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "mps",
                "observacao": "Campo interno para controle de MPS",
                "descricao": "MPS(0,1,2)",
                "tipo": "int",
                "tipo_label": "Integer",
                "tamanho": 1.0,
                "req": true,
                "cascade": "N",
                "fk": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "descr",
                "descricao": "Descrição completa de serviços",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 0.0,
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": true,
                "fk": null,
                "cascade": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "codigo",
                "descricao": "Código",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 15.0,
                "exibirNaLista": true,
                "exibirAutocomplete": true,
                "fk": null,
                "cascade": null,
                "req": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "preco",
                "descricao": "Preço",
                "tipo": "dec",
                "tipo_label": "BigDecimal",
                "tamanho": 16.2,
                "exibirNaLista": true,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "unidade",
                "descricao": "Unidade",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 30.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "descrComp",
                "descricao": "Descrição complementar",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 0.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "validacoes": null,
                "observacao": null,
                "options": null,
                "versao": 2,
                "nome": "obs",
                "descricao": "Observações",
                "tipo": "str",
                "tamanho": 0.0,
                "fk": null,
                "cascade": null,
                "req": false,
                "exibirNaLista": false,
                "exibirAutocomplete": false,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "validacoes": null,
                "observacao": "Apenas serviço(mps=2) - com90grupo = 01",
                "options": null,
                "versao": 2,
                "nome": "tipoServico",
                "descricao": "Código do serviço conforme tabela de serviços",
                "tipo": "str",
                "tamanho": 10.0,
                "fk": null,
                "cascade": null,
                "req": false,
                "exibirNaLista": false,
                "exibirAutocomplete": false,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            }
        ],
        "versao": 2,
        "nome": "Cgs50",
        "descricao": "Itens e Serviços",
        "sistema": "CGS",
        "tarefa": "CGS1050"
    },
    {
        "colunas": [
            {
                "versao": 2,
                "nome": "empresa",
                "descricao": "Empresa",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cas65",
                "fk_label": "Cas65-Empresa",
                "req": false,
                "cascade": "N",
                "uniqueKeys": "uk, uk1",
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "nome",
                "descricao": "Nome ou Razão Social",
                "tipo": "str",
                "tamanho": 250.0,
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": true,
                "fk": null,
                "cascade": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "fantasia",
                "descricao": "Nome Fantasia",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 100.0,
                "exibirNaLista": false,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "codigo",
                "descricao": "Código",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 15.0,
                "exibirNaLista": true,
                "uniqueKeys": "uk",
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirAutocomplete": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "options": [
                    {
                        "value": "0",
                        "label": "Pessoa Física"
                    },
                    {
                        "value": "1",
                        "label": "Pessoa Juridica"
                    },
                    {
                        "value": "2",
                        "label": "Estrangeiro"
                    }
                ],
                "versao": 2,
                "nome": "tipo",
                "descricao": "Tipo de contato",
                "tipo": "int",
                "tamanho": 1.0,
                "req": false,
                "exibirNaLista": false,
                "exibirAutocomplete": false,
                "fk": null,
                "cascade": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "ni",
                "descricao": "CPF/CNPJ",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 20.0,
                "exibirNaLista": true,
                "exibirAutocomplete": true,
                "uniqueKeys": "uk1",
                "fk": null,
                "cascade": null,
                "req": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "options": [
                    {
                        "value": "1",
                        "label": "Contribuinte ICMS"
                    },
                    {
                        "value": "2",
                        "label": "Isento de ICMS"
                    },
                    {
                        "value": "9",
                        "label": "Não contribuinte"
                    }
                ],
                "versao": 2,
                "nome": "contribuinte",
                "descricao": "Código contribuinte",
                "tipo": "int",
                "tamanho": 1.0,
                "req": false,
                "exibirNaLista": false,
                "exibirAutocomplete": false,
                "fk": null,
                "cascade": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "ie",
                "descricao": "Inscrição estadual",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 30.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "im",
                "descricao": "Inscrição municipal",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 30.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "cep",
                "descricao": "CEP",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 8.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "endereco",
                "descricao": "Endereço",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 200.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "bairro",
                "descricao": "Bairro",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 50.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "numero",
                "descricao": "Número",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 50.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "complemento",
                "descricao": "Complemento",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 50.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "uf",
                "descricao": "UF",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 2.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "municipio",
                "descricao": "Município",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 50.0,
                "exibirNaLista": true,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 5,
                "nome": "municipioIbge",
                "descricao": "Código IBGE do município",
                "tipo": "str",
                "tamanho": 10.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "telefone",
                "descricao": "Telefone",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 20.0,
                "exibirNaLista": true,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "telefoneAux",
                "descricao": "Telefone Adicional",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 20.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "celular",
                "descricao": "Celular",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 20.0,
                "exibirNaLista": true,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "validacoes": "email,min:6",
                "options": null,
                "versao": 2,
                "nome": "email",
                "descricao": "E-Mail",
                "tipo": "str",
                "tamanho": 200.0,
                "fk": null,
                "cascade": null,
                "req": false,
                "exibirNaLista": true,
                "exibirAutocomplete": false,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "obs",
                "descricao": "Observações",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 0.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "cliente",
                "descricao": "Cliente",
                "tipo": "bol",
                "tipo_label": "Boolean",
                "tamanho": 1.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "fornecedor",
                "descricao": "Fornecedor",
                "tipo": "bol",
                "tipo_label": "Boolean",
                "tamanho": 1.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 2,
                "nome": "transportadora",
                "descricao": "Transportadora",
                "tipo": "bol",
                "tipo_label": "Boolean",
                "tamanho": 1.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            }
        ],
        "versao": 2,
        "nome": "Cgs80",
        "descricao": "Entidade",
        "sistema": "CGS",
        "tarefa": "CGS1080"
    },
    {
        "versao": 5,
        "colunas": [
            {
                "versao": 5,
                "nome": "dtEmiss",
                "descricao": "Data de emissão",
                "tipo": "dat",
                "tamanho": 10.0,
                "req": true,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 5,
                "nome": "hrEmiss",
                "descricao": "Hora de emissão",
                "tipo": "tim",
                "tamanho": 6.0,
                "req": true,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "validacoes": null,
                "observacao": null,
                "options": [
                    {
                        "value": "1",
                        "label": "NFSe"
                    }
                ],
                "versao": 5,
                "nome": "sistema",
                "descricao": "Sistema",
                "tipo": "int",
                "tamanho": 2.0,
                "fk": null,
                "cascade": null,
                "req": true,
                "exibirNaLista": false,
                "exibirAutocomplete": false,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "validacoes": null,
                "observacao": null,
                "options": [
                    {
                        "value": "1",
                        "label": "Documento"
                    },
                    {
                        "value": "2",
                        "label": "Cancelamento"
                    },
                    {
                        "value": "3",
                        "label": "Carta de correção"
                    },
                    {
                        "value": "4",
                        "label": "Inutilização"
                    },
                    {
                        "value": "5",
                        "label": "Evento"
                    }
                ],
                "versao": 5,
                "nome": "tipoDoc",
                "descricao": "Tipo de documento",
                "tipo": "int",
                "tamanho": 2.0,
                "fk": null,
                "cascade": null,
                "req": true,
                "exibirNaLista": false,
                "exibirAutocomplete": false,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 5,
                "nome": "documento",
                "descricao": "Documento armazenado",
                "tipo": "str",
                "tamanho": 0.0,
                "req": false,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "validacoes": null,
                "observacao": null,
                "options": [
                    {
                        "value": "0",
                        "label": "Aguardando proc assíncrono"
                    },
                    {
                        "value": "10",
                        "label": "Falha"
                    },
                    {
                        "value": "20",
                        "label": "Erro"
                    },
                    {
                        "value": "30",
                        "label": "Rejeitado"
                    },
                    {
                        "value": "100",
                        "label": "Aprovado"
                    }
                ],
                "versao": 5,
                "nome": "statusProc",
                "descricao": "Status do processamento",
                "tipo": "int",
                "tamanho": 3.0,
                "fk": null,
                "cascade": null,
                "req": true,
                "exibirNaLista": false,
                "exibirAutocomplete": false,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 5,
                "nome": "protocolo",
                "descricao": "Número protocolo",
                "tipo": "str",
                "tamanho": 40.0,
                "fk": null,
                "cascade": null,
                "req": true,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 5,
                "nome": "cStat",
                "descricao": "Código de retorno",
                "tipo": "str",
                "tamanho": 20.0,
                "fk": null,
                "cascade": null,
                "req": true,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 5,
                "nome": "xMotivo",
                "descricao": "Descrição do retorno",
                "tipo": "str",
                "tamanho": 0.0,
                "fk": null,
                "cascade": null,
                "req": true,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 5,
                "nome": "uidTrack",
                "descricao": "UID do Tracker armazenado externamente",
                "tipo": "str",
                "tamanho": 100.0,
                "req": true,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 5,
                "nome": "regOrigem",
                "descricao": "Origem do registro",
                "tipo": "jsn",
                "tamanho": 0.0,
                "req": true,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            }
        ],
        "nome": "Gdf10",
        "descricao": "Transmissões efetuadas",
        "sistema": "GDF",
        "tarefa": null
    },
    {
        "colunas": [
            {
                "versao": 1,
                "nome": "numero",
                "descricao": "Número do contrato",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 30.0,
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": false,
                "uniqueKeys": "uk"
            },
            {
                "versao": 1,
                "nome": "cnpj",
                "descricao": "CNPJ do cliente",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 14.0,
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": true,
                "uniqueKeys": "uk1"
            },
            {
                "versao": 1,
                "nome": "rs",
                "descricao": "Razão social",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 100.0,
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": true
            },
            {
                "versao": 1,
                "nome": "tenant",
                "descricao": "Tenant Name",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 10.0,
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": true,
                "uniqueKeys": "uk2"
            }
        ],
        "versao": 1,
        "nome": "Rot01",
        "descricao": "Contrato",
        "sistema": "ROT",
        "tarefa": "ROT1001"
    },
    {
        "colunas": [
            {
                "versao": 1,
                "nome": "contrato",
                "descricao": "Contrato",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Rot01",
                "fk_label": "Rot01-Contrato",
                "req": true
            },
            {
                "validacoes": "email",
                "versao": 1,
                "nome": "email",
                "descricao": "E-Mail",
                "tipo": "str",
                "tamanho": 100.0,
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": false,
                "uniqueKeys": "uk"
            },
            {
                "versao": 1,
                "nome": "senha",
                "descricao": "Senha",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 200.0
            },
            {
                "versao": 1,
                "nome": "token",
                "descricao": "Token",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 200.0
            },
            {
                "versao": 1,
                "nome": "refreshToken",
                "descricao": "Refresh Token",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 200.0
            },
            {
                "versao": 1,
                "nome": "roles",
                "descricao": "Roles",
                "tipo": "str",
                "tamanho": 0.0,
                "req": false,
                "exibirNaLista": false,
                "exibirAutocomplete": false
            }
        ],
        "versao": 1,
        "nome": "Rot10",
        "descricao": "Autenticação",
        "sistema": "ROT",
        "tarefa": "ROT1001"
    },
    {
        "colunas": [
            {
                "versao": 3,
                "nome": "empresa",
                "descricao": "Empresa",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cas65",
                "fk_label": "Cas65-Empresa",
                "req": false,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "options": [
                    {
                        "value": "0",
                        "label": "Receber"
                    },
                    {
                        "value": "1",
                        "label": "Pagar"
                    }
                ],
                "versao": 3,
                "nome": "tipo",
                "descricao": "Tipo",
                "tipo": "int",
                "tamanho": 1.0,
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": false,
                "fk": null,
                "cascade": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "forma",
                "descricao": "Forma de Pagamento/Recebimento",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cgs38",
                "fk_label": "Cgs38-Forma de Pagamento/Recebimento",
                "req": true,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "entidade",
                "descricao": "Entidade",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cgs80",
                "fk_label": "Cgs80-Entidade",
                "req": true,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 4,
                "nome": "nossoNum",
                "descricao": "Nosso Número",
                "tipo": "lng",
                "tamanho": 12.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 4,
                "nome": "nossoNumDV",
                "descricao": "DV do Nosso Número",
                "tipo": "int",
                "tamanho": 1.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "dtEmiss",
                "descricao": "Data de emissão",
                "tipo": "dat",
                "tipo_label": "Date",
                "tamanho": 10.0,
                "req": true,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "dtVenc",
                "descricao": "Data de vencimento",
                "tipo": "dat",
                "tipo_label": "Date",
                "tamanho": 10.0,
                "req": true,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "dtPagto",
                "descricao": "Data da baixa",
                "tipo": "dat",
                "tipo_label": "Date",
                "tamanho": 10.0,
                "req": false,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 4,
                "nome": "hist",
                "descricao": "Histórico",
                "tipo": "str",
                "tamanho": 0.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "valor",
                "descricao": "Valor",
                "tipo": "dec",
                "tipo_label": "Decimal",
                "tamanho": 16.2,
                "req": true,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "multa",
                "descricao": "Valor da Multa",
                "tipo": "dec",
                "tipo_label": "Decimal",
                "tamanho": 16.2,
                "req": false,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "juros",
                "descricao": "Valor de Juros",
                "tipo": "dec",
                "tipo_label": "Decimal",
                "tamanho": 16.2,
                "req": false,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "encargos",
                "descricao": "Valor de Encargos",
                "tipo": "dec",
                "tipo_label": "Decimal",
                "tamanho": 16.2,
                "req": false,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "desconto",
                "descricao": "Valor de Desconto",
                "tipo": "dec",
                "tipo_label": "Decimal",
                "tamanho": 16.2,
                "req": false,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 4,
                "nome": "regOrigem",
                "descricao": "Registro de Origem",
                "tipo": "jsn",
                "tamanho": 0.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 4,
                "nome": "lancamento",
                "descricao": "Lancamento de quitação",
                "tipo": "lng",
                "tamanho": 10.0,
                "fk": "Scf11",
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            }
        ],
        "versao": 3,
        "nome": "Scf02",
        "descricao": "Documento a pagar/receber",
        "sistema": "SCF",
        "tarefa": null
    },
    {
        "versao": 4,
        "colunas": [
            {
                "versao": 4,
                "nome": "doc",
                "descricao": "Documento",
                "tipo": "lng",
                "tamanho": 10.0,
                "fk": "Scf02",
                "req": true,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 4,
                "nome": "remNumero",
                "descricao": "Número da remessa",
                "tipo": "int",
                "tamanho": 10.0,
                "req": true,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 4,
                "nome": "banco",
                "descricao": "Conta bancária",
                "tipo": "lng",
                "tamanho": 10.0,
                "fk": "Cgs45",
                "req": true,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 4,
                "nome": "retData",
                "descricao": "Data do retorno",
                "tipo": "dat",
                "tamanho": 10.0,
                "req": true,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 4,
                "nome": "dtPgto",
                "descricao": "Data de pagamento",
                "tipo": "dat",
                "tamanho": 10.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            }
        ],
        "nome": "Scf021",
        "descricao": "Integração Bancária",
        "sistema": "SCF",
        "tarefa": null
    },
    {
        "colunas": [
            {
                "versao": 3,
                "nome": "empresa",
                "descricao": "Empresa",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cas65",
                "fk_label": "Cas65-Empresa",
                "cascade": "N",
                "req": false,
                "uniqueKeys": "uk"
            },
            {
                "versao": 3,
                "nome": "nome",
                "descricao": "Nome",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 30.0,
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": true,
                "uniqueKeys": "uk"
            }
        ],
        "versao": 3,
        "nome": "Scf10",
        "descricao": "Conta Corrente",
        "sistema": "SCF",
        "tarefa": "SCF1010"
    },
    {
        "colunas": [
            {
                "versao": 3,
                "nome": "empresa",
                "descricao": "Empresa",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cas65",
                "fk_label": "Cas65-Empresa",
                "cascade": "N",
                "req": false,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "validacoes": null,
                "observacao": null,
                "options": [
                    {
                        "value": "0",
                        "label": "Receita"
                    },
                    {
                        "value": "1",
                        "label": "Despesa"
                    }
                ],
                "versao": 3,
                "nome": "tipo",
                "descricao": "Tipo",
                "tipo": "int",
                "tamanho": 1.0,
                "fk": null,
                "cascade": null,
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": false,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "conta",
                "descricao": "Conta",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Scf10",
                "fk_label": "Scf10-Conta Corrente",
                "cascade": "N",
                "req": true,
                "exibirNaLista": true,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "data",
                "descricao": "Data",
                "tipo": "dat",
                "tipo_label": "LocalDate",
                "tamanho": 10.0,
                "req": true,
                "exibirNaLista": true,
                "fk": null,
                "cascade": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "valor",
                "descricao": "Valor",
                "tipo": "dec",
                "tipo_label": "BigDecimal",
                "tamanho": 16.2,
                "req": true,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "hist",
                "descricao": "Histórico",
                "tipo": "str",
                "tipo_label": "String",
                "tamanho": 0.0,
                "req": true,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 4,
                "nome": "regOrigem",
                "descricao": "Registro de origem",
                "tipo": "jsn",
                "tamanho": 0.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            }
        ],
        "versao": 3,
        "nome": "Scf11",
        "descricao": "Lançamentos financeiros",
        "sistema": "SCF",
        "tarefa": "SCF1011"
    },
    {
        "colunas": [
            {
                "versao": 3,
                "nome": "empresa",
                "descricao": "Empresa",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cas65",
                "fk_label": "Cas65-Empresa",
                "req": false,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "natureza",
                "descricao": "Natureza da Operação",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cgs18",
                "fk_label": "Cgs18-Parâmetros de cálculo",
                "req": true,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "validacoes": null,
                "observacao": null,
                "options": [
                    {
                        "value": "0",
                        "label": "Orçamento"
                    },
                    {
                        "value": "1",
                        "label": "Pedido"
                    },
                    {
                        "value": "2",
                        "label": "Nota Fiscal Produto"
                    },
                    {
                        "value": "3",
                        "label": "Nota Fiscal Serviço"
                    }
                ],
                "versao": 3,
                "nome": "tipo",
                "descricao": "Tipo",
                "tipo": "int",
                "tamanho": 1.0,
                "fk": null,
                "cascade": null,
                "req": true,
                "exibirNaLista": false,
                "exibirAutocomplete": false,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "options": [
                    {
                        "value": "0",
                        "label": "Entrada"
                    },
                    {
                        "value": "1",
                        "label": "Saída"
                    }
                ],
                "versao": 3,
                "nome": "es",
                "descricao": "Entrada/Saida",
                "tipo": "int",
                "tamanho": 1.0,
                "req": true,
                "uniqueKeys": null,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "emitirDoc",
                "descricao": "Emitir documento fiscal",
                "tipo": "bol",
                "tamanho": 1.0,
                "req": true,
                "uniqueKeys": null,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "numero",
                "descricao": "Número do documento",
                "tipo": "int",
                "tipo_label": "Integer",
                "tamanho": 9.0,
                "req": true,
                "exibirNaLista": true,
                "fk": null,
                "cascade": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "serie",
                "descricao": "Série",
                "tipo": "int",
                "tipo_label": "Integer",
                "tamanho": 3.0,
                "req": false,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "dtEmiss",
                "descricao": "Data de emissão",
                "tipo": "dat",
                "tipo_label": "LocalDate",
                "tamanho": 10.0,
                "req": true,
                "exibirNaLista": true,
                "fk": null,
                "cascade": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "hrEmiss",
                "descricao": "Hora de emissão",
                "tipo": "tim",
                "tipo_label": "LocalTime",
                "tamanho": 4.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "entidade",
                "descricao": "Cliente ou Fornecedor",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cgs80",
                "fk_label": "Cgs80-Entidade",
                "req": true,
                "exibirNaLista": true,
                "cascade": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "dtCanc",
                "descricao": "Data de cancelamento",
                "tipo": "dat",
                "tipo_label": "Date",
                "tamanho": 10.0,
                "req": false,
                "exibirNaLista": true,
                "fk": null,
                "cascade": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "vlrTotal",
                "descricao": "Valor total",
                "tipo": "dec",
                "tipo_label": "Decimal",
                "tamanho": 16.2,
                "req": true,
                "exibirNaLista": true,
                "fk": null,
                "cascade": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "vlrIss",
                "descricao": "Valor do ISS",
                "tipo": "dec",
                "tipo_label": "Decimal",
                "tamanho": 16.2,
                "req": false,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 5,
                "nome": "dfeAprov",
                "descricao": "Registro de DFE aprovado",
                "tipo": "lng",
                "tamanho": 10.0,
                "fk": "Gdf10",
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 5,
                "nome": "dfeCancAprov",
                "descricao": "Registro de DFE de Cancelamento Aprovado",
                "tipo": "lng",
                "tamanho": 10.0,
                "fk": "Gdf10",
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "options": [
                    {
                        "value": "0",
                        "label": "Não integrado"
                    },
                    {
                        "value": "1",
                        "label": "Integrado"
                    },
                    {
                        "value": "2",
                        "label": "Não gerou documentos"
                    }
                ],
                "default": "0",
                "versao": 4,
                "nome": "integraScf",
                "descricao": "Integração com o SCF",
                "tipo": "int",
                "tamanho": 1.0,
                "req": true,
                "uniqueKeys": null,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 5,
                "nome": "numeroNFSe",
                "descricao": "Número da NFSe",
                "tipo": "int",
                "tamanho": 9.0,
                "fk": null,
                "cascade": null,
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            }
        ],
        "versao": 3,
        "nome": "Srf01",
        "descricao": "Documentos Fiscais",
        "sistema": "SRF",
        "tarefa": null
    },
    {
        "colunas": [
            {
                "versao": 3,
                "nome": "doc",
                "descricao": "Documento",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Srf01",
                "fk_label": "Srf01-Documento Fiscal",
                "req": true
            },
            {
                "versao": 3,
                "nome": "item",
                "descricao": "Item",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cgs50",
                "fk_label": "Cgs50-Itens e Serviços",
                "req": true
            },
            {
                "versao": 3,
                "nome": "qtd",
                "descricao": "Quantidade",
                "tipo": "dec",
                "tipo_label": "Decimal",
                "tamanho": 16.6,
                "req": true
            },
            {
                "versao": 3,
                "nome": "vlrUnit",
                "descricao": "Unitário",
                "tipo": "dec",
                "tipo_label": "Decimal",
                "tamanho": 16.6,
                "req": true
            },
            {
                "versao": 3,
                "nome": "vlrTotal",
                "descricao": "Valor total",
                "tipo": "dec",
                "tipo_label": "Decimal",
                "tamanho": 16.2,
                "req": true
            },
            {
                "versao": 3,
                "nome": "alqIss",
                "descricao": "Aliquota ISS",
                "tipo": "dec",
                "tipo_label": "Decimal",
                "tamanho": 4.2,
                "req": false
            },
            {
                "versao": 3,
                "nome": "vlrIss",
                "descricao": "Valor do ISS",
                "tipo": "dec",
                "tipo_label": "Decimal",
                "tamanho": 16.2,
                "req": false
            }
        ],
        "versao": 3,
        "nome": "Srf011",
        "descricao": "Itens",
        "sistema": "SRF"
    },
    {
        "colunas": [
            {
                "versao": 3,
                "nome": "doc",
                "descricao": "Documento",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Srf01",
                "fk_label": "Srf01-Documento Fiscal",
                "req": true,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "forma",
                "descricao": "Forma de Pagamento/Recebimento",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Cgs38",
                "fk_label": "Cgs38-Forma de Pagamento/Recebimento",
                "req": true,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "parcela",
                "descricao": "Parcela",
                "tipo": "int",
                "tipo_label": "Integer",
                "tamanho": 10.0,
                "req": true,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "dtVenc",
                "descricao": "Data de vencimento",
                "tipo": "dat",
                "tipo_label": "Date",
                "tamanho": 10.0,
                "req": true,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "valor",
                "descricao": "Valor",
                "tipo": "dec",
                "tipo_label": "Decimal",
                "tamanho": 16.2,
                "req": true,
                "fk": null,
                "cascade": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            },
            {
                "versao": 3,
                "nome": "documento",
                "descricao": "Contas Pagar/receber",
                "tipo": "lng",
                "tipo_label": "Long",
                "tamanho": 10.0,
                "fk": "Scf02",
                "fk_label": "Scf02-Documento a pagar/receber",
                "cascade": "N",
                "req": null,
                "exibirNaLista": null,
                "exibirAutocomplete": null,
                "uniqueKeys": null,
                "validCount": null,
                "optionCount": null
            }
        ],
        "versao": 3,
        "nome": "Srf012",
        "descricao": "Parcelamento",
        "sistema": "SRF",
        "tarefa": null
    }
]
""".trimIndent()