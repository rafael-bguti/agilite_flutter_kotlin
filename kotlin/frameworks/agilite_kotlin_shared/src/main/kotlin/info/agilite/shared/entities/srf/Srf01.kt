package info.agilite.shared.entities.srf;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import info.agilite.shared.entities.cgs.Cgs18
import info.agilite.shared.entities.cgs.Cgs80
import info.agilite.shared.entities.cas.gdf.Gdf10
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
const val SRF01INTEGRASCF_NAO_INTEGRADO = 0
const val SRF01INTEGRASCF_INTEGRADO = 1
const val SRF01INTEGRASCF_NAO_GEROU_DOCUMENTOS = 2
class Srf01() : AbstractEntity(20) {
  constructor(srf01id: Long) : this() {
    this.srf01id = srf01id
  }

  constructor(
    srf01empresa: Long,
    srf01natureza: Cgs18,
    srf01tipo: Int,
    srf01es: Int,
    srf01emitirDoc: Boolean,
    srf01numero: Int,
    srf01serie: Int,
    srf01dtEmiss: LocalDate,
    srf01hrEmiss: LocalTime,
    srf01entidade: Cgs80,
    srf01dtCanc: LocalDate,
    srf01vlrTotal: BigDecimal,
    srf01vlrIss: BigDecimal,
    srf01dfeAprov: Gdf10,
    srf01dfeCancAprov: Gdf10,
    srf01integraScf: Int,
    srf01numeroNFSe: Int
  ) : this() {
    this.srf01empresa = srf01empresa
    this.srf01natureza = srf01natureza
    this.srf01tipo = srf01tipo
    this.srf01es = srf01es
    this.srf01emitirDoc = srf01emitirDoc
    this.srf01numero = srf01numero
    this.srf01serie = srf01serie
    this.srf01dtEmiss = srf01dtEmiss
    this.srf01hrEmiss = srf01hrEmiss
    this.srf01entidade = srf01entidade
    this.srf01dtCanc = srf01dtCanc
    this.srf01vlrTotal = srf01vlrTotal
    this.srf01vlrIss = srf01vlrIss
    this.srf01dfeAprov = srf01dfeAprov
    this.srf01dfeCancAprov = srf01dfeCancAprov
    this.srf01integraScf = srf01integraScf
    this.srf01numeroNFSe = srf01numeroNFSe
  }


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
    
