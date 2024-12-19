package info.agilite.shared.entities.srf;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import info.agilite.shared.entities.cgs.CGS18SCF_NAO_GERAR
import info.agilite.shared.entities.cgs.Cgs18
import info.agilite.shared.entities.cgs.Cgs80
import info.agilite.shared.entities.gdf.Gdf10
import info.agilite.shared.events.INTEGRACAO_AGUARDANDO_O_INICIO
import info.agilite.shared.events.INTEGRACAO_NAO_EXECUTAR
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
//GERADOR INI

const val SRF01TIPO_ORCAMENTO = 0
const val SRF01TIPO_PEDIDO = 1
const val SRF01TIPO_NOTA_FISCAL_PRODUTO = 2
const val SRF01TIPO_NOTA_FISCAL_SERVICO = 3
const val SRF01ES_ENTRADA = 0
const val SRF01ES_SAIDA = 1

class Srf01() : AbstractEntity(33) {
  constructor(srf01id: Long) : this() {
    this.srf01id = srf01id
  }

  constructor(
    srf01empresa: Long? = null,
    srf01natureza: Cgs18,
    srf01tipo: Int,
    srf01es: Int,
    srf01ep: Boolean,
    srf01numero: Int,
    srf01serie: Int? = null,
    srf01dtEmiss: LocalDate,
    srf01hrEmiss: LocalTime? = null,
    srf01entidade: Cgs80,
    srf01nome: String,
    srf01ni: String? = null,
    srf01ie: String? = null,
    srf01im: String? = null,
    srf01cep: String? = null,
    srf01endereco: String? = null,
    srf01bairro: String? = null,
    srf01endNumero: String? = null,
    srf01complemento: String? = null,
    srf01uf: String? = null,
    srf01municipio: String? = null,
    srf01dtCanc: LocalDate? = null,
    srf01vlrTotal: BigDecimal,
    srf01vlrIss: BigDecimal? = null,
    srf01dfeAprov: Gdf10? = null,
    srf01dfeCancAprov: Gdf10? = null,
    srf01integracaoScf: Int,
    srf01integracaoGdf: Int,
    srf01obs: String? = null,
    srf01dtEmail: LocalDate? = null
  ) : this() {
    if(srf01empresa != null) this.srf01empresa = srf01empresa
    this.srf01natureza = srf01natureza
    this.srf01tipo = srf01tipo
    this.srf01es = srf01es
    this.srf01ep = srf01ep
    this.srf01numero = srf01numero
    this.srf01serie = srf01serie
    this.srf01dtEmiss = srf01dtEmiss
    this.srf01hrEmiss = srf01hrEmiss
    this.srf01entidade = srf01entidade
    this.srf01nome = srf01nome
    this.srf01ni = srf01ni
    this.srf01ie = srf01ie
    this.srf01im = srf01im
    this.srf01cep = srf01cep
    this.srf01endereco = srf01endereco
    this.srf01bairro = srf01bairro
    this.srf01endNumero = srf01endNumero
    this.srf01complemento = srf01complemento
    this.srf01uf = srf01uf
    this.srf01municipio = srf01municipio
    this.srf01dtCanc = srf01dtCanc
    this.srf01vlrTotal = srf01vlrTotal
    this.srf01vlrIss = srf01vlrIss
    this.srf01dfeAprov = srf01dfeAprov
    this.srf01dfeCancAprov = srf01dfeCancAprov
    this.srf01integracaoScf = srf01integracaoScf
    this.srf01integracaoGdf = srf01integracaoGdf
    this.srf01obs = srf01obs
    this.srf01dtEmail = srf01dtEmail
  }


