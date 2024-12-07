package info.agilite.shared.entities.scf;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.*
import info.agilite.boot.orm.AbstractEntity
import java.math.BigDecimal
//GERADOR INI

class Scf10() : AbstractEntity(3) {
  constructor(scf10id: Long) : this() {
    this.scf10id = scf10id
  }

  constructor(
    scf10empresa: Long,
    scf10nome: String
  ) : this() {
    this.scf10empresa = scf10empresa
    this.scf10nome = scf10nome
  }


  var scf10id: Long = -1L
    get() {
      validateLoaded(0, "scf10id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var scf10empresa: Long = -1
    get() {
      validateLoaded(1, "scf10empresa", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    
  var scf10nome: String = "--defaultString--"
    get() {
      validateLoaded(2, "scf10nome", true)
      return field
    }
    set(value){
      orm.changed(field, value, 2)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(!isIdDefined()) null else scf10id
    set(value) { if(value != null)this.scf10id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Scf10
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = SCF10_METADATA
}
const val N_SCF10EMPRESA = "scf10empresa";
const val N_SCF10NOME = "scf10nome";

val SCF10ID = FieldMetadata("scf10id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val SCF10EMPRESA = FieldMetadata("scf10empresa", 1, "Empresa", FieldTypeMetadata.long, 10.0, true, null, null, null, null, null, false, false, false);
val SCF10NOME = FieldMetadata("scf10nome", 2, "Nome", FieldTypeMetadata.string, 30.0, true, null, null, null, null, null, true, true, true);
 
val SCF10_METADATA = EntityMetadata(
  name = "Scf10",
  descr = "Conta Corrente",

  fields = listOf(
    SCF10ID,SCF10EMPRESA,SCF10NOME,
  ),

  keys = listOf(
    KeyMetadata("scf10_uk", KeyMetadataType.uk, "scf10empresa, scf10nome"),
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
