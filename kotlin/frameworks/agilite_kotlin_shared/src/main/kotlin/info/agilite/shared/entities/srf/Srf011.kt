package info.agilite.shared.entities.srf;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import info.agilite.shared.entities.cgs.Cgs50
import java.math.BigDecimal
//GERADOR INI


class Srf011() : AbstractEntity(9) {
  constructor(srf011id: Long) : this() {
    this.srf011id = srf011id
  }

  constructor(
    srf011doc: Long? = null,
    srf011item: Cgs50,
    srf011descr: String,
    srf011qtd: BigDecimal,
    srf011vlrUnit: BigDecimal,
    srf011vlrTotal: BigDecimal,
    srf011alqIss: BigDecimal? = null,
    srf011vlrIss: BigDecimal? = null
  ) : this() {
    if(srf011doc != null) this.srf011doc = srf011doc
    this.srf011item = srf011item
    this.srf011descr = srf011descr
    this.srf011qtd = srf011qtd
    this.srf011vlrUnit = srf011vlrUnit
    this.srf011vlrTotal = srf011vlrTotal
    this.srf011alqIss = srf011alqIss
    this.srf011vlrIss = srf011vlrIss
  }


  //CUSTOM INI
  constructor(
    srf011item: Cgs50,
    srf011descr: String? = null,
    srf011qtd: BigDecimal,
    srf011vlrUnit: BigDecimal,
  ) : this(
    srf011item = srf011item,
    srf011descr = srf011descr ?: srf011item.cgs50descr,
    srf011qtd = srf011qtd,
    srf011vlrUnit = srf011vlrUnit,
    srf011vlrTotal = srf011qtd * srf011vlrUnit,
  )
  //CUSTOM END

  var srf011id: Long = -1L
    get() {
      validateLoaded(0, "srf011id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var srf011doc: Long = -1
    get() {
      validateLoaded(1, "srf011doc", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var srf011item: Cgs50 = Cgs50()
    get() {
      validateLoaded(2, "srf011item", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var srf011descr: String = "--defaultString--"
    get() {
      validateLoaded(3, "srf011descr", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    
  var srf011qtd: BigDecimal = BigDecimal("-1")
    get() {
      validateLoaded(4, "srf011qtd", true)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }
    
  var srf011vlrUnit: BigDecimal = BigDecimal("-1")
    get() {
      validateLoaded(5, "srf011vlrUnit", true)
      return field
    }
    set(value){
      orm.changed(field, value, 5)
      field = value
    }
    
  var srf011vlrTotal: BigDecimal = BigDecimal("-1")
    get() {
      validateLoaded(6, "srf011vlrTotal", true)
      return field
    }
    set(value){
      orm.changed(field, value, 6)
      field = value
    }
    
  var srf011alqIss: BigDecimal? = null
    get() {
      validateLoaded(7, "srf011alqIss", false)
      return field
    }
    set(value){
      orm.changed(field, value, 7)
      field = value
    }
    
  var srf011vlrIss: BigDecimal? = null
    get() {
      validateLoaded(8, "srf011vlrIss", false)
      return field
    }
    set(value){
      orm.changed(field, value, 8)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else srf011id
    set(value) { if(value != null)this.srf011id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Srf011
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = SRF011_METADATA
}
const val N_SRF011_DOC = "srf011doc";
const val N_SRF011_ITEM = "srf011item";
const val N_SRF011_DESCR = "srf011descr";
const val N_SRF011_QTD = "srf011qtd";
const val N_SRF011_VLR_UNIT = "srf011vlrUnit";
const val N_SRF011_VLR_TOTAL = "srf011vlrTotal";
const val N_SRF011_ALQ_ISS = "srf011alqIss";
const val N_SRF011_VLR_ISS = "srf011vlrIss";

val SRF011ID = FieldMetadata("srf011id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val SRF011DOC = FieldMetadata("srf011doc", 1, "Documento", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false);
val SRF011ITEM = FieldMetadata("srf011item", 2, "Item", FieldTypeMetadata.fk, 10.0, true, "Cgs50", null, null, null, null, false, false, false);
val SRF011DESCR = FieldMetadata("srf011descr", 3, "Descrição", FieldTypeMetadata.string, 0.0, true, null, null, null, null, null, false, false, false);
val SRF011QTD = FieldMetadata("srf011qtd", 4, "Quantidade", FieldTypeMetadata.decimal, 16.6, true, null, null, null, null, null, false, false, false);
val SRF011VLRUNIT = FieldMetadata("srf011vlrUnit", 5, "Unitário", FieldTypeMetadata.decimal, 16.6, true, null, null, null, null, null, false, false, false);
val SRF011VLRTOTAL = FieldMetadata("srf011vlrTotal", 6, "Valor total", FieldTypeMetadata.decimal, 16.2, true, null, null, null, null, null, false, false, false);
val SRF011ALQISS = FieldMetadata("srf011alqIss", 7, "Aliquota ISS", FieldTypeMetadata.decimal, 4.2, false, null, null, null, null, null, false, false, false);
val SRF011VLRISS = FieldMetadata("srf011vlrIss", 8, "Valor do ISS", FieldTypeMetadata.decimal, 16.2, false, null, null, null, null, null, false, false, false);
 
val SRF011_METADATA = EntityMetadata(
  name = "Srf011",
  descr = "Itens",

  fields = listOf(
    SRF011ID,SRF011DOC,SRF011ITEM,SRF011DESCR,SRF011QTD,SRF011VLRUNIT,SRF011VLRTOTAL,SRF011ALQISS,SRF011VLRISS,
  ),

  keys = listOf(
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