  //CUSTOM INI
  constructor(
    srf01empresa: Long,
    srf01natureza: Cgs18,
    srf01tipo: Int,
    srf01es: Int,
    srf01numero: Int,
    srf01serie: Int? = null,
    srf01dtEmiss: LocalDate,
    srf01hrEmiss: LocalTime? = null,
    srf01entidade: Cgs80,
    srf01dtCanc: LocalDate? = null,
    srf01vlrTotal: BigDecimal,
    srf01vlrIss: BigDecimal? = null,
    srf01dfeAprov: Gdf10? = null,
    srf01dfeCancAprov: Gdf10? = null,
    srf01obs: String? = null
  ) : this(
    srf01empresa = srf01empresa,
    srf01natureza = srf01natureza,
    srf01tipo = srf01tipo,
    srf01es = srf01es,
    srf01ep = true,
    srf01numero = srf01numero,
    srf01serie = srf01serie,
    srf01dtEmiss = srf01dtEmiss,
    srf01hrEmiss = srf01hrEmiss,
    srf01entidade = srf01entidade,
    srf01nome = srf01entidade.cgs80nome,
    srf01ni = srf01entidade.cgs80ni,
    srf01ie = srf01entidade.cgs80ie,
    srf01im = srf01entidade.cgs80im,
    srf01cep = srf01entidade.cgs80cep,
    srf01endereco = srf01entidade.cgs80endereco,
    srf01bairro = srf01entidade.cgs80bairro,
    srf01endNumero = srf01entidade.cgs80numero,
    srf01complemento = srf01entidade.cgs80complemento,
    srf01uf = srf01entidade.cgs80uf,
    srf01municipio = srf01entidade.cgs80municipio,
    srf01dtCanc = srf01dtCanc,
    srf01vlrTotal = srf01vlrTotal,
    srf01vlrIss = srf01vlrIss,
    srf01dfeAprov = srf01dfeAprov,
    srf01dfeCancAprov = srf01dfeCancAprov,
    srf01integracaoScf = if(srf01natureza.cgs18scf == CGS18SCF_NAO_GERAR) INTEGRACAO_NAO_EXECUTAR else INTEGRACAO_AGUARDANDO_O_INICIO,
    srf01integracaoGdf = if(srf01natureza.cgs18emitirDoc) INTEGRACAO_AGUARDANDO_O_INICIO else INTEGRACAO_NAO_EXECUTAR,
    srf01obs = srf01obs,
  )


  //CUSTOM END

