package gerador.cobranca.tfs3

import java.math.BigDecimal


object TFS3ValoresHistoricos {
  val historicos = mutableMapOf<String, TFS3ValorHistorio>()
  init {
    historicos.put("56365612000132", TFS3ValorHistorio("SINDICATO DOS TRABALHADORES NAS USINAS DE ACUCAR,NAS INDUSTRIAS DE SUCO CONCENTRADO,DO CAFE SOLUVEL,", 54.88.toBigDecimal()))
    historicos.put("46106829000174", TFS3ValorHistorio("Sindicato de Papel, Papelão e Celulose de Campinas", 54.88.toBigDecimal()))
    historicos.put("43975226000110", TFS3ValorHistorio("SINDICATO DOS TRABALHADORES NAS INDUSTRIAS DE ALIMENTACAO DE ARARAQUARA E REGIAO", 54.88.toBigDecimal()))
    historicos.put("56390123000130", TFS3ValorHistorio("CASA DE SAUDE BEZERRA DE MENEZES", 54.88.toBigDecimal()))
    historicos.put("11001107000170", TFS3ValorHistorio("ROMANATO ALIMENTOS LTDA", 88.48.toBigDecimal()))
    historicos.put("51496669000157", TFS3ValorHistorio("SINDICATO DOS TRABAL EM TRANSP RODOV DE JAU E REGIAO", 54.88.toBigDecimal()))
    historicos.put("51420057000180", TFS3ValorHistorio("SINDICATO DOS TRAB NAS INDS METLS MECS DE MAT ELETRICO", 54.88.toBigDecimal()))
    historicos.put("44739365000108", TFS3ValorHistorio("ITAMOVEL INDUSTRIA E COMERCIO DE MOVEIS LTDA", 54.88.toBigDecimal()))
    historicos.put("63991178000161", TFS3ValorHistorio("CARRANTOS SERVICOS DE VIGILANCIA LTDA", 166.88.toBigDecimal()))
    historicos.put("56389877000170", TFS3ValorHistorio("KOELLE LTDA - EDUCACAO E CULTURA", 88.48.toBigDecimal()))
    historicos.put("30301994000147", TFS3ValorHistorio("LOUVEIRA SERVICOS DE INTERMEDIACAO DE ABATE DE AVES LTDA", 88.48.toBigDecimal()))
    historicos.put("50125418000101", TFS3ValorHistorio("ASSOCIACAO DE PAIS E AMIGOS DOS EXCEPCIONAIS DE ITATIBA ", 54.88.toBigDecimal()))
    historicos.put("52646635000164", TFS3ValorHistorio("FERRAMENTARIA ITUPEVA COM. E IND. LTDA.", 54.88.toBigDecimal()))
    historicos.put("44496685000184", TFS3ValorHistorio("SINDICATO DOS TRAB NAS IND DO AC DE D C B B E MACATUBA", 54.88.toBigDecimal()))
    historicos.put("10491234000132", TFS3ValorHistorio("DM2 LIMPEZA E CONSERVACAO LIMITADA", 88.48.toBigDecimal()))
    historicos.put("08371465000160", TFS3ValorHistorio("Marcio Mitsuo Nakati", 54.88.toBigDecimal()))
    historicos.put("61387478000100", TFS3ValorHistorio("ESCRITORIO CUNHA LIMA SOCIEDADE SIMPLES LTDA", 88.48.toBigDecimal()))
    historicos.put("50116185000172", TFS3ValorHistorio("A RELA S/A INDÚSTRIA E COMERCIO", 88.48.toBigDecimal()))
    historicos.put("49311558000187", TFS3ValorHistorio("ADVANCE - INDUSTRIA TEXTIL LTDA", 54.88.toBigDecimal()))
    historicos.put("67165647000144", TFS3ValorHistorio("CNC CONTABILIDADE E AUDITORIA LTDA", 166.88.toBigDecimal()))
    historicos.put("46106514000127", TFS3ValorHistorio("SINDICATO DOS TRABALHADORES NAS INDUSTRIAS METALURGICAS, MECANICAS, DE MATERIAL ELETRICO E ELETRONIC", 88.48.toBigDecimal()))
    historicos.put("59620567000103", TFS3ValorHistorio("SINDICATO DOS TRABALHADORES NAS INDUSTRIAS DE LAPIS, CANETAS, QUIMICAS, FARMACEUTICAS, MATERIAL PLAS", 54.88.toBigDecimal()))
    historicos.put("46927133000109", TFS3ValorHistorio("SINDICATO DOS TRABALHADORES ASSALARIADOS RURAIS DE CAPIVARI E REGIAO", 49.39.toBigDecimal()))
    historicos.put("01428808000218", TFS3ValorHistorio("LATICINIOS SUICO HOLANDES LTDA ", 54.88.toBigDecimal()))
    historicos.put("46070678000141", TFS3ValorHistorio("SINDICATO DOS TRABALHADORES NAS INDUSTRIAS DE ALIMENTACAO DE CAMPINAS (SITAC)", 54.88.toBigDecimal()))
    historicos.put("56977002000190", TFS3ValorHistorio("SINDICATO DOS EMPREGADOS NO COMERCIO DE LIMEIRA", 54.88.toBigDecimal()))
    historicos.put("46058160000192", TFS3ValorHistorio("SIND DOS TRAB NAS IND DA CONSTRUCAO E DO MOBILIARIO", 54.88.toBigDecimal()))
    historicos.put("01847054000150", TFS3ValorHistorio("REDXCORP PRODUCAO E LOCACAO EIRELI", 54.88.toBigDecimal()))
    historicos.put("01997586000173", TFS3ValorHistorio("SIMOES E MOREIRA LTDA ", 54.88.toBigDecimal()))
    historicos.put("61472650000124", TFS3ValorHistorio("MSO INDUSTRIA DE PRODUTOS OTICOS LTDA", 54.88.toBigDecimal()))
    historicos.put("12315369000172", TFS3ValorHistorio("VIVA MAO DE OBRA TEMPORARIA E SERVICOS TERCERIZADOS LTDA", 88.48.toBigDecimal()))
    historicos.put("61702569000193", TFS3ValorHistorio("MACC CONTABILIDADE S/S", 54.88.toBigDecimal()))
    historicos.put("11894930000152", TFS3ValorHistorio("BRINQUEDOS ZUCATOYS INDUSTRIA E COMERCIO EIRELI", 88.48.toBigDecimal()))
  }

}

class TFS3ValorHistorio(
  val nome: String,
  val valor: BigDecimal,
)


