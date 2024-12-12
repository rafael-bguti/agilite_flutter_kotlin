package info.agilite.core.utils

object CsvUtils {
  fun parse(content: String): List<List<String>> {
    return content.split("\n")
      .map { it.trim() }
      .filter { it.isNotEmpty() }
      .filter { !it.startsWith("#") }
      .toList().let {
        removeBreakLines(it)
      }.map { splitLine(it) }
  }

  private fun removeBreakLines(list: List<String>): List<String> {
    val result = mutableListOf<String>()

    val currentLine = StringBuilder()
    var quotesStart = false
    for (line in list) {
      for (i in line.indices) {
        val currentChar = line[i]
        if (currentChar == '"' && (i == 0 || line[i - 1] != '\\')) {
          quotesStart = !quotesStart
        }
        currentLine.append(currentChar)
      }

      if (!quotesStart) {// fechou todas as aspas
        result.add(currentLine.toString())
        currentLine.clear()
      }else{
        currentLine.append("\n")
      }
    }

    return result
  }

  private fun splitLine(line: String): List<String> {
    val result = mutableListOf<String>()
    val currentField = StringBuilder()
    var quotesStart = false

    for (i in line.indices) {
      val currentChar = line[i]
      if (currentChar == '"' && (i == 0 || line[i - 1] != '\\')) {
        quotesStart = !quotesStart
      } else if (currentChar == ',' && !quotesStart) {
        result.add(processField(currentField))
        currentField.clear()
      } else {
        currentField.append(currentChar)
      }
    }


    if(currentField.isNotEmpty()) {
      result.add(processField(currentField))
    }

    return result
  }

