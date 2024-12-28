package info.agilite.shared.entities.scf;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import info.agilite.core.model.LowerCaseMap
import info.agilite.shared.entities.cgs.Cgs38
import info.agilite.shared.entities.cgs.Cgs80
import java.math.BigDecimal
import java.time.LocalDate
//GERADOR INI

const val SCF02TIPO_RECEBER = 0
const val SCF02TIPO_PAGAR = 1

class Scf02() : AbstractEntity(18) {
  constructor(scf02id: Long) : this() {
    this.scf02id = scf02id
  }

  constructor(
    scf02empresa: Long? = null,
    scf02tipo: Int,
    scf02forma: Cgs38,
    scf02entidade: Cgs80,
    scf02nossoNum: Long? = null,
    scf02nossoNumDV: Int? = null,
    scf02dtEmiss: LocalDate,
    scf02dtVenc: LocalDate,
    scf02hist: String? = null,
    scf02valor: BigDecimal,
    scf02multa: BigDecimal? = null,
    scf02juros: BigDecimal? = null,
    scf02encargos: BigDecimal? = null,
    scf02desconto: BigDecimal? = null,
    scf02regOrigem: LowerCaseMap? = null,
    scf02lancamento: Scf11? = null
  ) : this() {
    if(scf02empresa != null) this.scf02empresa = scf02empresa
    this.scf02tipo = scf02tipo
    this.scf02forma = scf02forma
    this.scf02entidade = scf02entidade
    this.scf02nossoNum = scf02nossoNum
    this.scf02nossoNumDV = scf02nossoNumDV
    this.scf02dtEmiss = scf02dtEmiss
    this.scf02dtVenc = scf02dtVenc
    this.scf02hist = scf02hist
    this.scf02valor = scf02valor
    this.scf02multa = scf02multa
    this.scf02juros = scf02juros
    this.scf02encargos = scf02encargos
    this.scf02desconto = scf02desconto
    this.scf02regOrigem = scf02regOrigem
    this.scf02lancamento = scf02lancamento
  }


  //CUSTOM INI
  //CUSTOM END