  var srf01id: Long = -1L
    get() {
      validateLoaded(0, "srf01id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var srf01empresa: Long = -1
    get() {
      validateLoaded(1, "srf01empresa", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var srf01natureza: Cgs18 = Cgs18()
    get() {
      validateLoaded(2, "srf01natureza", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var srf01tipo: Int = -1
    get() {
      validateLoaded(3, "srf01tipo", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    
  var srf01es: Int = -1
    get() {
      validateLoaded(4, "srf01es", true)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }
    
  var srf01ep: Boolean = false
    get() {
      validateLoaded(5, "srf01ep", true)
      return field
    }
    set(value){
      orm.changed(field, value, 5)
      field = value
    }
    
  var srf01numero: Int = -1
    get() {
      validateLoaded(6, "srf01numero", true)
      return field
    }
    set(value){
      orm.changed(field, value, 6)
      field = value
    }
    
  var srf01serie: Int? = null
    get() {
      validateLoaded(7, "srf01serie", false)
      return field
    }
    set(value){
      orm.changed(field, value, 7)
      field = value
    }
    
  var srf01dtEmiss: LocalDate = LocalDate.now()
    get() {
      validateLoaded(8, "srf01dtEmiss", true)
      return field
    }
    set(value){
      orm.changed(field, value, 8)
      field = value
    }
    
  var srf01hrEmiss: LocalTime? = null
    get() {
      validateLoaded(9, "srf01hrEmiss", false)
      return field
    }
    set(value){
      orm.changed(field, value, 9)
      field = value
    }
    
  var srf01entidade: Cgs80 = Cgs80()
    get() {
      validateLoaded(10, "srf01entidade", true)
      return field
    }
    set(value){
      orm.changed(field, value, 10)
      field = value
    }
    
  var srf01nome: String = "--defaultString--"
    get() {
      validateLoaded(11, "srf01nome", true)
      return field
    }
    set(value){
      orm.changed(field, value, 11)
      field = value
    }
    
  var srf01ni: String? = null
    get() {
      validateLoaded(12, "srf01ni", false)
      return field
    }
    set(value){
      orm.changed(field, value, 12)
      field = value
    }
    
  var srf01ie: String? = null
    get() {
      validateLoaded(13, "srf01ie", false)
      return field
    }
    set(value){
      orm.changed(field, value, 13)
      field = value
    }
    
  var srf01im: String? = null
    get() {
      validateLoaded(14, "srf01im", false)
      return field
    }
    set(value){
      orm.changed(field, value, 14)
      field = value
    }
    
  var srf01cep: String? = null
    get() {
      validateLoaded(15, "srf01cep", false)
      return field
    }
    set(value){
      orm.changed(field, value, 15)
      field = value
    }
    
  var srf01endereco: String? = null
    get() {
      validateLoaded(16, "srf01endereco", false)
      return field
    }
    set(value){
      orm.changed(field, value, 16)
      field = value
    }
    
  var srf01bairro: String? = null
    get() {
      validateLoaded(17, "srf01bairro", false)
      return field
    }
    set(value){
      orm.changed(field, value, 17)
      field = value
    }
    
  var srf01endNumero: String? = null
    get() {
      validateLoaded(18, "srf01endNumero", false)
      return field
    }
    set(value){
      orm.changed(field, value, 18)
      field = value
    }
    
  var srf01complemento: String? = null
    get() {
      validateLoaded(19, "srf01complemento", false)
      return field
    }
    set(value){
      orm.changed(field, value, 19)
      field = value
    }
    
  var srf01uf: String? = null
    get() {
      validateLoaded(20, "srf01uf", false)
      return field
    }
    set(value){
      orm.changed(field, value, 20)
      field = value
    }
    
  var srf01municipio: String? = null
    get() {
      validateLoaded(21, "srf01municipio", false)
      return field
    }
    set(value){
      orm.changed(field, value, 21)
      field = value
    }
    
  var srf01dtCanc: LocalDate? = null
    get() {
      validateLoaded(22, "srf01dtCanc", false)
      return field
    }
    set(value){
      orm.changed(field, value, 22)
      field = value
    }
    
  var srf01vlrTotal: BigDecimal = BigDecimal("-1")
    get() {
      validateLoaded(23, "srf01vlrTotal", true)
      return field
    }
    set(value){
      orm.changed(field, value, 23)
      field = value
    }
    
  var srf01vlrIss: BigDecimal? = null
    get() {
      validateLoaded(24, "srf01vlrIss", false)
      return field
    }
    set(value){
      orm.changed(field, value, 24)
      field = value
    }
    
  var srf01dfeAprov: Gdf10? = null
    get() {
      validateLoaded(25, "srf01dfeAprov", false)
      return field
    }
    set(value){
      orm.changed(field, value, 25)
      field = value
    }
    
  var srf01dfeCancAprov: Gdf10? = null
    get() {
      validateLoaded(26, "srf01dfeCancAprov", false)
      return field
    }
    set(value){
      orm.changed(field, value, 26)
      field = value
    }
    
  var srf01integracaoScf: Int = 0
    get() {
      validateLoaded(27, "srf01integracaoScf", true)
      return field ?: 0
    }
    set(value){
      orm.changed(field, value, 27)
      field = value
    }
    
  var srf01integracaoGdf: Int = 0
    get() {
      validateLoaded(28, "srf01integracaoGdf", true)
      return field ?: 0
    }
    set(value){
      orm.changed(field, value, 28)
      field = value
    }
    
  var srf01obs: String? = null
    get() {
      validateLoaded(29, "srf01obs", false)
      return field
    }
    set(value){
      orm.changed(field, value, 29)
      field = value
    }
    
  var srf01dtEmail: LocalDate? = null
    get() {
      validateLoaded(30, "srf01dtEmail", false)
      return field
    }
    set(value){
      orm.changed(field, value, 30)
      field = value
    }
    
  var srf011s: Set<Srf011>? = null
    get() {
      validateLoaded(31, "srf011s", false)
      return field
    }
    set(value){
      orm.changed(field, value, 31)
      field = value
    }
    
  var srf012s: Set<Srf012>? = null
    get() {
      validateLoaded(32, "srf012s", false)
      return field
    }
    set(value){
      orm.changed(field, value, 32)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else srf01id
    set(value) { if(value != null)this.srf01id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Srf01
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = SRF01_METADATA
}
const val N_SRF01_EMPRESA = "srf01empresa";
const val N_SRF01_NATUREZA = "srf01natureza";
const val N_SRF01_TIPO = "srf01tipo";
const val N_SRF01_ES = "srf01es";
const val N_SRF01_EP = "srf01ep";
const val N_SRF01_NUMERO = "srf01numero";
const val N_SRF01_SERIE = "srf01serie";
const val N_SRF01_DT_EMISS = "srf01dtEmiss";
const val N_SRF01_HR_EMISS = "srf01hrEmiss";
const val N_SRF01_ENTIDADE = "srf01entidade";
const val N_SRF01_NOME = "srf01nome";
const val N_SRF01_NI = "srf01ni";
const val N_SRF01_IE = "srf01ie";
const val N_SRF01_IM = "srf01im";
const val N_SRF01_CEP = "srf01cep";
const val N_SRF01_ENDERECO = "srf01endereco";
const val N_SRF01_BAIRRO = "srf01bairro";
const val N_SRF01_END_NUMERO = "srf01endNumero";
const val N_SRF01_COMPLEMENTO = "srf01complemento";
const val N_SRF01_UF = "srf01uf";
const val N_SRF01_MUNICIPIO = "srf01municipio";
const val N_SRF01_DT_CANC = "srf01dtCanc";
const val N_SRF01_VLR_TOTAL = "srf01vlrTotal";
const val N_SRF01_VLR_ISS = "srf01vlrIss";
const val N_SRF01_DFE_APROV = "srf01dfeAprov";
const val N_SRF01_DFE_CANC_APROV = "srf01dfeCancAprov";
const val N_SRF01_INTEGRACAO_SCF = "srf01integracaoScf";
const val N_SRF01_INTEGRACAO_GDF = "srf01integracaoGdf";
const val N_SRF01_OBS = "srf01obs";
const val N_SRF01_DT_EMAIL = "srf01dtEmail";
const val N_SRF011S = "srf011s";
const val N_SRF012S = "srf012s";

val SRF01ID = FieldMetadata("srf01id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val SRF01EMPRESA = FieldMetadata("srf01empresa", 1, "Empresa", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false);
val SRF01NATUREZA = FieldMetadata("srf01natureza", 2, "Natureza da Operação", FieldTypeMetadata.fk, 10.0, true, "Cgs18", null, null, null, null, false, false, false);
val SRF01TIPO = FieldMetadata("srf01tipo", 3, "Tipo", FieldTypeMetadata.int, 1.0, true, null, listOf(FieldOptionMetadata(0, "Orçamento"),FieldOptionMetadata(1, "Pedido"),FieldOptionMetadata(2, "Nota Fiscal Produto"),FieldOptionMetadata(3, "Nota Fiscal Serviço")), null, null, null, false, false, false);
val SRF01ES = FieldMetadata("srf01es", 4, "Entrada/Saida", FieldTypeMetadata.int, 1.0, true, null, listOf(FieldOptionMetadata(0, "Entrada"),FieldOptionMetadata(1, "Saída")), null, null, null, false, false, false);
val SRF01EP = FieldMetadata("srf01ep", 5, "Emissão própria", FieldTypeMetadata.boolean, 1.0, true, null, null, null, null, null, false, false, false);
val SRF01NUMERO = FieldMetadata("srf01numero", 6, "Número do documento", FieldTypeMetadata.int, 9.0, true, null, null, null, null, null, true, true, false);
val SRF01SERIE = FieldMetadata("srf01serie", 7, "Série", FieldTypeMetadata.int, 3.0, false, null, null, null, null, null, false, false, false);
val SRF01DTEMISS = FieldMetadata("srf01dtEmiss", 8, "Data de emissão", FieldTypeMetadata.date, 10.0, true, null, null, null, null, null, true, true, false);
val SRF01HREMISS = FieldMetadata("srf01hrEmiss", 9, "Hora de emissão", FieldTypeMetadata.time, 4.0, false, null, null, null, null, null, false, false, false);
val SRF01ENTIDADE = FieldMetadata("srf01entidade", 10, "Cliente ou Fornecedor", FieldTypeMetadata.fk, 10.0, true, "Cgs80", null, null, null, null, true, true, false);
val SRF01NOME = FieldMetadata("srf01nome", 11, "Nome ou Razão Social", FieldTypeMetadata.string, 250.0, true, null, null, null, null, null, true, true, true);
val SRF01NI = FieldMetadata("srf01ni", 12, "CPF/CNPJ", FieldTypeMetadata.string, 20.0, false, null, null, null, null, null, true, true, true);
val SRF01IE = FieldMetadata("srf01ie", 13, "Inscrição estadual", FieldTypeMetadata.string, 30.0, false, null, null, null, null, null, false, false, false);
val SRF01IM = FieldMetadata("srf01im", 14, "Inscrição municipal", FieldTypeMetadata.string, 30.0, false, null, null, null, null, null, false, false, false);
val SRF01CEP = FieldMetadata("srf01cep", 15, "CEP", FieldTypeMetadata.string, 8.0, false, null, null, null, null, null, false, false, false);
val SRF01ENDERECO = FieldMetadata("srf01endereco", 16, "Endereço", FieldTypeMetadata.string, 200.0, false, null, null, null, null, null, false, false, false);
val SRF01BAIRRO = FieldMetadata("srf01bairro", 17, "Bairro", FieldTypeMetadata.string, 50.0, false, null, null, null, null, null, false, false, false);
val SRF01ENDNUMERO = FieldMetadata("srf01endNumero", 18, "Número", FieldTypeMetadata.string, 50.0, false, null, null, null, null, null, false, false, false);
val SRF01COMPLEMENTO = FieldMetadata("srf01complemento", 19, "Complemento", FieldTypeMetadata.string, 50.0, false, null, null, null, null, null, false, false, false);
val SRF01UF = FieldMetadata("srf01uf", 20, "UF", FieldTypeMetadata.string, 2.0, false, null, null, null, null, null, false, false, false);
val SRF01MUNICIPIO = FieldMetadata("srf01municipio", 21, "Município", FieldTypeMetadata.string, 50.0, false, null, null, null, null, null, true, true, false);
val SRF01DTCANC = FieldMetadata("srf01dtCanc", 22, "Data de cancelamento", FieldTypeMetadata.date, 10.0, false, null, null, null, null, null, true, true, false);
val SRF01VLRTOTAL = FieldMetadata("srf01vlrTotal", 23, "Valor total", FieldTypeMetadata.decimal, 16.2, true, null, null, null, null, null, true, true, false);
val SRF01VLRISS = FieldMetadata("srf01vlrIss", 24, "Valor do ISS", FieldTypeMetadata.decimal, 16.2, false, null, null, null, null, null, false, false, false);
val SRF01DFEAPROV = FieldMetadata("srf01dfeAprov", 25, "Registro de DFE aprovado", FieldTypeMetadata.fk, 10.0, false, "Gdf10", null, null, null, null, false, false, false);
val SRF01DFECANCAPROV = FieldMetadata("srf01dfeCancAprov", 26, "Registro de DFE de Cancelamento Aprovado", FieldTypeMetadata.fk, 10.0, false, "Gdf10", null, null, null, null, false, false, false);
val SRF01INTEGRACAOSCF = FieldMetadata("srf01integracaoScf", 27, "Integração com o SCF", FieldTypeMetadata.int, 1.0, true, null, null, null, null, null, false, false, false);
val SRF01INTEGRACAOGDF = FieldMetadata("srf01integracaoGdf", 28, "Integração com o GDF", FieldTypeMetadata.int, 1.0, true, null, null, null, null, null, false, false, false);
val SRF01OBS = FieldMetadata("srf01obs", 29, "Observações", FieldTypeMetadata.string, 0.0, false, null, null, null, null, null, false, false, false);
val SRF01DTEMAIL = FieldMetadata("srf01dtEmail", 30, "Data do envio do e-mail", FieldTypeMetadata.date, 10.0, false, null, null, null, null, null, false, false, false);
 
val SRF01_METADATA = EntityMetadata(
  name = "Srf01",
  descr = "Documentos Fiscais",

  fields = listOf(
    SRF01ID,SRF01EMPRESA,SRF01NATUREZA,SRF01TIPO,SRF01ES,SRF01EP,SRF01NUMERO,SRF01SERIE,SRF01DTEMISS,SRF01HREMISS,SRF01ENTIDADE,SRF01NOME,SRF01NI,SRF01IE,SRF01IM,SRF01CEP,SRF01ENDERECO,SRF01BAIRRO,SRF01ENDNUMERO,SRF01COMPLEMENTO,SRF01UF,SRF01MUNICIPIO,SRF01DTCANC,SRF01VLRTOTAL,SRF01VLRISS,SRF01DFEAPROV,SRF01DFECANCAPROV,SRF01INTEGRACAOSCF,SRF01INTEGRACAOGDF,SRF01OBS,SRF01DTEMAIL,
  ),

  keys = listOf(
    KeyMetadata("srf01_uk", KeyMetadataType.uk, "srf01empresa, srf01natureza, srf01numero, srf01serie"),
  ),

  oneToMany = mapOf(
    "srf011s" to OneToManyMetadata(31, Srf011::class.java, "srf011doc"),
    "srf012s" to OneToManyMetadata(32, Srf012::class.java, "srf012doc"),
  )
)

//GERADOR END