  private fun processField(field: StringBuilder): String {
    return field.toString().replace("\"", "")
  }
}
//
//fun main() {
////  val result = CsvUtils.parse(teste1)
//  val result = CsvUtils.parse(teste)
//  for(line in result.indices) {
//    for(coluna in result[line].indices){
//      println("Linha: $line, Coluna: $coluna, Valor: ${result[line][coluna]}")
//
//    }
//  }
//}
//
//private val teste1 = """
//pre,"linha 1,
//linha 2,
//linha 3",pos
//pre,"linha 10,
//linha 20,
//linha 30",pos
//""".trimIndent()
//
//
//private val teste = """
//#-Arquivos de cobrança SAM4
//
//
//#Revenda(cnpj='08041707000158', rs='BR-SIS Soluções em Sistemas de Processamento de Dados Ltda M')
//#Total revenda: 653
//3,NFSe,08041707000158,"
// - Licença SAM: 07.204.591/0001-68  Biotec Produtos Hospitalares Ltda
//   - Qtd: 2156 Tipo: NFe -  (07204591000168-BIOTEC, 51920700000135-BIOTEC, 11934368000143-BIOPACK, 07204591000320-BIOTEC ITAPEVA, 14809184000140-BIOLINE)
//   Total Licença: 456
// - Licença SAM: 51.490.621/0001-31  Leoni Alimentos Ltda
//   - Qtd: 283 Tipo: NFe -  (51490621000131-LEONI)
//   Total Licença: 151
// - Licença SAM: 15.169.918/0001-36  DEPROI Desenvolvimento de Projetos Insdustriais Ltda. ME.
//   - Manutenção dos documentos armazenados. (15169918000136-DEPROI, 00567953000136-DEPROI  - Serviços)
//   Total Licença: 46
//"
//10,1,1,653.0
//30,Boleto Inter,20241231,653.0
//
//
//#Revenda(cnpj='03784086000189', rs='Solution Sys - Soluções em Sistemas Ltda ME')
//#Total revenda: 46
//3,NFSe,03784086000189,"
// - Licença SAM: 64.856.974/0001-54  Suzan Indústria e Comércio de Materiais de Construção Ltda.
//   - Qtd: 18 Tipo: NFe -  (64856974000154-SUZAN TELAS)
//   Total Licença: 0
// - Licença SAM: 16.937.595/0001-46  QI Equipamentos para Automação Industrial Ltda ME
//   - Qtd: 53 Tipo: NFe -  (16937595000146-QI AUTOMACAO)
//   Total Licença: 0
// - Licença SAM: 03.784.086/0001-89  Solution Sys Soluções em Sistemas Ltda. ME
//   - Qtd: 8 Tipo: NFe -  (05758809000109-JURP, 36242369000158-FLOR DE LOTUS, 30697666000101-MARIC)
//   Total Licença: 46
//"
//10,1,1,46.0
//30,Boleto Inter,20241231,46.0
//
//
//#Revenda(cnpj='08194121000123', rs='LCR Apoio Administrativo Ltda - ME')
//#Total revenda: 92
//3,NFSe,08194121000123,"
// - Licença SAM: 08.194.121/0001-23  LCR Apoio Administrativo Ltda. ME
//   - Qtd: 2 Tipo: NFe -  (42879117000137-MMRTQ PLASTICOS LTDA, 12131513000110-FAST TOOL INJECAO)
//   Total Licença: 46
// - Licença SAM: 22.742.237/0001-80  Profer Equipamentos Industriais
//   - Manutenção dos documentos armazenados. (22742237000180-PROFER, 11219264000157-MT, 48133044000116-Z2)
//   Total Licença: 46
//"
//10,1,1,92.0
//30,Boleto Inter,20241231,92.0
//
//
//#Revenda(cnpj='04150109000166', rs='R&O Informática Ltda. ME')
//#Total revenda: 0
//3,NFSe,04150109000166,"
//"
//10,1,1,0.0
//30,Boleto Inter,20241231,0.0
//
//
//#Revenda(cnpj='52104228000125', rs='ELTech Sistemas')
//#Total revenda: 794
//3,NFSe,52104228000125,"
// - Licença SAM: 27.253.358/0003-44  Mitra Comercio Internacional e Distribuicao Ltda
//   - Qtd: 229 Tipo: NFe -  (27253358000344-MITRA MG, 27253358000182-MITRA SC)
//   Total Licença: 151
// - Licença SAM: 32.654.854/0001-88  Equilíbrio, Indústria, Comércio, Importação EIRELI.
//   - Qtd: 187 Tipo: NFe -  (41801484000155-COMPANY, 32654854000188-EQUILÍBRIO)
//   Total Licença: 106
// - Licença SAM: 74.536.111/0001-53  Vetro Textil Indústria e Comércio Ltda.
//   - Qtd: 159 Tipo: NFe -  (74536111000153-VETROTEXTIL, 46676542000180-TECTAPE)
//   Total Licença: 106
// - Licença SAM: 36.406.221/0001-08  LCB Distribuidora
//   - Qtd: 1469 Tipo: NFe -  (39406221000108-LCB DISTRIBUIDORA, 51728642000142-ARS ITATIBA)
//   Total Licença: 385
// - Licença SAM: 08.642.960/0001-67  Catira e Casadei Comércio de Cortinas Ltda.
//   - Qtd: 2 Tipo: NFe -  (08642960000167-REQUINTE)
//   Total Licença: 46
//"
//10,1,1,794.0
//30,Boleto Inter,20241231,794.0
//
//
//#Revenda(cnpj='10483676000137', rs='Potencial Tecnologia em Sistemas Ltda. - ME ')
//#Total revenda: 102
//3,NFSe,10483676000137,"
// - Licença SAM: 46.538.868/0001-40  Soluc Implementos Rodoviários Ltda
//   - Qtd: 74 Tipo: NFe -  (46538868000140-SOLUC)
//   - Qtd: 11 Tipo: CTe -  (46538868000140-SOLUC)
//   Total Licença: 102
//"
//10,1,1,102.0
//30,Boleto Inter,20241231,102.0
//
//
//#Revenda(cnpj='37013890000186', rs='Edson Francisco Shinichi Kawazoe')
//#Total revenda: 106
//3,NFSe,37013890000186,"
// - Licença SAM: 09.341.846/0001-60  Arqtec Comercio de Serviços Ltda.
//   - Qtd: 115 Tipo: NFe -  (09341846000160-ARQTEC, 39872271000172-R-TEC, 39908896000147-V-ARQ, 36741438000178-TEC-ARQ, 53034804000178-L ARQ)
//   Total Licença: 106
//"
//10,1,1,106.0
//30,Boleto Inter,20241231,106.0
//
//"""