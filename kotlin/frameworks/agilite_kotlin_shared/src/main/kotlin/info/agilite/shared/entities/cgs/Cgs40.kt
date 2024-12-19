package info.agilite.shared.entities.cgs;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import info.agilite.boot.orm.annotations.EntityCacheable
import java.math.BigDecimal
//GERADOR INI

const val CGS40TIPO_RECEITA = 0
const val CGS40TIPO_DESPESAS = 1
@EntityCacheable
class Cgs40() : AbstractEntity(6) {
  constructor(cgs40id: Long) : this() {
    this.cgs40id = cgs40id
  }

  constructor(
    cgs40empresa: Long? = null,
    cgs40descr: String,
    cgs40tipo: Int,
    cgs40sup: Cgs40? = null,
    cgs40grupo: Boolean
  ) : this() {
    if(cgs40empresa != null) this.cgs40empresa = cgs40empresa
    this.cgs40descr = cgs40descr
    this.cgs40tipo = cgs40tipo
    this.cgs40sup = cgs40sup
    this.cgs40grupo = cgs40grupo
  }


  //CUSTOM INI
  //CUSTOM END

  var cgs40id: Long = -1L
    get() {
      validateLoaded(0, "cgs40id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var cgs40empresa: Long = -1
    get() {
      validateLoaded(1, "cgs40empresa", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var cgs40descr: String = "--defaultString--"
    get() {
      validateLoaded(2, "cgs40descr", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    
  var cgs40tipo: Int = -1
    get() {
      validateLoaded(3, "cgs40tipo", true)
      return field
    }
    set(value){
      orm.changed(field, value, 3)
      field = value
    }
    
  var cgs40sup: Cgs40? = null
    get() {
      validateLoaded(4, "cgs40sup", false)
      return field
    }
    set(value){
      orm.changed(field, value, 4)
      field = value
    }
    
  var cgs40grupo: Boolean = false
    get() {
      validateLoaded(5, "cgs40grupo", true)
      return field
    }
    set(value){
      orm.changed(field, value, 5)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else cgs40id
    set(value) { if(value != null)this.cgs40id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Cgs40
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = CGS40_METADATA
}
const val N_CGS40_EMPRESA = "cgs40empresa";
const val N_CGS40_DESCR = "cgs40descr";
const val N_CGS40_TIPO = "cgs40tipo";
const val N_CGS40_SUP = "cgs40sup";
const val N_CGS40_GRUPO = "cgs40grupo";

val CGS40ID = FieldMetadata("cgs40id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val CGS40EMPRESA = FieldMetadata("cgs40empresa", 1, "Empresa", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false);
val CGS40DESCR = FieldMetadata("cgs40descr", 2, "Descrição", FieldTypeMetadata.string, 30.0, true, null, null, null, null, null, true, true, true);
val CGS40TIPO = FieldMetadata("cgs40tipo", 3, "Tipo", FieldTypeMetadata.int, 1.0, true, null, listOf(FieldOptionMetadata(0, "Receita"),FieldOptionMetadata(1, "Despesas")), null, null, null, false, false, false);
val CGS40SUP = FieldMetadata("cgs40sup", 4, "Categoria Superior", FieldTypeMetadata.fk, 10.0, false, "Cgs40", null, null, null, null, false, false, false);
val CGS40GRUPO = FieldMetadata("cgs40grupo", 5, "É grupo?", FieldTypeMetadata.boolean, 1.0, true, null, null, null, null, null, false, false, false);
 
val CGS40_METADATA = EntityMetadata(
  name = "Cgs40",
  descr = "Categorias financeiras",

  fields = listOf(
    CGS40ID,CGS40EMPRESA,CGS40DESCR,CGS40TIPO,CGS40SUP,CGS40GRUPO,
  ),

  keys = listOf(
    KeyMetadata("cgs40_uk", KeyMetadataType.uk, "cgs40empresa, cgs40descr"),
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
