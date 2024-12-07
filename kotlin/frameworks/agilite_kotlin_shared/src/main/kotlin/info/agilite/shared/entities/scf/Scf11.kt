package info.agilite.shared.entities.scf;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import info.agilite.core.model.LowerCaseMap
import info.agilite.shared.entities.cgs.Cgs45
import java.math.BigDecimal
import java.time.LocalDate
//GERADOR INI

const val SCF11TIPO_RECEITA = 0
const val SCF11TIPO_DESPESA = 1
class Scf11() : AbstractEntity(8) {
  constructor(scf11id: Long) : this() {
    this.scf11id = scf11id
  }

  constructor(
    scf11empresa: Long,
    scf11tipo: Int,
    scf11conta: Cgs45,
    scf11data: LocalDate,
    scf11valor: BigDecimal,
    scf11hist: String,
    scf11regOrigem: LowerCaseMap
  ) : this() {
    this.scf11empresa = scf11empresa
    this.scf11tipo = scf11tipo
    this.scf11conta = scf11conta
    this.scf11data = scf11data
    this.scf11valor = scf11valor
    this.scf11hist = scf11hist
    this.scf11regOrigem = scf11regOrigem
  }


  var scf11id: Long = -1L
    get() {
      validateLoaded(0, "scf11id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var scf11empresa: Long = -1
    get() {
      validateLoaded(1, "scf11empresa", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var scf11tipo: Int = -1
    get() {
      validateLoaded(2, "scf11tipo", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var scf11conta: Cgs45 = Cgs45()
    get() {
      validateLoaded(3, "scf11conta", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    
  var scf11data: LocalDate = LocalDate.now()
    get() {
      validateLoaded(4, "scf11data", true)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }
    
  var scf11valor: BigDecimal = BigDecimal("-1")
    get() {
      validateLoaded(5, "scf11valor", true)
      return field
    }
    set(value){
      orm.changed(field, value, 5)
      field = value
    }
    
  var scf11hist: String = "--defaultString--"
    get() {
      validateLoaded(6, "scf11hist", true)
      return field
    }
    set(value){
      orm.changed(field, value, 6)
      field = value
    }
    
  var scf11regOrigem: LowerCaseMap? = null
    get() {
      validateLoaded(7, "scf11regOrigem", false)
      return field
    }
    set(value){
      orm.changed(field, value, 7)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else scf11id
    set(value) { if(value != null)this.scf11id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Scf11
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = SCF11_METADATA
}
const val N_SCF11EMPRESA = "scf11empresa";
const val N_SCF11TIPO = "scf11tipo";
const val N_SCF11CONTA = "scf11conta";
const val N_SCF11DATA = "scf11data";
const val N_SCF11VALOR = "scf11valor";
const val N_SCF11HIST = "scf11hist";
const val N_SCF11REGORIGEM = "scf11regOrigem";

val SCF11ID = FieldMetadata("scf11id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val SCF11EMPRESA = FieldMetadata("scf11empresa", 1, "Empresa", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false);
val SCF11TIPO = FieldMetadata("scf11tipo", 2, "Tipo", FieldTypeMetadata.int, 1.0, true, null, listOf(FieldOptionMetadata(0, "Receita"),FieldOptionMetadata(1, "Despesa")), null, null, null, true, true, false);
val SCF11CONTA = FieldMetadata("scf11conta", 3, "Conta", FieldTypeMetadata.fk, 10.0, true, "Cgs45", null, null, null, null, true, true, false);
val SCF11DATA = FieldMetadata("scf11data", 4, "Data", FieldTypeMetadata.date, 10.0, true, null, null, null, null, null, true, true, false);
val SCF11VALOR = FieldMetadata("scf11valor", 5, "Valor", FieldTypeMetadata.decimal, 16.2, true, null, null, null, null, null, false, false, false);
val SCF11HIST = FieldMetadata("scf11hist", 6, "Histórico", FieldTypeMetadata.string, 0.0, true, null, null, null, null, null, false, false, false);
val SCF11REGORIGEM = FieldMetadata("scf11regOrigem", 7, "Registro de origem", FieldTypeMetadata.json, 0.0, false, null, null, null, null, null, false, false, false);
 
val SCF11_METADATA = EntityMetadata(
  name = "Scf11",
  descr = "Lançamentos financeiros",

  fields = listOf(
    SCF11ID,SCF11EMPRESA,SCF11TIPO,SCF11CONTA,SCF11DATA,SCF11VALOR,SCF11HIST,SCF11REGORIGEM,
  ),

  keys = listOf(
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