  var srf01emitirDoc: Boolean = false
    get() {
      validateLoaded(5, "srf01emitirDoc", true)
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
    
  var srf01dtCanc: LocalDate? = null
    get() {
      validateLoaded(11, "srf01dtCanc", false)
      return field
    }
    set(value){
      orm.changed(field, value, 11)
      field = value
    }
    
  var srf01vlrTotal: BigDecimal = BigDecimal("-1")
    get() {
      validateLoaded(12, "srf01vlrTotal", true)
      return field
    }
    set(value){
      orm.changed(field, value, 12)
      field = value
    }
    
  var srf01vlrIss: BigDecimal? = null
    get() {
      validateLoaded(13, "srf01vlrIss", false)
      return field
    }
    set(value){
      orm.changed(field, value, 13)
      field = value
    }
    
  var srf01dfeAprov: Gdf10? = null
    get() {
      validateLoaded(14, "srf01dfeAprov", false)
      return field
    }
    set(value){
      orm.changed(field, value, 14)
      field = value
    }
    
  var srf01dfeCancAprov: Gdf10? = null
    get() {
      validateLoaded(15, "srf01dfeCancAprov", false)
      return field
    }
    set(value){
      orm.changed(field, value, 15)
      field = value
    }
    
  var srf01integraScf: Int = 0
    get() {
      validateLoaded(16, "srf01integraScf", true)
      return field ?: 0
    }
    set(value){
      orm.changed(field, value, 16)
      field = value
    }
    
  var srf01numeroNFSe: Int? = null
    get() {
      validateLoaded(17, "srf01numeroNFSe", false)
      return field
    }
    set(value){
      orm.changed(field, value, 17)
      field = value
    }
    
  var srf011s: Set<Srf011>? = null
    get() {
      validateLoaded(18, "srf011s", false)
      return field
    }
    set(value){
      orm.changed(field, value, 18)
      field = value
    }
    
  var srf012s: Set<Srf012>? = null
    get() {
      validateLoaded(19, "srf012s", false)
      return field
    }
    set(value){
      orm.changed(field, value, 19)
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
const val N_SRF01EMPRESA = "srf01empresa";
const val N_SRF01NATUREZA = "srf01natureza";
const val N_SRF01TIPO = "srf01tipo";
const val N_SRF01ES = "srf01es";
const val N_SRF01EMITIRDOC = "srf01emitirDoc";
const val N_SRF01NUMERO = "srf01numero";
const val N_SRF01SERIE = "srf01serie";
const val N_SRF01DTEMISS = "srf01dtEmiss";
const val N_SRF01HREMISS = "srf01hrEmiss";
const val N_SRF01ENTIDADE = "srf01entidade";
const val N_SRF01DTCANC = "srf01dtCanc";
const val N_SRF01VLRTOTAL = "srf01vlrTotal";
const val N_SRF01VLRISS = "srf01vlrIss";
const val N_SRF01DFEAPROV = "srf01dfeAprov";
const val N_SRF01DFECANCAPROV = "srf01dfeCancAprov";
const val N_SRF01INTEGRASCF = "srf01integraScf";
const val N_SRF01NUMERONFSE = "srf01numeroNFSe";

val SRF01ID = FieldMetadata("srf01id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val SRF01EMPRESA = FieldMetadata("srf01empresa", 1, "Empresa", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false);
val SRF01NATUREZA = FieldMetadata("srf01natureza", 2, "Natureza da Operação", FieldTypeMetadata.fk, 10.0, true, "Cgs18", null, null, null, null, false, false, false);
val SRF01TIPO = FieldMetadata("srf01tipo", 3, "Tipo", FieldTypeMetadata.int, 1.0, true, null, listOf(FieldOptionMetadata(0, "Orçamento"),FieldOptionMetadata(1, "Pedido"),FieldOptionMetadata(2, "Nota Fiscal Produto"),FieldOptionMetadata(3, "Nota Fiscal Serviço")), null, null, null, false, false, false);
val SRF01ES = FieldMetadata("srf01es", 4, "Entrada/Saida", FieldTypeMetadata.int, 1.0, true, null, listOf(FieldOptionMetadata(0, "Entrada"),FieldOptionMetadata(1, "Saída")), null, null, null, false, false, false);
val SRF01EMITIRDOC = FieldMetadata("srf01emitirDoc", 5, "Emitir documento fiscal", FieldTypeMetadata.boolean, 1.0, true, null, null, null, null, null, false, false, false);
val SRF01NUMERO = FieldMetadata("srf01numero", 6, "Número do documento", FieldTypeMetadata.int, 9.0, true, null, null, null, null, null, true, true, false);
val SRF01SERIE = FieldMetadata("srf01serie", 7, "Série", FieldTypeMetadata.int, 3.0, false, null, null, null, null, null, false, false, false);
val SRF01DTEMISS = FieldMetadata("srf01dtEmiss", 8, "Data de emissão", FieldTypeMetadata.date, 10.0, true, null, null, null, null, null, true, true, false);
val SRF01HREMISS = FieldMetadata("srf01hrEmiss", 9, "Hora de emissão", FieldTypeMetadata.time, 4.0, false, null, null, null, null, null, false, false, false);
val SRF01ENTIDADE = FieldMetadata("srf01entidade", 10, "Cliente ou Fornecedor", FieldTypeMetadata.fk, 10.0, true, "Cgs80", null, null, null, null, true, true, false);
val SRF01DTCANC = FieldMetadata("srf01dtCanc", 11, "Data de cancelamento", FieldTypeMetadata.date, 10.0, false, null, null, null, null, null, true, true, false);
val SRF01VLRTOTAL = FieldMetadata("srf01vlrTotal", 12, "Valor total", FieldTypeMetadata.decimal, 16.2, true, null, null, null, null, null, true, true, false);
val SRF01VLRISS = FieldMetadata("srf01vlrIss", 13, "Valor do ISS", FieldTypeMetadata.decimal, 16.2, false, null, null, null, null, null, false, false, false);
val SRF01DFEAPROV = FieldMetadata("srf01dfeAprov", 14, "Registro de DFE aprovado", FieldTypeMetadata.fk, 10.0, false, "Gdf10", null, null, null, null, false, false, false);
val SRF01DFECANCAPROV = FieldMetadata("srf01dfeCancAprov", 15, "Registro de DFE de Cancelamento Aprovado", FieldTypeMetadata.fk, 10.0, false, "Gdf10", null, null, null, null, false, false, false);
val SRF01INTEGRASCF = FieldMetadata("srf01integraScf", 16, "Integração com o SCF", FieldTypeMetadata.int, 1.0, true, null, listOf(FieldOptionMetadata(0, "Não integrado"),FieldOptionMetadata(1, "Integrado"),FieldOptionMetadata(2, "Não gerou documentos")), null, null, null, false, false, false);
val SRF01NUMERONFSE = FieldMetadata("srf01numeroNFSe", 17, "Número da NFSe", FieldTypeMetadata.int, 9.0, false, null, null, null, null, null, false, false, false);
 
val SRF01_METADATA = EntityMetadata(
  name = "Srf01",
  descr = "Documentos Fiscais",

  fields = listOf(
    SRF01ID,SRF01EMPRESA,SRF01NATUREZA,SRF01TIPO,SRF01ES,SRF01EMITIRDOC,SRF01NUMERO,SRF01SERIE,SRF01DTEMISS,SRF01HREMISS,SRF01ENTIDADE,SRF01DTCANC,SRF01VLRTOTAL,SRF01VLRISS,SRF01DFEAPROV,SRF01DFECANCAPROV,SRF01INTEGRASCF,SRF01NUMERONFSE,
  ),

  keys = listOf(
    KeyMetadata("srf01_uk", KeyMetadataType.uk, "srf01empresa"),
  ),

  oneToMany = mapOf(
    "srf011s" to OneToManyMetadata(18, Srf011::class.java, "srf011doc"),
    "srf012s" to OneToManyMetadata(19, Srf012::class.java, "srf012doc"),
  )
)

//GERADOR END