  var scf02id: Long = -1L
    get() {
      validateLoaded(0, "scf02id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var scf02empresa: Long = -1
    get() {
      validateLoaded(1, "scf02empresa", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var scf02tipo: Int = -1
    get() {
      validateLoaded(2, "scf02tipo", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var scf02forma: Cgs38 = Cgs38()
    get() {
      validateLoaded(3, "scf02forma", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    
  var scf02entidade: Cgs80 = Cgs80()
    get() {
      validateLoaded(4, "scf02entidade", true)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }
    
  var scf02nossoNum: Long? = null
    get() {
      validateLoaded(5, "scf02nossoNum", false)
      return field
    }
    set(value){
      orm.changed(field, value, 5)
      field = value
    }
    
  var scf02nossoNumDV: Int? = null
    get() {
      validateLoaded(6, "scf02nossoNumDV", false)
      return field
    }
    set(value){
      orm.changed(field, value, 6)
      field = value
    }
    
  var scf02dtEmiss: LocalDate = LocalDate.now()
    get() {
      validateLoaded(7, "scf02dtEmiss", true)
      return field
    }
    set(value){
      orm.changed(field, value, 7)
      field = value
    }
    
  var scf02dtVenc: LocalDate = LocalDate.now()
    get() {
      validateLoaded(8, "scf02dtVenc", true)
      return field
    }
    set(value){
      orm.changed(field, value, 8)
      field = value
    }
    
  var scf02hist: String? = null
    get() {
      validateLoaded(9, "scf02hist", false)
      return field
    }
    set(value){
      orm.changed(field, value, 9)
      field = value
    }
    
  var scf02valor: BigDecimal = BigDecimal("-1")
    get() {
      validateLoaded(10, "scf02valor", true)
      return field
    }
    set(value){
      orm.changed(field, value, 10)
      field = value
    }
    
  var scf02multa: BigDecimal? = null
    get() {
      validateLoaded(11, "scf02multa", false)
      return field
    }
    set(value){
      orm.changed(field, value, 11)
      field = value
    }
    
  var scf02juros: BigDecimal? = null
    get() {
      validateLoaded(12, "scf02juros", false)
      return field
    }
    set(value){
      orm.changed(field, value, 12)
      field = value
    }
    
  var scf02encargos: BigDecimal? = null
    get() {
      validateLoaded(13, "scf02encargos", false)
      return field
    }
    set(value){
      orm.changed(field, value, 13)
      field = value
    }
    
  var scf02desconto: BigDecimal? = null
    get() {
      validateLoaded(14, "scf02desconto", false)
      return field
    }
    set(value){
      orm.changed(field, value, 14)
      field = value
    }
    
  var scf02regOrigem: LowerCaseMap? = null
    get() {
      validateLoaded(15, "scf02regOrigem", false)
      return field
    }
    set(value){
      orm.changed(field, value, 15)
      field = value
    }
    
  var scf02lancamento: Scf11? = null
    get() {
      validateLoaded(16, "scf02lancamento", false)
      return field
    }
    set(value){
      orm.changed(field, value, 16)
      field = value
    }
    
  var scf021s: Set<Scf021>? = null
    get() {
      validateLoaded(17, "scf021s", false)
      return field
    }
    set(value){
      orm.changed(field, value, 17)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else scf02id
    set(value) { if(value != null)this.scf02id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Scf02
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = SCF02_METADATA
}
const val N_SCF02_EMPRESA = "scf02empresa";
const val N_SCF02_TIPO = "scf02tipo";
const val N_SCF02_FORMA = "scf02forma";
const val N_SCF02_ENTIDADE = "scf02entidade";
const val N_SCF02_NOSSO_NUM = "scf02nossoNum";
const val N_SCF02_NOSSO_NUM_DV = "scf02nossoNumDV";
const val N_SCF02_DT_EMISS = "scf02dtEmiss";
const val N_SCF02_DT_VENC = "scf02dtVenc";
const val N_SCF02_HIST = "scf02hist";
const val N_SCF02_VALOR = "scf02valor";
const val N_SCF02_MULTA = "scf02multa";
const val N_SCF02_JUROS = "scf02juros";
const val N_SCF02_ENCARGOS = "scf02encargos";
const val N_SCF02_DESCONTO = "scf02desconto";
const val N_SCF02_REG_ORIGEM = "scf02regOrigem";
const val N_SCF02_LANCAMENTO = "scf02lancamento";
const val N_SCF021S = "scf021s";

val SCF02ID = FieldMetadata("scf02id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val SCF02EMPRESA = FieldMetadata("scf02empresa", 1, "Empresa", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false);
val SCF02TIPO = FieldMetadata("scf02tipo", 2, "Tipo", FieldTypeMetadata.int, 1.0, true, null, listOf(FieldOptionMetadata(0, "Receber"),FieldOptionMetadata(1, "Pagar")), null, null, null, true, true, false);
val SCF02FORMA = FieldMetadata("scf02forma", 3, "Forma de Pagamento/Recebimento", FieldTypeMetadata.fk, 10.0, true, "Cgs38", null, null, null, null, false, false, false);
val SCF02ENTIDADE = FieldMetadata("scf02entidade", 4, "Entidade", FieldTypeMetadata.fk, 10.0, true, "Cgs80", null, null, null, null, false, false, false);
val SCF02NOSSONUM = FieldMetadata("scf02nossoNum", 5, "Nosso Número", FieldTypeMetadata.long, 12.0, false, null, null, null, null, null, false, false, false);
val SCF02NOSSONUMDV = FieldMetadata("scf02nossoNumDV", 6, "DV do Nosso Número", FieldTypeMetadata.int, 1.0, false, null, null, null, null, null, false, false, false);
val SCF02DTEMISS = FieldMetadata("scf02dtEmiss", 7, "Data de emissão", FieldTypeMetadata.date, 10.0, true, null, null, null, null, null, false, false, false);
val SCF02DTVENC = FieldMetadata("scf02dtVenc", 8, "Data de vencimento", FieldTypeMetadata.date, 10.0, true, null, null, null, null, null, false, false, false);
val SCF02HIST = FieldMetadata("scf02hist", 9, "Histórico", FieldTypeMetadata.string, 0.0, false, null, null, null, null, null, false, false, false);
val SCF02VALOR = FieldMetadata("scf02valor", 10, "Valor", FieldTypeMetadata.decimal, 16.2, true, null, null, null, null, null, false, false, false);
val SCF02MULTA = FieldMetadata("scf02multa", 11, "Valor da Multa", FieldTypeMetadata.decimal, 16.2, false, null, null, null, null, null, false, false, false);
val SCF02JUROS = FieldMetadata("scf02juros", 12, "Valor de Juros", FieldTypeMetadata.decimal, 16.2, false, null, null, null, null, null, false, false, false);
val SCF02ENCARGOS = FieldMetadata("scf02encargos", 13, "Valor de Encargos", FieldTypeMetadata.decimal, 16.2, false, null, null, null, null, null, false, false, false);
val SCF02DESCONTO = FieldMetadata("scf02desconto", 14, "Valor de Desconto", FieldTypeMetadata.decimal, 16.2, false, null, null, null, null, null, false, false, false);
val SCF02REGORIGEM = FieldMetadata("scf02regOrigem", 15, "Registro de Origem", FieldTypeMetadata.json, 0.0, false, null, null, null, null, null, false, false, false);
val SCF02LANCAMENTO = FieldMetadata("scf02lancamento", 16, "Lancamento de quitação", FieldTypeMetadata.fk, 10.0, false, "Scf11", null, null, null, null, false, false, false);
 
val SCF02_METADATA = EntityMetadata(
  name = "Scf02",
  descr = "Documento a pagar/receber",

  fields = listOf(
    SCF02ID,SCF02EMPRESA,SCF02TIPO,SCF02FORMA,SCF02ENTIDADE,SCF02NOSSONUM,SCF02NOSSONUMDV,SCF02DTEMISS,SCF02DTVENC,SCF02HIST,SCF02VALOR,SCF02MULTA,SCF02JUROS,SCF02ENCARGOS,SCF02DESCONTO,SCF02REGORIGEM,SCF02LANCAMENTO,
  ),

  keys = listOf(
  ),

  oneToMany = mapOf(
    "scf021s" to OneToManyMetadata(17, Scf021::class.java, "scf021doc"),
  )
)

//GERADOR END
