package info.agilite.cgl.domain.entities;
import com.fasterxml.jackson.annotation.JsonIgnore
import info.agilite.boot.metadata.models.EntityMetadata
import info.agilite.boot.metadata.models.FieldMetadata
import info.agilite.boot.metadata.models.FieldTypeMetadata
import info.agilite.boot.metadata.models.KeyMetadata
import info.agilite.boot.orm.AbstractEntity
//GERADOR INI

class Cgl29() : AbstractEntity(2) {
  constructor(cgl29id: Long) : this() {
    this.cgl29id = cgl29id
  }

  constructor(
    cgl29nome: String
  ) : this() {
    this.cgl29nome = cgl29nome
  }


  var cgl29id: Long = -1L
    get() {
      validateLoaded(0, "cgl29id", true)
      return field
    }
    set(value) {
      orm.onIdChange(value)
      field = value
    }
    
  var cgl29nome: String = "--defaultString--"
    get() {
      validateLoaded(1, "cgl29nome", true)
      return field
    }
    set(value){
      orm.changed(field, value, 1)
      field = value
    }
    

  override var id: Long?
    @JsonIgnore get() = if(cgl29id == -1L) null else cgl29id
    set(value) { if(value != null)this.cgl29id = value }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Cgl29
    return id != null && id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 1
  }


  override fun getMetadata() = CGL29_METADATA
}
const val N_CGL29NOME = "cgl29nome";

val CGL29ID = FieldMetadata("cgl29id", 0, "ID", FieldTypeMetadata.id, 10.0, true, null, null, null, null, null, false, false, false);
val CGL29NOME = FieldMetadata("cgl29nome", 1, "Nome", FieldTypeMetadata.string, 50.0, true, null, null, null, null, null, false, false, false);
 
val CGL29_METADATA = EntityMetadata(
  name = "Cgl29",
  descr = "Credencial",

  fields = listOf(
    CGL29ID,CGL29NOME,
  ),

  keys = listOf(
    KeyMetadata("cgl29_uk", KeyMetadataType.uk, "cgl29nome"),
  ),

  oneToMany = mapOf(
  )
)

//GERADOR END
